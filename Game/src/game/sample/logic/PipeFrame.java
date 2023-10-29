package game.sample.logic;

import game.sample.entity.Bullet;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PipeFrame extends JFrame {
    public static final int GAME_HEIGHT = 720;
    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;

    protected long lastRender;
    protected ArrayList<Float> fpsHistory;

    protected BufferStrategy bufferStrategy;
    protected BufferedImage backgroundImage;
    private StartMenuMusicPlayer musicPlayer = new StartMenuMusicPlayer();
    private GameStart gameStarter = new GameStart();
    private BossFrame bossFrame;
    private GameLoop game;

    public PipeFrame(String title) {
        super(title);
        setResizable(false);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        playBackgroundMusic();

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyChar() == 'j' || e.getKeyChar() == 'J')) {
                    try {
                        transitionToBossFrame();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        this.setFocusable(true);
        this.requestFocusInWindow();

    }
    private void transitionToBossFrame() throws InterruptedException {
        synchronized (this) {
            this.setVisible(false);
            if (this.bufferStrategy != null) {
                this.bufferStrategy.dispose();
            }
            game.isPipe = false;
        }
        bossFrame = new BossFrame("Final Fight");

        synchronized (bossFrame) {
            bossFrame.setVisible(true);
            if (bossFrame.bufferStrategy == null) {
                bossFrame.initBufferStrategy();
            }
        }
    }

    public void initBufferStrategy() {
        // Triple-buffering
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();
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

    public void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("Game/resource/image/pipe-background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rendering all game elements based on the game state.
     */
    private void doRendering(Graphics2D g2d, GameState state) {
        loadBackgroundImage();

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        } else {
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }
        // load  girl
        g2d.drawImage(state.getGirl().getCurrentImage(), state.getGirl().getX(), state.getGirl().getY(), null);
        for (Bullet bullet : state.getBullets()) {
            bullet.render(g2d);
        }
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
        this.getContentPane().removeAll();
        this.setResizable(false);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.initBufferStrategy();
        this.setFocusable(true);
        this.requestFocusInWindow();

        // Create and execute the game-loop
        game = new GameLoop(this);
        game.init();
        ThreadPool.execute(game);
    }
}
