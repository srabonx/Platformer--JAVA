package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Direction.*;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;


public class GamePanel extends JPanel{
	
	//size of window
	private int windX = GAME_WIDTH , windY = GAME_HEIGHT;

	private MouseInputs mouseInputs;
	
	private Dimension size;
	
	private Game game;
	
	public GamePanel(Game game) {
		
		this.game = game;
		
		mouseInputs = new MouseInputs(this);
		
		setPanelSize(windX, windY);
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		
	}
	

	private void setPanelSize(int x, int y) {
		 size = new Dimension(x, y);
		 setMinimumSize(size);
		 setMaximumSize(size);
		 setPreferredSize(size);
		
	}
	

	
	public void updateGame() {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}


	
	public Game getGame() {
		return game;
	}

	

	
	
}
