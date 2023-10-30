package game.sample.logic;

import game.sample.entity.Bullet;
import game.sample.entity.Heart;

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

    private int backgroundX1=0, backgroundX2; // x-coordinates for the two background images
    private BufferedImage backgroundImage1, backgroundImage2, damageImage, healthImage;

    public PipeFrame(String title) {
        super(title);
        setResizable(false);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        playBackgroundMusic();
        initializeBackground();
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
    public void initializeBackground() {
        try {
            backgroundImage1 = ImageIO.read(new File("Game/resource/image/pipe-background.png"));
            backgroundImage2 = ImageIO.read(new File("Game/resource/image/pipe-background-inverse.png")); // 加载相同的图片
            backgroundX1 = 0;
            backgroundX2 = GAME_WIDTH;
            damageImage = ImageIO.read(new File("Game/resource/image/damage.png"));
            healthImage = ImageIO.read(new File("Game/resource/image/health.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    /**
     * Rendering all game elements based on the game state.
     */
    private void doRendering(Graphics2D g2d, GameState state) {

        g2d.drawImage(backgroundImage1, backgroundX1, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g2d.drawImage(backgroundImage2, backgroundX2, 0, GAME_WIDTH, GAME_HEIGHT, null);

        // Update the x-coordinates for the background images
        backgroundX1 -= 10; // Move the first image to the left
        backgroundX2 -= 10; // Move the second image to the left
        //System.out.println("X1 " + backgroundX2);
        //System.out.println("X2 " + backgroundX2);
        // 当第一个背景图像完全离开屏幕时，将其移动到第二个背景图像的右侧
        if (backgroundX1 <= -GAME_WIDTH) {
            backgroundX1 = backgroundX2 + GAME_WIDTH;
        }

        // 当第二个背景图像完全离开屏幕时，将其移动到第一个背景图像的右侧
        if (backgroundX2 <= -GAME_WIDTH) {
            backgroundX2 = backgroundX1 + GAME_WIDTH;
        }
        // load  girl
        g2d.drawImage(state.getGirl().getCurrentImage(), state.getGirl().getX(), state.getGirl().getY(), null);
        state.getGirl().setSwim(false);
        // load slimes

        for (int i = 0; i < state.getSlimes().size(); i++) {
            g2d.drawImage(state.getSlimes().get(i).getSlimeImage(), state.getSlimes().get(i).getX(),state.getSlimes().get(i).getY(),null);
            state.getSlimes().get(i).render(g2d);
        }
        //load bullets
        for (Bullet bullet : state.getBullets()) {
            bullet.render(g2d);
        }
        for (Heart heart : state.getHearts()) {
            heart.render(g2d);
        }
        if(state.getEffect()!= null){
            state.getEffect().render(g2d);
        }
        // Draw GAME OVER
        g2d.drawImage(healthImage, 70,650,60,30,null);
        g2d.drawImage(damageImage, 150,650,60,30,null);
        state.getGirl().drawDamgeInfo(g2d);
        state.getGirl().drawBar(g2d);
//        String str = "GAME OVER";
//        g2d.setColor(Color.WHITE);
//        g2d.setFont(new Font("Arial", Font.BOLD, 64));
//        int strWidth = g2d.getFontMetrics().stringWidth(str);
//        System.out.println("strWidth: "+strWidth);
//        System.out.println("StartX: "+(GAME_WIDTH- strWidth) / 2);
//        g2d.drawString(str, (GAME_WIDTH- strWidth) / 2 , GAME_HEIGHT / 2 - 120);
//        initializeButtons();


    }
    private void gameOverDraw(Graphics2D g2d) {
        String str = "GAME OVER";
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 64));
        int strWidth = g2d.getFontMetrics().stringWidth(str);
        System.out.println("strWidth: "+strWidth);
        System.out.println("StartX: "+(GAME_WIDTH- strWidth) / 2);
        g2d.drawString(str, (GAME_WIDTH- strWidth) / 2 , GAME_HEIGHT / 2 - 120);
        initializeButtons();
    }
    private void initializeButtons() {
        JButton restartButton = new JButton("Restart");
        restartButton.setBounds(GAME_WIDTH/2, GAME_HEIGHT/2, 100, 50); // 设置位置和大小
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里添加重启游戏的代码
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(GAME_WIDTH/2, GAME_HEIGHT/2, 100, 50); // 设置位置和大小
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // 退出游戏
            }
        });
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
        JLabel titleLabel = new JLabel();
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
        Dimension buttonSize = new Dimension(500, 80); // Set the width and height of the buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 20); // Set the font size of the buttons

        // "Play" button
        JButton playButton = new JButton();
        playButton.setPreferredSize(buttonSize);
        playButton.setFont(buttonFont);
        playButton.setContentAreaFilled(false);
        playButton.setOpaque(false);
        playButton.setBorderPainted(false);
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



        JButton exitButton = new JButton();
        exitButton.setPreferredSize(buttonSize);
        exitButton.setFont(buttonFont);
        exitButton.setContentAreaFilled(false);
        exitButton.setOpaque(false);
        exitButton.setBorderPainted(false);
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
