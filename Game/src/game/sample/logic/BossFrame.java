package game.sample.logic;

import game.sample.entity.Boss;
import game.sample.entity.Bullet;
import game.sample.entity.BulletType;
import game.sample.entity.Girl;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BossFrame extends JFrame {
    public static final int GAME_HEIGHT = 720;
    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;

    protected long lastRender;
    protected ArrayList<Float> fpsHistory;

    protected BufferStrategy bufferStrategy;
    protected BufferedImage backgroundImage,healthImage,damageImage, finalBackground;
    private StartMenuMusicPlayer musicPlayer = new StartMenuMusicPlayer();
    private GameStart gameStarter = new GameStart();

    public BossFrame(String title) {
        super(title);
        setResizable(false);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        playBackgroundMusic();  // Start the background music
        initialImage();

    }

    public void initialImage(){
        try {
            damageImage = ImageIO.read(new File("Game/resource/image/damage.png"));
            healthImage = ImageIO.read(new File("Game/resource/image/health.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initBufferStrategy() throws InterruptedException {
        if (!this.isVisible()) {
            this.setVisible(true);
        }
        // Triple-buffering
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();
        // Create and execute the game-loop
        GameLoop game = new GameLoop(this);
        game.init();
        ThreadPool.execute(game);
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
    public void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("Game/resource/image/sea-boss-bg.png"));
            finalBackground = ImageIO.read(new File("Game/resource/image/final-sea-bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(GameState state) throws InterruptedException {
        if (bufferStrategy == null) {
            initBufferStrategy();
            if (bufferStrategy == null) {
                return; // if bufferStrategy is still null, exit the method
            }
        }
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

    private void doRendering(Graphics2D g2d, GameState state) {
        loadBackgroundImage();

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        } else {
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }

        Girl girl = state.getGirl();
        girl.setSwim(true);
        girl.getGun().setFireInterval(100);
        girl.getGun().setDamage(50);
        girl.getGun().setBullet(BulletType.DOUBLE);
        girl.getGun().setBulletSpeed(30);
        g2d.drawImage(girl.getCurrentImage(), girl.getX(),girl.getY(), null);

        for (Bullet bullet : state.getBullets()) {
            bullet.render(g2d);
        }
        // load boss
        Boss boss = state.getBoss();
        if (boss.isAlive()) {
            boss.render(g2d);
            boss.update();
            boss.attack();
        }
        g2d.drawImage(healthImage, 70,650,60,30,null);
        g2d.drawImage(damageImage, 150,650,60,30,null);
        state.getGirl().drawDamgeInfo(g2d);
        state.getGirl().drawBar(g2d);
        // Draw GAME OVER
        if (state.gameOver) {
            String str = "GAME OVER";
            g2d.setColor(Color.WHITE);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
            int strWidth = g2d.getFontMetrics().stringWidth(str);
            g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, GAME_HEIGHT / 2);
        }else if (!boss.isAlive()){
            g2d.drawImage(finalBackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        }
    }


}
