package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utils.LoadSave;

import static utils.Constants.ObjectConstants.*;
import static utils.Constants.Projectiles.*;
import static utils.HelpMethods.CanCannonSeePlayer;
import static utils.HelpMethods.IsProjectileHittingLevel;;

public class ObjectManager {

	private Playing playing;

	private BufferedImage[][] potionImages, containerImages;
	private BufferedImage[] cannonImages;
	private BufferedImage spikeImg;
	private BufferedImage cannonBallImg;

	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Spike> spikes;
	private ArrayList<Cannon> cannons;
	private ArrayList<Projectile> cannonBalls = new ArrayList<>();

	private int cannonTick;

	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();

	}

	public void checkSpikesTouched(Player p) {
		for (Spike s : spikes)
			if (s.getHitbox().intersects(p.getHitbox()))
				p.kill();
	}

	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for (Potion p : potions)
			if (p.isActive()) {
				if (hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					applyEffectToPlayer(p);
				}
			}

	}

	public void applyEffectToPlayer(Potion p) {
		if (p.getObjectType() == RED_POTION)
			playing.getPlayer().changeHealth(RED_POTION_VALUE);
		else
			playing.getPlayer().changePower(BLUE_POTION_VALUE);
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {

		for (GameContainer gc : containers)
			if (gc.isActive() && !gc.doAnimation) {
				if (gc.getHitbox().intersects(attackBox)) {
					gc.setAnimation(true);

					int type = 0;

					if (gc.getObjectType() == BARREL)
						type = 1;

					potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2),
							(int) (gc.getHitbox().y - gc.getHitbox().height / 2), type));

					return;
				}

			}

	}

	public void loadObjects(Level newLevel) {
		potions = new ArrayList<>(newLevel.getPotions());
		containers = new ArrayList<>(newLevel.getContainers());
		spikes = newLevel.getSpikes();
		cannons = newLevel.getCannons();
		cannonBalls.clear();

	}

	private void loadImgs() {

		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_SPRITE);
		BufferedImage containerSprites = LoadSave.GetSpriteAtlas(LoadSave.OBJECTS_SPRITE);
		spikeImg = LoadSave.GetSpriteAtlas(LoadSave.SPIKE_ATLAS);
		BufferedImage cannonSprite = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
		cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);

		potionImages = new BufferedImage[2][7];
		containerImages = new BufferedImage[2][8];
		cannonImages = new BufferedImage[7];

		for (int i = 0; i < potionImages.length; i++)
			for (int j = 0; j < potionImages[i].length; j++)
				potionImages[i][j] = potionSprite.getSubimage(j * 12, i * 16, 12, 16);

		for (int i = 0; i < containerImages.length; i++)
			for (int j = 0; j < containerImages[i].length; j++)
				containerImages[i][j] = containerSprites.getSubimage(j * 40, i * 30, 40, 30);

		for (int i = 0; i < cannonImages.length; i++)
			cannonImages[i] = cannonSprite.getSubimage(i * 40, 0, 40, 26);

	}

	public void update(int[][] lvlData, Player player) {
		for (Potion p : potions)
			if (p.isActive())
				p.update();

		for (GameContainer gc : containers)
			if (gc.isActive())
				gc.update();

		updateCannons(lvlData, player);
		updateProjectiles(lvlData, player);

	}

	private void updateProjectiles(int[][] lvlData, Player player) {
		for (Projectile cb : cannonBalls)
			if (cb.isActive()) {
				cb.updatePosition();
				if (cb.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-25);
					cb.setActive(false);
				} else if (IsProjectileHittingLevel(cb, lvlData))
					cb.setActive(false);

			}
	}

	private void updateCannons(int[][] lvlData, Player player) {
		for (Cannon c : cannons)
			if (c.isActive()) {
				if (!c.doAnimation) 
					if (c.getY() == player.getTileY()) 
						if (isPlayerInRange(c, player)) 
							if (isPlayerInfrontOfCannon(c, player)) {
								if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getY()))

									if (c.getFirstUpdate()) {
										c.setAnimation(true);
										c.setFirstUpdate(false);
									}

								cannonTick++;
								if (cannonTick >= 200) {
									c.setFirstUpdate(true);
									cannonTick = 0;
								}
							}else
								c.setFirstUpdate(true);

				c.update();
				if (c.getAniIndex() == 4 && c.getAniTick() == 0)
					shootCannon(c);
			}

		// if the cannon is not animating
		// same y tile
		// player is in range
		// player in front
		// line of sight
		// shoot

	}

	private void shootCannon(Cannon c) {

		int dir = 1;
		if (c.getObjectType() == CANNON_LEFT)
			dir = -1;

		cannonBalls.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));

	}

	private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
		if (c.getObjectType() == CANNON_LEFT) {
			if (c.getHitbox().x > player.getHitbox().x)
				return true;
		} else if (c.getHitbox().x < player.getHitbox().x)
			return true;

		return false;

	}

	private boolean isPlayerInRange(Cannon c, Player player) {
		int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
		return absValue <= Game.TILES_SIZE * 5;
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawPotions(g, xLvlOffset);
		drawContainers(g, xLvlOffset);
		drawTraps(g, xLvlOffset);
		drawCannons(g, xLvlOffset);
		drawCannonBall(g, xLvlOffset);
	}

	private void drawCannonBall(Graphics g, int xLvlOffset) {

		for (Projectile p : cannonBalls)
			if (p.isActive())
				g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y),
						CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);

	}

	private void drawCannons(Graphics g, int xLvlOffset) {
		for (Cannon c : cannons)
			if (c.isActive()) {

				int x = (int) (c.getHitbox().x - xLvlOffset);
				int width = CANNON_WIDTH;

				if (c.objectType == CANNON_RIGHT) {
					x += width;
					width *= -1;
				}

				g.drawImage(cannonImages[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);

			}

	}

	private void drawTraps(Graphics g, int xLvlOffset) {
		for (Spike sp : spikes)
			g.drawImage(spikeImg, (int) sp.getHitbox().x - xLvlOffset, (int) sp.getHitbox().y - sp.getyDrawOffset(),
					SPIKE_WIDTH, SPIKE_HEIGHT, null);
	}

	private void drawContainers(Graphics g, int xLvlOffset) {
		for (GameContainer gc : containers)
			if (gc.isActive()) {
				int type = 0;

				if (gc.objectType == BARREL)
					type = 1;

				g.drawImage(containerImages[type][gc.getAniIndex()],
						(int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
						(int) (gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH, CONTAINER_HEIGHT, null);

			}
	}

	private void drawPotions(Graphics g, int xLvlOffset) {
		for (Potion p : potions)
			if (p.isActive()) {
				int type = 0;
				if (p.objectType == RED_POTION)
					type = 1;

				g.drawImage(potionImages[type][p.getAniIndex()],
						(int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
						(int) (p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT, null);

			}

	}

	public void resetAllObjects() {

		loadObjects(playing.getLevelManager().getLevel());

		for (Potion p : potions)
			p.reset();

		for (GameContainer gc : containers)
			gc.reset();

		for (Cannon c : cannons)
			c.reset();

	}

}
