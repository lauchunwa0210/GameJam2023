package game.sample.logic;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameFrame extends BaseFrame {

	public GameFrame(String title) {
		super(title);
		playBackgroundMusic();  // Start the background music
	}

	@Override
	public void loadBackgroundImage() {
		try {
			backgroundImage = ImageIO.read(new File("Game/resource/image/pipe-background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRendering(Graphics2D g2d, GameState state) {
		super.doRendering(g2d, state);
		g2d.drawImage(state.getGirl().getCurrentImage(), state.getGirl().getX(), state.getGirl().getY(), null);
		// ... Specific GameFrame rendering logic (if any) ...
	}

	public void transitionToBossFrame() {
		this.dispose();
		BossFrame bossFrame = new BossFrame("Boss Level");
		bossFrame.setVisible(true);
		bossFrame.initBufferStrategy();
		bossFrame.loadBackgroundImage();
	}

	public void playBackgroundMusic() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Game/resource/music/Black Ritual .wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}

