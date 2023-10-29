/*** In The Name of Allah ***/
package game.sample.logic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * The window on which the rendering is performed.
 * This example uses the modern BufferStrategy approach for double-buffering,
 * actually it performs triple-buffering!
 * For more information on BufferStrategy check out:
 *    http://docs.oracle.com/javase/tutorial/extra/fullscreen/bufferstrategy.html
 *    http://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferStrategy.html
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class GameFrame extends JFrame {

	public static final int GAME_HEIGHT = 720;                  // 720p game resolution
	public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio

	private long lastRender;
	private ArrayList<Float> fpsHistory;

	private BufferStrategy bufferStrategy;

	private BufferedImage backgroundImage;

public class GameFrame extends BaseFrame {
	public GameFrame(String title) {
		super(title);
		setResizable(false);
		setSize(GAME_WIDTH, GAME_HEIGHT);
		lastRender = -1;
		fpsHistory = new ArrayList<>(100);
	}

	/**
	 * This must be called once after the JFrame is shown:
	 *    frame.setVisible(true);
	 * and before any rendering is started.
	 */
	public void initBufferStrategy() {
		// Triple-buffering
		createBufferStrategy(3);
		bufferStrategy = getBufferStrategy();
	}

	// This method could be in your initialization or constructor
	GameStart gameStarter = new GameStart();
	@Override
	public void loadBackgroundImage() {
		try {
			backgroundImage = ImageIO.read(new File("Game/resource/image/pipe-background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Game rendering with triple-buffering using BufferStrategy.
	 */
	public void render(GameState state) {
		// Render single frame
		do {
			// The following loop ensures that the contents of the drawing buffer
			// are consistent in case the underlying surface was recreated
			do {
				// Get a new graphics context every time through the loop
				// to make sure the strategy is validated
				Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
				try {
					doRendering(graphics, state);
				} finally {
					// Dispose the graphics
					graphics.dispose();
				}
				// Repeat the rendering if the drawing buffer contents were restored
			} while (bufferStrategy.contentsRestored());

			// Display the buffer
			bufferStrategy.show();
			// Tell the system to do the drawing NOW;
			// otherwise it can take a few extra ms and will feel jerky!
			Toolkit.getDefaultToolkit().sync();

		// Repeat the rendering if the drawing buffer was lost
		} while (bufferStrategy.contentsLost());
	}

	/**
	 * Rendering all game elements based on the game state.
	 */
	private void doRendering(Graphics2D g2d, GameState state) {
		// Draw background
		loadBackgroundImage();
		if (backgroundImage != null) {
			g2d.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
		} else {
			g2d.setColor(Color.GRAY);
			g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		}

		// Draw girl
		g2d.drawImage(state.getGirl().getCurrentImage(), state.getGirl().getX(), state.getGirl().getY(), null);

		// Print FPS info
		long currentRender = System.currentTimeMillis();
		if (lastRender > 0) {
			fpsHistory.add(1000.0f / (currentRender - lastRender));
			if (fpsHistory.size() > 100) {
				fpsHistory.remove(0); // remove oldest
			}
			float avg = 0.0f;
			for (float fps : fpsHistory) {
				avg += fps;
			}
			avg /= fpsHistory.size();
			String str = String.format("Average FPS = %.1f , Last Interval = %d ms",
					avg, (currentRender - lastRender));
			g2d.setColor(Color.CYAN);
			g2d.setFont(g2d.getFont().deriveFont(18.0f));
			int strWidth = g2d.getFontMetrics().stringWidth(str);
			int strHeight = g2d.getFontMetrics().getHeight();
			g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, strHeight);
		}
		lastRender = currentRender;

		// Draw GAME OVER
		if (state.gameOver) {
			String str = "GAME OVER";
			g2d.setColor(Color.WHITE);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
			int strWidth = g2d.getFontMetrics().stringWidth(str);
			g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, GAME_HEIGHT / 2);
		}
	}
	public void showMenu() {
		// Create a panel for the menu
		JPanel menuPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Title label
		JLabel titleLabel = new JLabel("Simple Ball Game");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2; // Span across two columns for centering
		c.weighty = 0.3; // Increase weight to move title down slightly
		c.insets = new Insets(100, 10, 40, 10); // Increase top inset to move title down
		c.anchor = GridBagConstraints.NORTH; // Align to the top
		menuPanel.add(titleLabel, c);

		c.weighty = 0.06; // No extra vertical space
		c.insets = new Insets(5, 10, 5, 10); // Reduced vertical insets
		Dimension buttonSize = new Dimension(500, 50); // Set the width and height of the buttons
		Font buttonFont = new Font("Arial", Font.PLAIN, 20); // Set the font size of the buttons

		// "Play" button
		JButton playButton = new JButton("Play");
		playButton.setPreferredSize(buttonSize);
		playButton.setFont(buttonFont);
		c.gridy = 1; // Positioning the Play button

		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Start the game when "Play" is clicked
				startGame();
			}
		});

		// Add the button to the panel
		menuPanel.add(playButton, c);

		JButton achievementsButton = new JButton("Achievements");
		achievementsButton.setPreferredSize(buttonSize);
		achievementsButton.setFont(buttonFont);
		c.gridy = 2; // Adjust the gridy value to position the button
		achievementsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Define what happens when Achievements is clicked.
				// You might want to display a new window or panel with achievement information.
			}
		});
		menuPanel.add(achievementsButton, c);

		JButton exitButton = new JButton("Exit");
		exitButton.setPreferredSize(buttonSize);
		exitButton.setFont(buttonFont);
		c.gridy = 3;
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Exit the game when "Exit" is clicked
				System.exit(0);
			}
		});
		menuPanel.add(exitButton, c);


		menuPanel.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		c.weighty = 0.5; // Larger weight to consume more vertical space
		c.gridy = 3;   // Position at the bottom
		menuPanel.add(new JPanel(), c); // Adding an empty, invisible panel

		// Add the menu panel to the frame
		this.setContentPane(menuPanel);

		// Set the size of the window and other properties
		this.pack();
		this.setLocationRelativeTo(null); // Center on screen
		this.setVisible(true);
	}
	private void startGame() {
		// Remove the menu panel from the frame
		gameStarter.gameStart(GAME_WIDTH, GAME_HEIGHT, this);
	}

}
