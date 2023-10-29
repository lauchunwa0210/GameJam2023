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
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class GameFrame extends BaseFrame {
  
  StartMenuMusicPlayer musicPlayer = new StartMenuMusicPlayer();
	GameStart gameStarter = new GameStart();
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
		for (int i = 0; i < state.getSlimes().size(); i++) {
			g2d.drawImage(state.getSlimes().get(i).getSlimeImage(), state.getSlimes().get(i).getX(),state.getSlimes().get(i).getY(),null);
			state.getSlimes().get(i).render(g2d);
		}

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
  public void showMenu() {
		ImageIcon backgroundImageIcon = new ImageIcon("Game/resource/image/cover.png");
		Image backgroundImage = backgroundImageIcon.getImage();
		// Create a panel for the menu
		JPanel menuPanel = new JPanel(new GridBagLayout()){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Draw the background image
				g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		GridBagConstraints c = new GridBagConstraints();

		// Title label
		JLabel titleLabel = new JLabel("Toxic Tide");
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
		c.weighty = 0.2; // Larger weight to consume more vertical space
		c.gridy = 3;   // Position at the bottom
		menuPanel.add(new JPanel(), c); // Adding an empty, invisible panel

		// Add the menu panel to the frame
		this.setContentPane(menuPanel);

		// Set the size of the window and other properties
		this.pack();
		this.setLocationRelativeTo(null); // Center on screen
		this.setVisible(true);
		musicPlayer.playMusic("Game/resource/music/MenuMusic.wav");
	}
	private void startGame() {
		musicPlayer.stopMusic();
		// Remove the menu panel from the frame
		gameStarter.gameStart(GAME_WIDTH, GAME_HEIGHT, this);
	}
}

