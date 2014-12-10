package main;

import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundThread extends Thread{
	Clip clip;
	public SoundThread(String url){
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(Main.class.getClassLoader().getResource(url)));
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	private SoundThread(Clip clip){
		this.clip = clip;
	}
	@Override
	public void run(){
		try{
			clip.setFramePosition(0);
			clip.start();
		} catch (NullPointerException e){
			
		}
	}
	public void playClip(){
		clone().start();
	}
	@Override
	public SoundThread clone(){
		return new SoundThread(clip);
	}
}