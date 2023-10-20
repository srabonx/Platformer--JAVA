package entities;

import static utils.Constants.ANIMATION_SPEED;
import static utils.Constants.ExtrAnimation.FALL;
import static utils.Constants.ExtrAnimation.FALL_DEFAULT_HEIGHT;
import static utils.Constants.ExtrAnimation.FALL_DEFAULT_WIDTH;
import static utils.Constants.ExtrAnimation.GetSpriteAmount;
import static utils.Constants.ExtrAnimation.JAMP;
import static utils.Constants.ExtrAnimation.RUN;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;

public abstract class Entity {

	protected float x, y;
	protected int width, height;
	protected int aniIndex, aniTick;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth;
	protected int currentHealth;
	protected float walkSpeed = 1.0f * Game.TILES_SCALE;

	protected Rectangle2D.Float hitbox;
	protected Rectangle2D.Float attackBox;
	
	protected boolean isMoving = false, isAttacking = false;
	
	
	// extra animation
		
		protected BufferedImage[][] fallAni;
	
		protected int extraAniState;
		protected int eAniTick;
		protected int eAniIndex;

		protected int eWidth = 52;
		protected int eHeight = 20;

		protected int EflipX = 0;
		protected int RflipX = 0;
		protected int jumpY;
		protected int jumpX;
		protected boolean isFalling = false;
		protected boolean isRunning = false;
		protected boolean isDead = false;
	
	

	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		initFallAni();

	}

	protected void drawAttackBox(Graphics g, int lvlOffset) {
		g.setColor(Color.RED);
		g.drawRect((int) attackBox.x - lvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	protected void drawHitbox(Graphics g, int xLvlOffset) {

		// for debugging only
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
		
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.TILES_SCALE), (int) (height * Game.TILES_SCALE));
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public int getState() {
		return state;
	}

	public int getAniIndex() {
		return aniIndex;
	}
	
	
	
	// EXTRA ANIMATIONS
	
	protected void initFallAni() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.FALL_JUMP_RUN_TRAIL);
		fallAni = new BufferedImage[3][6];

		for (int i = 0; i < fallAni.length; i++)
			for (int j = 0; j < fallAni[i].length; j++)
				fallAni[i][j] = img.getSubimage(j * FALL_DEFAULT_WIDTH, i * FALL_DEFAULT_HEIGHT, FALL_DEFAULT_WIDTH,
						FALL_DEFAULT_HEIGHT);

	}
	
	protected void setExtraAnimation() {
		int startAni = extraAniState;

		if (isMoving) {
			extraAniState = RUN;
			isRunning = true;
		}
		if (inAir)
			if (airSpeed < 0)
				extraAniState = JAMP;
			else {
				extraAniState = FALL;
				jumpX = (int) hitbox.x;
				jumpY = (int) hitbox.y;
				isFalling = true;

			}
		if (startAni != extraAniState)
			resetExtraAniTick();

	}

	protected void resetExtraAniTick() {
		eAniIndex = 0;
		eAniTick = 0;

	}

	protected void updateExtraAniTick() {

		eAniTick++;

		if (eAniTick >= ANIMATION_SPEED) {
			eAniTick = 0;
			eAniIndex++;
			if (eAniIndex >= GetSpriteAmount(extraAniState)) {
				eAniIndex = 0;

				isRunning = false;
				isFalling = false;

			}
		}

	}
	
}
