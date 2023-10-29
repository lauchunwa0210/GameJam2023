package game.sample.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

enum AttackPattern{
    STANDARD,
    SPREAD,
    DOUBLE,
}

public class Gun {
    private final Image gun;
    private int damage = 10;
    private int speed = 5;

    private int gunX;
    private int gunY;

    {
        try {
            gun = ImageIO.read(new File("Game/resource/image/gun.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int x, y;

    public Gun(){

    }

    public Gun(int x, int y) {
        this.gunX = x;
        this.gunY = y;
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void fire(){

    }
}
