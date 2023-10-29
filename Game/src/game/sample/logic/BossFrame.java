package game.sample.logic;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BossFrame extends BaseFrame {

	public BossFrame(String title) {
		super(title);
		playBackgroundMusic();  // Start the background music
	}

	@Override
	public void loadBackgroundImage() {
		try {
			backgroundImage = ImageIO.read(new File("Game/resource/image/sea-boss-bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRendering(Graphics2D g2d, GameState state) {
		super.doRendering(g2d, state);
		// ... Specific BossFrame rendering logic (if any) ...
	}

	public void playBackgroundMusic() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Game/resource/music/Waltz for the Dead .wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
