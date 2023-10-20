package ui;

import static utils.Constants.UI.UrmButtons.URM_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.ImageGraphicAttribute;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

public class GameOverOverlay {

	private Playing playing;
	private BufferedImage img;
	private int imgX, imgY, imgW, imgH;
	private UrmButton menuButton, playButton;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
		createImage();
		createButtons();
	}

	private void createButtons() {
		int menuX = (int) (335 * Game.TILES_SCALE);
		int playX = (int) (440 * Game.TILES_SCALE);
		int y = (int) (195 * Game.TILES_SCALE);

		menuButton = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
		playButton = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);

	}

	private void createImage() {
		img = LoadSave.GetSpriteAtlas(LoadSave.GAME_OVER);

		imgW = (int) (img.getWidth() * Game.TILES_SCALE);
		imgH = (int) (img.getHeight() * Game.TILES_SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.TILES_SCALE);

	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(img, imgX, imgY, imgW, imgH, null);

		menuButton.draw(g);
		playButton.draw(g);

	}

	public void update() {
		menuButton.update();
		playButton.update();
	}

	public void keyPressed(KeyEvent e) {

	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		playButton.setMouseOver(false);
		menuButton.setMouseOver(false);

		if (isIn(menuButton, e))
			menuButton.setMouseOver(true);
		else if (isIn(playButton, e))
			playButton.setMouseOver(true);
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menuButton, e))
			menuButton.setMousePressed(true);
		else if (isIn(playButton, e))
			playButton.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menuButton, e)) {
			if (menuButton.isMousePressed()) {
				playing.resetAll();
				playing.setGamestate(Gamestate.MENU);
			}

		} else if (isIn(playButton, e))
			if (playButton.isMousePressed()) {
				playing.resetAll();
				playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
			}
		menuButton.resetBools();
		playButton.resetBools();
	}

}
