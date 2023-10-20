package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import utils.LoadSave;

public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 60;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;
	private GameOptions option;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;

	public static final int TILES_DEFAULT_SIZE = 32;
	public static final float TILES_SCALE = 1.5f;
	public static final int TILES_IN_WIDTH = 26;
	public static final int TILES_IN_HEIGHT = 14;
	public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * TILES_SCALE);
	public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	public Game() {
		initClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();

		startGameLoop();
	}

	private void initClasses() {

		audioOptions = new AudioOptions(this);
		menu = new Menu(this);
		playing = new Playing(this);
		option = new GameOptions(this);
		audioPlayer = new AudioPlayer();

	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {

		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
			option.update();
			break;
		case QUIT:
		default:
			System.exit(0);
			break;

		}

	}

	public void render(Graphics g) {

		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case OPTIONS:
			option.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		default:
			break;

		}

	}

	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {

			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;

			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + "| UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}

	public void windowLostFocus() {

		if (Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();

	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}

	public GameOptions getGameOptions() {
		return option;
	}

	public AudioOptions getAudioOptions() {
		return audioOptions;
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

}
