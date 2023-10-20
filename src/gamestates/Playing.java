package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import Enviroment.BigClouds;
import Enviroment.SmallCloud;
import audio.AudioPlayer;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utils.LoadSave;
import static utils.Constants.Enviroment.*;

public class Playing extends State implements Statemethods {

	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private GameOverOverlay gameOverOverlay;
	private PauseOverlay pauseOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;

	private boolean paused = false;

	private int xLvlOffset;
	private int leftBorder = (int) (0.4 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.6 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;

	private BufferedImage backgroundImg;
	private Random rnd = new Random();

	private SmallCloud cloud;
	private BigClouds bigClouds;

	private ArrayList<SmallCloud> sm;
	private ArrayList<BigClouds> bm;

	private boolean gameOver;
	private boolean levelCompleted;
	private boolean playerDying;

	public Playing(Game game) {
		super(game);

		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);

		initCloudArray();

		calcLvlOffset();
		loadStartLvl();

	}

	public void loadNextLvl() {
		resetAll();
		levelManager.loadNextLvl();
		player.setSpawn(levelManager.getLevel().getPlayerSpawn());
	}

	private void loadStartLvl() {
		enemyManager.loadEnemies(levelManager.getLevel());
		objectManager.loadObjects(levelManager.getLevel());

	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getLevel().getLvlOffset();

	}

	private void initClasses() {

		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player = new Player(200, 200, (int) (64 * Game.TILES_SCALE), (int) (40 * Game.TILES_SCALE), this);
		player.loadLevelData(levelManager.getLevel().getLevelData());
		player.setSpawn(levelManager.getLevel().getPlayerSpawn());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);

	}

	private void initCloudArray() {

		sm = new ArrayList<>();

		for (int i = 0; i < 8; i++) {
			cloud = new SmallCloud(SMALL_CLOUD_WIDTH * 2 * i, randY() + SMALL_CLOUD_HEIGHT, SMALL_CLOUD_WIDTH,
					SMALL_CLOUD_HEIGHT);
			sm.add(cloud);
		}

		bm = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			bigClouds = new BigClouds(i * BIG_CLOUD_WIDTH, (int) (204 * Game.TILES_SCALE), BIG_CLOUD_WIDTH,
					BIG_CLOUD_HEIGHT);
			bm.add(bigClouds);
		}
	}

	@Override
	public void update() {

		if (paused)
			pauseOverlay.update();
		else if (levelCompleted)
			levelCompletedOverlay.update();
		else if(gameOver) {
			gameOverOverlay.update();
		}else if(playerDying)
			player.update();
		else {
			levelManager.update();
			objectManager.update(levelManager.getLevel().getLevelData(), player);
			player.update();
			enemyManager.update(levelManager.getLevel().getLevelData(), player);
			cloudUpdate();

			checkCloseToBorder();
		}

	}

	private void cloudUpdate() {
		for (SmallCloud al : sm) {
			al.update(0.2f);
			if (al.getX() > Game.GAME_WIDTH)
				al.setXY(0 - SMALL_CLOUD_WIDTH, randY());
		}

		for (BigClouds bc : bm) {
			bc.update(0.1f);
			if (bc.getX() > Game.GAME_WIDTH)
				bc.setXY(0 - BIG_CLOUD_WIDTH, (int) (204 * Game.TILES_SCALE));
		}

	}

	private int randY() {
		return ((int) (80 * Game.TILES_SCALE) + rnd.nextInt((int) (90 * Game.TILES_SCALE)));
	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;

	}

	@Override
	public void draw(Graphics g) {

		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		drawClouds(g);

		levelManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);

		} else if (gameOver)
			gameOverOverlay.draw(g);
		else if (levelCompleted)
			levelCompletedOverlay.draw(g);
	}

	private void drawClouds(Graphics g) {

//		for (int i = 0; i < 3; i++)
//			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH -(int)(cloudSpeed), (int) (204 * Game.TILES_SCALE),
//					BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

//		for (int i = 0; i < smallCloudPos.length; i++)
//			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 2 * i - (int) (xLvlOffset * 0.4), smallCloudPos[i],
//					SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);

		// Moving Clouds

		for (BigClouds bc : bm)
			bc.draw(g, xLvlOffset);

		for (SmallCloud al : sm) {
			al.draw(g, xLvlOffset);

		}
	}

	public void resetAll() {
		// TODO: reset player,enemy,level etc.

		gameOver = false;
		paused = false;
		levelCompleted = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkSpikeTouched(Player p) {
		objectManager.checkSpikesTouched(p);

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!gameOver)
			if (paused)
				pauseOverlay.mouseDragged(e);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mousePressed(e);
			else if (levelCompleted)
				levelCompletedOverlay.mousePressed(e);
				
		}else {
			gameOverOverlay.mousePressed(e);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);
			else if (levelCompleted)
				levelCompletedOverlay.mouseReleased(e);
				
		}else {
			gameOverOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e);
			else if (levelCompleted)
				levelCompletedOverlay.mouseMoved(e);
			
		}else {
			gameOverOverlay.mouseMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (gameOver)
			gameOverOverlay.keyPressed(e);
		else
			switch (e.getKeyCode()) {

			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;

			case KeyEvent.VK_J:
				player.setAttacking(true);
				break;
			case KeyEvent.VK_E:
				player.powerAttack();
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;

			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;

			}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver)
			switch (e.getKeyCode()) {

			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;

			}

	}

	public void setMaxLvlOffser(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.levelCompleted = levelCompleted;
		if(levelCompleted)
			game.getAudioPlayer().lvlCompleted();
	}

	public void unpauseGame() {
		paused = false;
	}

	public void windowLostFocus() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;

	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
		
	}

}
