package objects;

import static utils.Constants.ObjectConstants.*;

import main.Game;

public class GameContainer extends GameObject {

	public GameContainer(int x, int y, int objectType) {
		super(x, y, objectType);
		createHitbox();
	}

	private void createHitbox() {

		if (objectType == BOX) {
			initHitbox(25, 18);

			xDrawOffset = (int) (7 * Game.TILES_SCALE);
			yDrawOffset = (int) (12 * Game.TILES_SCALE);

		} else {
			initHitbox(23, 25);

			xDrawOffset = (int) (8 * Game.TILES_SCALE);
			yDrawOffset = (int) (5 * Game.TILES_SCALE);
		}

		hitbox.y += yDrawOffset + (int) (Game.TILES_SCALE * 2);
		hitbox.x += xDrawOffset / 2;

	}

	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

}
