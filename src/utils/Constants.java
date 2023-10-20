package utils;

import main.Game;

public class Constants {

	public static final float GRAVITY = 0.04f * Game.TILES_SCALE;
	public static final int ANIMATION_SPEED = 25;

	public static class ExtrAnimation {

		public static final int FALL = 0;
		public static final int JAMP = 1;
		public static final int RUN = 2;

		public static final int FALL_DEFAULT_WIDTH = 52;
		public static final int FALL_DEFAULT_HEIGHT = 20;
		public static final int FALL_WIDTH = (int) (FALL_DEFAULT_WIDTH * Game.TILES_SCALE);
		public static final int FALL_HEIGHT = (int) (FALL_DEFAULT_HEIGHT * Game.TILES_SCALE);

		public static int GetSpriteAmount(int state) {
			switch (state) {
			case FALL:
				return 5;
			case JAMP:
				return 6;
			case RUN:
				return 5;
			default:
				return 1;
			}

		}
	}

	public static class Projectiles {

		public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
		public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
		public static final int CANNON_BALL_WIDTH = (int) (CANNON_BALL_DEFAULT_WIDTH * Game.TILES_SCALE);
		public static final int CANNON_BALL_HEIGHT = (int) (CANNON_BALL_DEFAULT_HEIGHT * Game.TILES_SCALE);
		public static final float SPEED = 0.5f * Game.TILES_SCALE;
	}

	public static class ObjectConstants {

		public static final int RED_POTION = 0;
		public static final int BLUE_POTION = 1;
		public static final int BARREL = 2;
		public static final int BOX = 3;
		public static final int SPIKE = 4;
		public static final int CANNON_LEFT = 5;
		public static final int CANNON_RIGHT = 6;

		public static final int RED_POTION_VALUE = 15;
		public static final int BLUE_POTION_VALUE = 10;

		public static final int CONTAINER_WIDTH_DEFAULT = 40;
		public static final int CONTAINER_HEIGHT_DEFAULT = 30;
		public static final int CONTAINER_WIDTH = (int) (CONTAINER_WIDTH_DEFAULT * Game.TILES_SCALE);
		public static final int CONTAINER_HEIGHT = (int) (CONTAINER_HEIGHT_DEFAULT * Game.TILES_SCALE);

		public static final int POTION_WIDTH_DEFAULT = 12;
		public static final int POTION_HEIGHT_DEFAULT = 16;
		public static final int POTION_WIDTH = (int) (POTION_WIDTH_DEFAULT * Game.TILES_SCALE);
		public static final int POTION_HEIGHT = (int) (POTION_HEIGHT_DEFAULT * Game.TILES_SCALE);

		public static final int SPIKE_WIDTH_DEFAULT = 32;
		public static final int SPIKE_HEIGHT_DEFAULT = 32;
		public static final int SPIKE_WIDTH = (int) (SPIKE_WIDTH_DEFAULT * Game.TILES_SCALE);
		public static final int SPIKE_HEIGHT = (int) (SPIKE_HEIGHT_DEFAULT * Game.TILES_SCALE);

		public static final int CANNON_WIDTH_DEFAULT = 40;
		public static final int CANNON_HEIGHT_DEFAULT = 26;
		public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * Game.TILES_SCALE);
		public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * Game.TILES_SCALE);

		public static int GetSpriteAmount(int objectType) {

			switch (objectType) {
			case RED_POTION, BLUE_POTION:
				return 7;
			case BARREL, BOX:
				return 8;
			case CANNON_LEFT, CANNON_RIGHT:
				return 7;

			}

			return 1;

		}

	}

	public static class EnemyConstants {
		public static final int CRABBY = 0;

		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;

		public static final int CRABBY_WIDTH_DEFAULT = 72;
		public static final int CRABBY_HEIGHT_DEFAULT = 32;

		public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.TILES_SCALE);
		public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.TILES_SCALE);

		public static final int CRABBY_DRAW_OFFSET_X = (int) (26 * Game.TILES_SCALE);
		public static final int CRABBY_DRAW_OFFSET_Y = (int) (9 * Game.TILES_SCALE);

		public static int GetSpriteAmmount(int enemyType, int enemyState) {
			switch (enemyType) {
			case CRABBY:
				switch (enemyState) {
				case IDLE:
					return 9;
				case RUNNING:
					return 6;
				case ATTACK:
					return 7;
				case HIT:
					return 4;
				case DEAD:
					return 5;
				}

			}

			return 0;
		}

		public static int GetMaxHealth(int enemyType) {
			switch (enemyType) {
			case CRABBY:
				return 10;

			default:
				return 1;
			}
		}

		public static int GetEnemyDmg(int enemyType) {
			switch (enemyType) {
			case CRABBY:
				return 5;

			default:
				return 0;
			}
		}

	}

	public static class Enviroment {
		public static final int BIG_CLOUD_WIDTH_DEFAULT = 449;
		public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
		public static final int SMALL_CLOUD_WIDTH_DEFAULT = 80;
		public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 28;

		public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.TILES_SCALE);
		public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.TILES_SCALE);

		public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.TILES_SCALE);
		public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.TILES_SCALE);
	}

	public static class UI {
		public static class Buttons {
			public static final int B_DEFAULT_WIDTH = 140;
			public static final int B_DEFAULT_HEIGHT = 56;
			public static final int B_WIDTH = (int) (B_DEFAULT_WIDTH * Game.TILES_SCALE);
			public static final int B_HEIGHT = (int) (B_DEFAULT_HEIGHT * Game.TILES_SCALE);
		}

		public static class PauseButtons {
			public static final int SOUND_DEFAULT_SIZE = 42;
			public static final int SOUND_SIZE = (int) (SOUND_DEFAULT_SIZE * Game.TILES_SCALE);
		}

		public static class UrmButtons {
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.TILES_SCALE);
		}

		public static class VolumeButtons {
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;

			public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.TILES_SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.TILES_SCALE);
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.TILES_SCALE);
		}

	}

	public static class Direction {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class PlayerConstants {
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int GROUND = 4;
		public static final int HIT = 5;
		public static final int ATTACK_1 = 6;
		public static final int ATTACK_JUMP_1 = 7;
		public static final int ATTACK_JUMP_2 = 8;
		public static final int DIED = 9;

		public static int GetSpriteAmmount(int player_action) {

			switch (player_action) {
			case RUNNING:
				return 6;
			case IDLE:
				return 5;
			case HIT:
				return 4;
			case JUMP:
			case ATTACK_1:
			case ATTACK_JUMP_1:
			case ATTACK_JUMP_2:
				return 3;
			case GROUND:
				return 2;
			case FALLING:
				return 1;
			case DIED:
				return 8;
			default:
				return 1;

			}
		}

	}
}
