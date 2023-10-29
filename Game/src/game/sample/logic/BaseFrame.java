package game.sample.logic;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class BaseFrame extends JFrame {

    public static final int GAME_HEIGHT = 720;
    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;

    protected long lastRender;
    protected ArrayList<Float> fpsHistory;

    protected BufferStrategy bufferStrategy;
    protected BufferedImage backgroundImage;

    public BaseFrame(String title) {
        super(title);
        setResizable(false);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        lastRender = -1;
        fpsHistory = new ArrayList<>(100);
    }

    public void initBufferStrategy() {
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();
    }

    public abstract void loadBackgroundImage();

    public void render(GameState state) {
        do {
            do {
                Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
                try {
                    doRendering(graphics, state);
                } finally {
                    graphics.dispose();
                }
            } while (bufferStrategy.contentsRestored());

            bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync();
        } while (bufferStrategy.contentsLost());
    }

    protected void doRendering(Graphics2D g2d, GameState state) {
        loadBackgroundImage();
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        } else {
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }
        // Other common rendering logic...
    }
}
