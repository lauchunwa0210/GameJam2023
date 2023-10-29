package game.sample.entity;

import game.sample.logic.GameFrame;

import java.awt.Color;
import java.awt.Graphics2D;

public class Missile {
    private int x, y;
    private int width, height;
    private int speed;

    public Missile(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void update() {
        y += speed;
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillRect(x, y, width, height);
    }

    public boolean isOffScreen() {
        return y > GameFrame.GAME_HEIGHT;
    }
}
