package game.sample.entity;

import java.awt.Image;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

public class Gun {
    int bulletSpeed = 15; // Set desired bullet speed
    int bulletDirection = 0; // Set desired bullet direction (0 for right)
    int fireInterval = 1000; // in milliseconds
    boolean canFire = true;
    Image bulletImage = null; // Set bullet image or keep it as null to use default yellow rectangle
    Timer timer = new Timer();

    public Gun(){
    }

    public Bullet fire(Point position, Boolean isRight, Boolean seaScene){
        if (!canFire){
            return null;
        }
        canFire = false;
        if(isRight){
            bulletDirection = 0;
        } else{
            bulletDirection = 180;
        }

        if (!seaScene && isRight){
            bulletSpeed += 10;
        }

        Bullet singleBullet = new Bullet(position, bulletSpeed, bulletDirection, bulletImage, BulletType.SINGLE);
        Bullet doubleBullet = new Bullet(position, bulletSpeed, bulletDirection, bulletImage, BulletType.DOUBLE);
        Bullet tripleBullet = new Bullet(position, bulletSpeed, bulletDirection, bulletImage, BulletType.TRIPLE);

        startFireCountdown();
        //  TODO:We need to decide which type of bullet you want to return.
        return doubleBullet;
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
