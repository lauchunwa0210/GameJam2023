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
    private final BulletType bulletType;

    public Bullet(Point startPosition, int speed, int direction, Image bulletImage, BulletType bulletType) {
        this.position = startPosition;
        this.speed = speed;
        this.direction = direction;
        this.bulletImage = bulletImage;
        this.bulletType = bulletType;
    }

    public void move() {
        position.translate(
                (int) (speed * Math.cos(Math.toRadians(direction))),
                0   // Since bullets move horizontally in this setup
        );
    }

    public void render(Graphics2D g2d) {
        switch (bulletType) {
            case SINGLE:
                drawBullet(g2d, position.x, position.y);
                break;
            case DOUBLE:
                drawBullet(g2d, position.x, position.y);
                drawBullet(g2d, position.x + 25, position.y);
                break;
            case TRIPLE:
                drawBullet(g2d, position.x, position.y - 15);
                drawBullet(g2d, position.x, position.y);
                drawBullet(g2d, position.x, position.y + 15);
                break;
        }
    }

    private void drawBullet(Graphics2D g2d, int x, int y) {
        if (bulletImage != null) {
            g2d.drawImage(bulletImage, x, y, null);
        } else {
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(x, y, 10, 5);  // Default bullet size if no image provided
        }
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public int getWidth() {
        return 10; // 返回子弹的宽度，这里我们假设子弹宽度为10像素
    }

    public int getHeight() {
        return 5; // 返回子弹的高度，这里我们假设子弹高度为5像素
    }


    public boolean isOffScreen() {
        return position.x < 0 || position.x > PipeFrame.GAME_WIDTH;
    }
}
