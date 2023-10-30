package game.sample.entity;

import game.sample.logic.BossFrame;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Missile {
    private int x, y;
    private int width, height;
    private int speedX, speedY;

    private BufferedImage image1;
    private BufferedImage image2;
    private BufferedImage currentImage;

    public Missile(int x, int y, int width, int height, int speedX, int speedY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speedX = speedX;
        this.speedY = speedY;
        loadImages();
        currentImage = (Math.random() < 0.5) ? image1 : image2; // Choose an image randomly
    }

    private void loadImages() {
        try {
            image1 = ImageIO.read(new File("Game/resource/image/missile1.png"));
            image2 = ImageIO.read(new File("Game/resource/image/missile2.png"));
            System.out.println("Images loaded successfully");

        } catch (IOException e) {
            System.out.println("Error loading images: " + e.getMessage());

            e.printStackTrace();
            image1 = null;
            image2 = null;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void update() {
        x += speedX;
        y += speedY;
    }

//    public void render(Graphics2D g2d) {
//        g2d.setColor(Color.RED);
//        g2d.fillRect(x, y, width, height);
//    }

    public void render(Graphics2D g2d) {
        if (currentImage != null) {
            System.out.println("Drawing image");
            g2d.drawImage(currentImage, x, y, width, height, null);
        } else {
            System.out.println("Drawing red rectangle");
            g2d.setColor(Color.RED);
            g2d.fillRect(x, y, width, height);
        }
    }

    public boolean isOffScreen() {
        return y > BossFrame.GAME_HEIGHT;
    }
}
