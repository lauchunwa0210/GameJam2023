package game.sample.logic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameFrame extends BaseFrame {

	public GameFrame(String title) {
		super(title);
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
		// Specific GameFrame rendering logic...
	}

	public void transitionToBossFrame() {
		this.dispose();
		BossFrame bossFrame = new BossFrame("Boss Level");
		bossFrame.setVisible(true);
		bossFrame.initBufferStrategy();
		bossFrame.loadBackgroundImage();
	}
}
