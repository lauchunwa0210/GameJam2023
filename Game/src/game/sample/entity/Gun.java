package game.sample.entity;

import java.awt.Image;
import java.awt.Point;

public class Gun {
    public Bullet fire(Point position, int speed, int direction, Image bulletImage) {
        return new Bullet(position, speed, direction, bulletImage);
    }
}