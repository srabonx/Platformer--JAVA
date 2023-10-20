package Enviroment;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.LoadSave;

public class BigClouds {
	
	private BufferedImage img;

	private int x, y, width, height;
	private float moveX;
	private int xLvlOffset;

	public BigClouds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		loadImg();

	}

	private void loadImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);

	}

	public void update(float x) {
		moveX += x;
	}

	public void draw(Graphics g, int xLvlOffset) {
		this.xLvlOffset = xLvlOffset;
		g.drawImage(img, (int)(x + moveX), y, width, height, null);
	}

	public int getX() {
		return (int) (x + moveX);
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
		moveX = 0;
	}
	
}
