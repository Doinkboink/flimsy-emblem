import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio {
	//use as thus: Main.game.audio.playSound(Sounds.MENU);
	private static boolean MUSIC;
	public boolean onMusic() {return MUSIC;}
	private static boolean SOUND;
	public boolean onSound() {return SOUND;}
	
	private static Clip musicClip;
	
	public enum Musics{
		TEMP1("Enjoy your options.wav"),
		TEMP2("RabRobRubert.wav");
		
		private Clip clip;
		private Musics(String s) {
			try {
				AudioInputStream mysound = AudioSystem.getAudioInputStream(new File("wav/music/" + s));
				clip = AudioSystem.getClip();
				clip.open(mysound);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void play() {
			musicClip.stop();
			musicClip = this.clip;
			musicClip.setFramePosition(0);
			if (MUSIC) {
				musicClip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
		
		/**
		 * initializes all the enums at once
		 */
		public void init() {
			values();
		}
	}
	public enum Sounds{
		MENU("menu.wav");
		
		private Clip clip;
		private Sounds(String s) {
			try {
				AudioInputStream mysound = AudioSystem.getAudioInputStream(new File("wav/sound/" + s));
				clip = AudioSystem.getClip();
				clip.open(mysound);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void play() {
			if (SOUND) {//TODO: Figure out how to get sound to spam properly?
				clip.stop();
				clip.setFramePosition(0);
				clip.start();
			}
		}
		
		/**
		 * initializes all the enums at once
		 */
		public void init() {
			values();
		}
	}

	public Audio (boolean m, boolean s) {
		MUSIC = m;
		SOUND = s;
		try {
			musicClip = AudioSystem.getClip();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playMusic(Musics m) {
		m.play();
	}
	public void playSound(Sounds s) {
		s.play();
	}
	
	public void toggleMusic() {
		MUSIC = !MUSIC;
		if (!MUSIC && musicClip.isRunning())musicClip.stop();
		else {
			musicClip.setFramePosition(0);
			musicClip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	public void toggleSound() {
		SOUND = !SOUND;
	}
}