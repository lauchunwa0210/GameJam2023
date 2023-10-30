package game.sample.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Slime {

    private Image slimeImage;
    private int health;
    private int x; // position
    private final int y = 465;
    private int speed;
    private final int imgWidth = 130;
    private final int imgHeight = 50;


    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Image getSlimeImage() {
        return slimeImage;
    }

    public Slime(int StartX, int speed) {
        try {
            slimeImage = ImageIO.read(new File("Game/resource/image/slime.png"));
            // resize
            slimeImage = slimeImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.x = StartX;
        this.speed = speed;
        this.health = 30;
    }

    public void move() {
        this.x -= speed;
    }

    public void attack(Girl girl) {
        if (this.x >= girl.getX() - 130 && this.x <= girl.getX() + 130 && this.getY()-girl.getY()<= 130) {
            girl.setHealth(girl.getHealth() - 2);
        }
    }

    public void render(Graphics2D g2d) {

        // Draw the health bar
        g2d.setColor(Color.RED);
        int healthBarWidth = (int) (((double)this.health / 30) * 100);
        g2d.fillRect(x+15, y - 20, healthBarWidth, 10);

        // Draw the health bar border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x+15, y - 20, 100, 10);

    }

}
