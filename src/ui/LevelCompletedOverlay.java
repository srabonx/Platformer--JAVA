package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import static utils.Constants.UI.UrmButtons.*;

public class LevelCompletedOverlay {

	Playing playing;
	private UrmButton menuButton, nextButton;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;

	public LevelCompletedOverlay(Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
	}

	private void initButtons() {
		int menuX = (int) (330 * Game.TILES_SCALE);
		int nextX = (int) (445 * Game.TILES_SCALE);
		int y = (int) (195 * Game.TILES_SCALE);

		menuButton = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
		nextButton = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);

	}

	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_SPRITE);

		bgW = (int) (img.getWidth() * Game.TILES_SCALE);
		bgH = (int) (img.getHeight() * Game.TILES_SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (75 * Game.TILES_SCALE);

	}

	public void draw(Graphics g) {
		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		menuButton.draw(g);
		nextButton.draw(g);
	}

	public void update() {
		menuButton.update();
		nextButton.update();
	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		nextButton.setMouseOver(false);
		menuButton.setMouseOver(false);

		if (isIn(menuButton, e))
			menuButton.setMouseOver(true);
		else if (isIn(nextButton, e))
			nextButton.setMouseOver(true);
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(menuButton, e))
			menuButton.setMousePressed(true);
		else if (isIn(nextButton, e))
			nextButton.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menuButton, e)) {
			if (menuButton.isMousePressed()) {
				playing.resetAll();
				playing.setGamestate(Gamestate.MENU);
			}

		} else if (isIn(nextButton, e)) 
			if (nextButton.isMousePressed()) {
				playing.loadNextLvl();
				playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
			}

		menuButton.resetBools();
		nextButton.resetBools();
	}

}
