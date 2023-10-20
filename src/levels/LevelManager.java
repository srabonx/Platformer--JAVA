package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;
import static main.Game.*;

public class LevelManager {

	private Game game;

	private BufferedImage[] levelSprite;
	private ArrayList<Level> levels;
	private int lvlIndex = 0;

	public LevelManager(Game game) {
		this.game = game;

		importOutsideSprite();
		levels = new ArrayList<>();
		buildAllLevels();

	}
	
	public void loadNextLvl() {
		lvlIndex ++;
		if(lvlIndex >= levels.size()) {
			lvlIndex = 0;
			System.out.println("no more levels");
			game.getPlaying().setGamestate(Gamestate.MENU);
		}
		
		Level newLevel = levels.get(lvlIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
		game.getPlaying().setMaxLvlOffser(newLevel.getLvlOffset());
		game.getPlaying().getObjectManager().loadObjects(newLevel);
	}

	private void buildAllLevels() {
		BufferedImage[] allLevels = LoadSave.GetAllLevels();

		for (BufferedImage img : allLevels)
			levels.add(new Level(img));

	}

	private void importOutsideSprite() {

		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);

		levelSprite = new BufferedImage[48];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 12; j++) {
				int index = i * 12 + j;
				levelSprite[index] = img.getSubimage(j * 32, i * 32, 32, 32);
			}

	}

	public void draw(Graphics g, int lvlOffset) {

		for (int i = 0; i < Game.TILES_IN_HEIGHT; i++)
			for (int j = 0; j < levels.get(lvlIndex).getLevelData()[0].length; j++) {
				int index = levels.get(lvlIndex).getSpriteIndex(j, i);
				g.drawImage(levelSprite[index], TILES_SIZE * j - lvlOffset, i * TILES_SIZE, TILES_SIZE, TILES_SIZE,
						null);
			}

	}

	public void update() {

	}

	public Level getLevel() {
		return levels.get(lvlIndex);
	}
	
	public int getAmountOfLvls() {
		return levels.size();
	}
	
	public int getLvlIndex() {
		return lvlIndex;
	}
	
	public void resetAll() {
		lvlIndex = 0;
	}
}
