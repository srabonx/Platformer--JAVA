package entities;

import static utils.Constants.Direction.*;
import static utils.Constants.ExtrAnimation.*;
import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.CanMoveHere;
import static utils.HelpMethods.GetEntityPosUnderRoofOrAboveFloor;
import static utils.HelpMethods.IsEntityOnFloor;
import static utils.HelpMethods.IsFloor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class Crabby extends Enemy {

	// Attack Box
	private int attackBoxOffsetX;

	public Crabby(float x, float y) {
		super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);

		initHitbox(22, 19);
		initAttackBox();

	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.TILES_SCALE), (int) (19 * Game.TILES_SCALE));
		attackBoxOffsetX = (int) (30 * Game.TILES_SCALE);

	}

	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();
		updateAttackBox();

	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;

	}

	public void updateBehavior(int[][] lvlData, Player player) {

		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			updateInAir(lvlData);
		else {
			switch (state) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, player))
					turnTowardPlayer(player);
				if (isPlayerCloseForAttack(player))
					newState(ATTACK);
				move(lvlData);
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				if (aniIndex == 4 && !attackChecked)
					checkPlayerHit(attackBox, player);
				break;
			case HIT:
				if (aniIndex == 4)
					newState(IDLE);

				break;
			}
		}

	}

	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}

}
