package game.sample.entity;

import game.sample.logic.BossFrame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Missile {
    private int x, y;
    private int width, height;
    private int speedX, speedY;

    public Missile(int x, int y, int width, int height, int speedX, int speedY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speedX = speedX;
        this.speedY = speedY;

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

    public void render(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillRect(x, y, width, height);
    }

    public boolean isOffScreen() {
        return y > BossFrame.GAME_HEIGHT;
    }
}
