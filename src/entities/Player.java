package entities;

import static utils.Constants.GRAVITY;
import static utils.Constants.ExtrAnimation.*;
import static utils.Constants.ANIMATION_SPEED;

import static utils.Constants.PlayerConstants.GetSpriteAmmount;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import javax.print.attribute.standard.Finishings;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

public class Player extends Entity {

	private BufferedImage[][] animations;

	private boolean left, right, up, down, jump;

	private int[][] lvlData;

	private float xDrawOffset = 21 * Game.TILES_SCALE;
	private float yDrawOffset = 4 * Game.TILES_SCALE;

	// jumping/Gravity
	private float jumpSpeed = -2.25f * Game.TILES_SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.TILES_SCALE;

	// Status Bar

	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.TILES_SCALE);
	private int statusBarHeight = (int) (58 * Game.TILES_SCALE);
	private int statusBarX = (int) (10 * Game.TILES_SCALE);
	private int statusBarY = (int) (10 * Game.TILES_SCALE);

	private int healthBarWidth = (int) (150 * Game.TILES_SCALE);
	private int healthBarHeight = (int) (4 * Game.TILES_SCALE);
	private int healthBarXStart = (int) (34 * Game.TILES_SCALE);
	private int healthBarYStart = (int) (14 * Game.TILES_SCALE);

	private int healthWidth = healthBarWidth;

	private int flipX = 0;
	private int flipW = 1;

	private int tileY = 0;

	private boolean attackChecked;
	private Playing playing;

	// Power Attack

	private int powerBarWidth = (int) (104 * Game.TILES_SCALE);
	private int powerBarHeight = (int) (2 * Game.TILES_SCALE);
	private int powerBarXStart = (int) (44 * Game.TILES_SCALE);
	private int powerBarYStart = (int) (34 * Game.TILES_SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;

	private boolean powerAttackActive;
	private int powerAttackTick;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);

		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = 1.0f * Game.TILES_SCALE;
		loadAnimations();
		initHitbox(20, 27);
		initAttackBox();

		initFallAni();
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.TILES_SCALE), (int) (20 * Game.TILES_SCALE));

	}

	public void update() {
		updateHealthBar();
		updatePowerBar();

		if (currentHealth <= 0) {
			if (state != DIED) {
				state = DIED;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
			} else if (aniIndex == GetSpriteAmmount(DIED) - 1 && aniTick >= ANIMATION_SPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else
				updateAnimationTick();

			return;
		}

		updateAttackBox();

		updatePos();

		if (isMoving) {
			checkPotionTouched();
			checkSpikeTouched();
			tileY = (int) hitbox.y / Game.TILES_SIZE;
			if (powerAttackActive) {
				powerAttackTick++;
				if (powerAttackTick >= 35) {
					powerAttackTick = 0;
					powerAttackActive = false;
				}

			}

		}
		if (isAttacking || powerAttackActive)
			checkAttack();

		updateAnimationTick();
		setAnimation();

		updateExtraAniTick();
		setExtraAnimation();

	}

	private void checkSpikeTouched() {
		playing.checkSpikeTouched(this);
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);

	}

	private void checkAttack() {
		if (attackChecked || aniIndex != 1)
			return;
		attackChecked = true;

		if (powerAttackActive)
			attackChecked = false;

		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();

	}

	private void updateAttackBox() {
		if (right || (powerAttackActive && flipW == 1))
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.TILES_SCALE * 10);
		else if (left || (powerAttackActive && flipW == -1))
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.TILES_SCALE * 10);

		attackBox.y = hitbox.y + Game.TILES_SCALE * 10;

	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);

	}

	private void updatePowerBar() {
		powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

		powerGrowTick++;
		if (powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(1);
		}
	}

	public void render(Graphics g, int lvlOffset) {
		if (state == JUMP)
			g.drawImage(fallAni[JAMP][eAniIndex], jumpX - (int) (15 * Game.TILES_SCALE) - lvlOffset + EflipX,
					jumpY - (int) yDrawOffset, (int) (flipW * (52 * Game.TILES_SCALE)), (int) (32 * Game.TILES_SCALE),
					null);
		else if (isFalling) {
			if (state == IDLE || state == RUNNING)
				g.drawImage(fallAni[FALL][eAniIndex], jumpX - (int) (15 * Game.TILES_SCALE) - lvlOffset + EflipX,
						jumpY - (int) yDrawOffset, (int) (flipW * (52 * Game.TILES_SCALE)),
						(int) (32 * Game.TILES_SCALE), null);
		} else if (isRunning)
			g.drawImage(fallAni[RUN][eAniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + RflipX,
					(int) hitbox.y - (int) yDrawOffset, (int) (flipW * (52 * Game.TILES_SCALE)),
					(int) (32 * Game.TILES_SCALE), null);

		// Draw Player
		g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset), flipW * width, height, null);

		// drawHitbox(g, lvlOffset);
		drawAttackBox(g, lvlOffset);
		drawUi(g);
	}

	private void drawUi(Graphics g) {
		// background ui
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

		// health bar
		g.setColor(Color.RED);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

		// power Bar
		g.setColor(Color.yellow);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);

	}

	public void changeHealth(int value) {
		currentHealth += value;
		if (currentHealth <= 0) {
			currentHealth = 0;
			// gameOver();
		} else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

	public void kill() {
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
		currentHealth = 0;

	}

	public void changePower(int value) {
		powerValue += value;
		if (powerValue >= powerMaxValue)
			powerValue = powerMaxValue;
		else if (powerValue <= 0)
			powerValue = 0;

	}

	private void updateAnimationTick() {

		aniTick++;

		if (aniTick >= ANIMATION_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmmount(state)) {
				aniIndex = 0;

				isAttacking = false;
				attackChecked = false;

			}
		}
	}

	private void setAnimation() {

		int startAni = state;

		if (isMoving)
			state = RUNNING;
		else
			state = IDLE;

		if (inAir)
			if (airSpeed < 0)
				state = JUMP;
			else
				state = FALLING;
		if (powerAttackActive) {
			state = ATTACK_JUMP_2;
			aniIndex = 1;
			aniTick = 0;
			return;
		}

		if (isAttacking) {
			state = ATTACK_1;
			/*
			 * if(startAni != ATTACK_1) { aniIndex = 1; aniTick = 0; return; }
			 */
		}

		if (startAni != state)
			resetAniTick();

	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;

	}

	private void updatePos() {

		isMoving = false;

		if (jump)
			jump();

		if (!inAir)
			if (!powerAttackActive)
				if ((!left && !right) || (left && right))
					return;

		float xSpeed = 0;
		float ySpeed = 0;

		if (right) {
			xSpeed += walkSpeed;
			flipX = 0;
			EflipX = 0;
			RflipX = 0;
			flipW = 1;
		}
		if (left) {
			xSpeed -= walkSpeed;
			flipX = width;
			EflipX = (int) (52 * Game.TILES_SCALE);
			RflipX = (int) (65 * Game.TILES_SCALE);
			flipW = -1;
		}

		if (powerAttackActive) {
			if ((!left && !right) || (left && right)) {
				if (flipW == -1)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;
			}

			xSpeed *= 3;
		}
		if (!inAir)
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;

		if (inAir && !powerAttackActive) {

			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {

				hitbox.y += airSpeed;
				airSpeed += GRAVITY;

				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityPosUnderRoofOrAboveFloor(hitbox, airSpeed);

				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;

				updateXPos(xSpeed);

			}

		} else
			updateXPos(xSpeed);

		isMoving = true;

	}

	private void jump() {
		if (inAir)
			return;

		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
		jumpY = (int) hitbox.y;
		jumpX = (int) hitbox.x;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;

	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
			if (powerAttackActive)
				powerAttackActive = false;
			powerAttackTick = 0;
		}

	}

	private void loadAnimations() {

		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

		animations = new BufferedImage[10][8];

		for (int i = 0; i < animations.length; i++)
			for (int j = 0; j < animations[j].length; j++) {
				animations[i][j] = img.getSubimage(j * 64, i * 40, 64, 40);
			}

		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.HEALTH_POWER_BAR);
	}

	public void loadLevelData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}

	public void setAttacking(boolean attacking) {
		this.isAttacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public int getTileY() {
		return tileY;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		isAttacking = false;
		isMoving = false;
		jump = false;
		state = IDLE;
		currentHealth = maxHealth;
		powerValue = powerMaxValue;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;

	}

	public void powerAttack() {
		if (powerAttackActive)
			return;
		if (powerValue >= 60) {
			powerAttackActive = true;
			changePower(-60);
		}

	}

}
