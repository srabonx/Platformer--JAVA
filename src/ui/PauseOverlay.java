package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;
import static utils.Constants.UI.PauseButtons.*;
import static utils.Constants.UI.UrmButtons.*;
import static utils.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

	private Playing playing;

	private BufferedImage backgroundImg;
	private int bgX, bgY, bgH, bgW;

	private UrmButton menuB, replayB, unpauseB;
	private AudioOptions audioOptions;

	public PauseOverlay(Playing playing) {

		this.playing = playing;
		audioOptions = playing.getGame().getAudioOptions();
		loadBackground();

		createUrmButtons();

	}

	private void createUrmButtons() {
		int menuX = (int) (313 * Game.TILES_SCALE);
		int replayX = (int) (387 * Game.TILES_SCALE);
		int unpauseX = (int) (462 * Game.TILES_SCALE);
		int bY = (int) (325 * Game.TILES_SCALE);

		menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
		replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
		unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);

	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgW = (int) (backgroundImg.getWidth() * Game.TILES_SCALE);
		bgH = (int) (backgroundImg.getHeight() * Game.TILES_SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (25 * Game.TILES_SCALE);

	}

	public void update() {

		menuB.update();
		replayB.update();
		unpauseB.update();
		audioOptions.update();

	}

	public void draw(Graphics g) {
		// Background
		g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

		// Urm Button
		menuB.draw(g);
		replayB.draw(g);
		unpauseB.draw(g);
		
		audioOptions.draw(g);
	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB))
			menuB.setMousePressed(true);
		else if (isIn(e, replayB))
			replayB.setMousePressed(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMousePressed(true);
		else
			audioOptions.mousePressed(e);

	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed()) {
				playing.setGamestate(Gamestate.MENU);
				playing.resetAll();
				playing.unpauseGame();
			}
		} else if (isIn(e, replayB)) {
			if (replayB.isMousePressed()) {
				playing.resetAll();
				playing.unpauseGame();
			}

		} else if (isIn(e, unpauseB)) {
			if (unpauseB.isMousePressed())
				playing.unpauseGame();
		} else
			audioOptions.mouseReleased(e);

		menuB.resetBools();
		replayB.resetBools();
		unpauseB.resetBools();

	}

	public void mouseMoved(MouseEvent e) {

		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);

		if (isIn(e, menuB))
			menuB.setMouseOver(true);
		else if (isIn(e, replayB))
			replayB.setMouseOver(true);
		else if (isIn(e, unpauseB))
			unpauseB.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);

	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}
}
