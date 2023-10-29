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

    {
        try {
            gun = ImageIO.read(new File("Game/resource/image/gun.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    int damage = 10;
    int speed = 5;

    public Gun() {
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
