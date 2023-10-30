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

    private BulletType bullet = BulletType.SINGLE;
    Image bulletImage = null; // Set bullet image or keep it as null to use default yellow rectangle
    Timer timer = new Timer();

    public Gun(){
    }

    public BulletType getBullet() {
        return bullet;
    }

    public void setBullet(BulletType bullet) {
        this.bullet = bullet;
    }

    public Bullet fire(Point position){
        if (!canFire){
            return null;
        }
        canFire = false;
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
