package objects;

import main.Game;

public class Cannon extends GameObject {

	private int tileY;
	private boolean firstUpdate;
	
	public Cannon(int x, int y, int objectType) {
		super(x, y, objectType);

		tileY = y / Game.TILES_SIZE;
		initHitbox(40, 26);
		hitbox.x -= (int) (4 * Game.TILES_SCALE);
		hitbox.y += (int) (6 * Game.TILES_SCALE);
		
		firstUpdate = true;
	}

	public void update() {
		if (doAnimation)
			updateAnimationTick();
		
	}

	public int getY() {
		return tileY;
	}
	
	public boolean getFirstUpdate() {
		return firstUpdate;
	}
	
	public void setFirstUpdate(boolean firstUpdate) {
		this.firstUpdate = firstUpdate;
	}

}
