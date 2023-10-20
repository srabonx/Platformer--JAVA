package ui;

import static utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gamestates.Gamestate;
import main.Game;

public class AudioOptions {

	private VolumeButton volumeB;
	private SoundButton musicButton, sfxButton;
	private Game game;
	
	public AudioOptions(Game game) {
		this.game = game;
		createSoundButtons();
		createVolumeButtons();
	}

	private void createVolumeButtons() {

		int vX = (int) (309 * Game.TILES_SCALE);
		int vY = (int) (278 * Game.TILES_SCALE);

		volumeB = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);

	}

	private void createSoundButtons() {

		int soundX = (int) (450 * Game.TILES_SCALE);
		int musicY = (int) (140 * Game.TILES_SCALE);
		int sfxY = (int) (186 * Game.TILES_SCALE);

		musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);

	}

	public void update() {
		musicButton.update();
		sfxButton.update();
		volumeB.update();
	}

	public void draw(Graphics g) {

		// Sound Button

		musicButton.draw(g);
		sfxButton.draw(g);

		// Volume Button
		volumeB.draw(g);
	}
	
	
	public void mouseDragged(MouseEvent e) {
		if(volumeB.isMousePressed()) {
			
			float valueBefore = volumeB.getFloatValue();
			volumeB.changeX(e.getX());
			float valueAfter = volumeB.getFloatValue();
			
			if(valueBefore != valueAfter)
				game.getAudioPlayer().setVolume(valueAfter);
			
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if(isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if(isIn(e,sfxButton))
			sfxButton.setMousePressed(true);
		else if(isIn(e,volumeB))
			volumeB.setMousePressed(true);
		
	}
	
	public void mouseReleased(MouseEvent e) {
		if(isIn(e, musicButton)) {
			if(musicButton.isMousePressed()) {
				musicButton.setMuted(!musicButton.isMuted());
				game.getAudioPlayer().toggleSongMute();
			}
		}else if(isIn(e,sfxButton)) 
			if(sfxButton.isMousePressed()) {
				sfxButton.setMuted(!sfxButton.isMuted());
				game.getAudioPlayer().toggleEffectsMute();
			}
		musicButton.resetBools();
		sfxButton.resetBools();
		volumeB.resetBools();
		
			
	}
	
	public void mouseMoved(MouseEvent e) {
		
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		
		volumeB.setMouseOver(false);
		
		if(isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if(isIn(e,sfxButton))
			sfxButton.setMouseOver(true);
		else if(isIn(e,volumeB))
			volumeB.setMouseOver(true);
		
	}
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
			
	}
}
