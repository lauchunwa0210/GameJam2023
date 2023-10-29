package game.sample.entity;

import game.sample.logic.PipeFrame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Image;

public class Bullet {
    private final Point position;
    private final int speed;
    private final int direction; // Assuming direction as an angle, 0 for right, 180 for left
    private final Image bulletImage;

    public Bullet(Point startPosition, int speed, int direction, Image bulletImage) {
        this.position = startPosition;
        this.speed = speed;
        this.direction = direction;
        this.bulletImage = bulletImage;
    }

    public void move() {
        position.translate(
                (int) (speed * Math.cos(Math.toRadians(direction))),
                0   // Since bullets move horizontally in this setup
        );
    }

    public void render(Graphics2D g2d) {
        if (bulletImage != null) {
            g2d.drawImage(bulletImage, position.x, position.y, null);
        } else {
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(position.x, position.y, 10, 5);  // Default bullet size if no image provided
        }
    }

    public boolean isOffScreen() {
        return position.x < 0 || position.x > PipeFrame.GAME_WIDTH;
    }

}