package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utils.HelpMethods;
import utils.LoadSave;
import static utils.HelpMethods.GetLevelData;
import static utils.HelpMethods.GetCrabs;
import static utils.HelpMethods.GetPlayerSpawn;

public class Level {

	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Crabby> crabs;
	private ArrayList<Potion> potions;
	private ArrayList<Spike> spikes;
	private ArrayList<Cannon> cannons;
	private ArrayList<GameContainer> containers;

	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;

	public Level(BufferedImage img) {
		this.img = img;

		createLvlData();
		createEnemies();
		createPotions();
		createSpikes();
		createCannons();
		createContainers();
		calcLvlOffset();
		calcPlayerSpawn();
	}

	private void createCannons() {
		cannons = HelpMethods.GetCannons(img);

	}

	private void createSpikes() {
		spikes = HelpMethods.GetSpikes(img);

	}

	private void createContainers() {
		containers = HelpMethods.GetContainers(img);

	}

	private void createPotions() {
		potions = HelpMethods.GetPotions(img);
	}

	private void calcPlayerSpawn() {
		playerSpawn = GetPlayerSpawn(img);
	}

	private void calcLvlOffset() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;

	}

	private void createEnemies() {
		crabs = GetCrabs(img);

	}

	private void createLvlData() {
		lvlData = GetLevelData(img);

	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLevelData() {
		return lvlData;
	}

	public int getLvlOffset() {
		return maxLvlOffsetX;
	}

	public ArrayList<Crabby> getCrabs() {
		return crabs;
	}

	public Point getPlayerSpawn() {
		return playerSpawn;
	}

	public ArrayList<Potion> getPotions() {
		return potions;
	}

	public ArrayList<GameContainer> getContainers() {
		return containers;
	}

	public ArrayList<Spike> getSpikes() {
		return spikes;
	}

	public ArrayList<Cannon> getCannons() {
		return cannons;
	}
}
