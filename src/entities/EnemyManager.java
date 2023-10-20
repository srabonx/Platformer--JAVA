package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;
import static utils.Constants.EnemyConstants.*;

public class EnemyManager {

	Playing playing;

	private BufferedImage[][] crabbyArr;

	private ArrayList<Crabby> crabbies = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;

		loadEnemyImgs();
	}

	public void loadEnemies(Level lvl) {
		crabbies = lvl.getCrabs();
	}

	private void loadEnemyImgs() {
		crabbyArr = new BufferedImage[5][9];

		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);

		for (int i = 0; i < crabbyArr.length; i++)
			for (int j = 0; j < crabbyArr[i].length; j++)
				crabbyArr[i][j] = temp.getSubimage(j * CRABBY_WIDTH_DEFAULT, i * CRABBY_HEIGHT_DEFAULT,
						CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);

	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Crabby c : crabbies)
			if (c.isActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			}
		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawCrabs(g, xLvlOffset);
		

	}


	private void drawCrabs(Graphics g, int xLvlOffset) {
		for (Crabby c : crabbies)
			if (c.isActive()) {
				g.drawImage(crabbyArr[c.getState()][c.getAniIndex()],
						(int) (c.getHitbox().x - CRABBY_DRAW_OFFSET_X - xLvlOffset) + c.flipX(),
						(int) (c.getHitbox().y - CRABBY_DRAW_OFFSET_Y), CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
				// c.drawAttackBox(g, xLvlOffset);
			}
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Crabby c : crabbies)
			if (c.isActive())
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(5);
					return;
				}
	}

	public void resetAllEnemies() {
		for (Crabby c : crabbies)
			c.resetEnemy();
	}
}
