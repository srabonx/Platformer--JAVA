package objects;

import java.awt.geom.Rectangle2D;

import main.Game;

import static utils.Constants.Projectiles.*;

public class Projectile {

	private Rectangle2D.Float hitbox;
	private int direction;
	private boolean active = true;

	public Projectile(int x, int y, int direction) {

		int xOffset = (int) (-3 * Game.TILES_SCALE);
		int yOffset = (int) (5 * Game.TILES_SCALE);

		if (direction == 1)
			xOffset = (int) (29 * Game.TILES_SCALE);

		hitbox = new Rectangle2D.Float(x + xOffset, y+yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
		this.direction = direction;
	}

	public void updatePosition() {
		hitbox.x += direction * SPEED;
	}

	public void setPosition(int x, int y) {
		hitbox.x = x;
		hitbox.y = y;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
