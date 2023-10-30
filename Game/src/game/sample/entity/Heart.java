package game.sample.entity;

import game.sample.logic.PipeFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Heart {

    private Image heartImage;
    private int x; // x position
    private int y; // y position will be randomized
    private final int imgWidth = 50;  // Adjusted for the size of heart pickup
    private final int imgHeight = 50; // Adjusted for the size of heart pickup
    private final int speed = 10;

    public Heart() {
        try {
            heartImage = ImageIO.read(new File("Game/resource/image/pixel-heart.png"));
            // resize
            heartImage = heartImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.x = 400 + new Random().nextInt(PipeFrame.GAME_WIDTH-400);
        this.y = 200 + new Random().nextInt(200); // Randomize y position between 300 and 500
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return imgWidth;
    }

    public int getHeight() {
        return imgHeight;
    }

    public Image getHeartImage() {
        return heartImage;
    }

    public void render(Graphics2D g2d) {
        g2d.drawImage(heartImage, x, y, null);
    }

    public void move() {
        this.x -= speed;  // Move the heart leftward by the speed
    }
}
