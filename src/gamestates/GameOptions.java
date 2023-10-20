package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import utils.LoadSave;
import static utils.Constants.UI.UrmButtons.*;

public class GameOptions extends State implements Statemethods {

	private AudioOptions audioOptions;
	private BufferedImage backgroundImage, optionsBgImage;
	private int bgX, bgY, bgW, bgH;
	private UrmButton menuButton;

	public GameOptions(Game game) {
		super(game);
		loadImgs();
		loadButton();
		audioOptions = game.getAudioOptions();
	}

	private void loadButton() {
		int menuX = (int) (387 * Game.TILES_SCALE);
		int menuY = (int) (325 * Game.TILES_SCALE);
		menuButton = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
	}

	private void loadImgs() {
		backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_SUPER_BACKGROUND);
		optionsBgImage = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);

		bgW = (int) (optionsBgImage.getWidth() * Game.TILES_SCALE);
		bgH = (int) (optionsBgImage.getHeight() * Game.TILES_SCALE);
		bgX = (int) (Game.GAME_WIDTH / 2 - bgW / 2);
		bgY = (int) (33 * Game.TILES_SCALE);

	}

	@Override
	public void update() {
		menuButton.update();
		audioOptions.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(optionsBgImage, bgX, bgY, bgW, bgH, null);

		menuButton.draw(g);
		audioOptions.draw(g);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuButton))
			menuButton.setMousePressed(true);
		else
			audioOptions.mousePressed(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuButton)) {
			if (menuButton.isMousePressed())
				Gamestate.state = Gamestate.MENU;
		} else
			audioOptions.mouseReleased(e);

		menuButton.resetBools();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuButton.setMouseOver(false);

		if (isIn(e, menuButton))
			menuButton.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Gamestate.state = Gamestate.MENU;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}

}
