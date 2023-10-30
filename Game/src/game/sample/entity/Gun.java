package game.sample.entity;

import java.awt.Image;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

public class Gun {
    int bulletSpeed = 10; // Set desired bullet speed
    int bulletDirection = 0; // Set desired bullet direction (0 for right)
    int fireInterval = 1000; // in milliseconds
    boolean canFire = true;
    Image bulletImage = null; // Set bullet image or keep it as null to use default yellow rectangle
    Timer timer = new Timer();

    public Gun(){
    }

    public Bullet fire(Point position){
        if (!canFire){
            return null;
        }
        canFire = false;
        Bullet newBullet = new Bullet(position, bulletSpeed, bulletDirection, bulletImage);
        startFireCountdown();
        return newBullet;
    }


    private void startFireCountdown() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                canFire = true;
            }
        }, fireInterval);
    }
}
