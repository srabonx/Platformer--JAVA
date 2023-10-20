package objects;

import main.Game;

public class Potion extends GameObject {

	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;;

	public Potion(int x, int y, int objectType) {
		super(x, y, objectType);
		doAnimation = true;
		initHitbox(7, 14);
		xDrawOffset = (int) (3 * Game.TILES_SCALE);
		yDrawOffset = (int) (2 * Game.TILES_SCALE);

		maxHoverOffset = (int) (10 * Game.TILES_SCALE);
	}

	public void update() {
		updateAnimationTick();
		updateHover();
	}

	private void updateHover() {
		hoverOffset += (0.075f * Game.TILES_SCALE * hoverDir);
		
		if(hoverOffset >= maxHoverOffset)
			hoverDir = -1;
		else if(hoverOffset < 0)
			hoverDir = 1;
		
		hitbox.y = y+hoverOffset;
	}

}
