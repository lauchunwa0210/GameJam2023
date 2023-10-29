package game.sample.logic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BossFrame extends BaseFrame {

	public BossFrame(String title) {
		super(title);
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
		// Specific BossFrame rendering logic...
	}
}
