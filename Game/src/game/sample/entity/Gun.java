package game.sample.entity;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Gun {
    private int bulletSpeed = 15; // Set desired bullet speed
    private int bulletDirection = 0; // Set desired bullet direction (0 for right)
    private int fireInterval = 1000; // in milliseconds
    private boolean canFire = true;
    private int damage = 10;
    private BulletType bullet = BulletType.SINGLE;
    private Image bulletImage = null; // Set bullet image or keep it as null to use default yellow rectangle
    private Timer timer = new Timer();
    private Clip shootSound;

    public Gun(){
        try {
            shootSound = AudioSystem.getClip();
            shootSound.open(AudioSystem.getAudioInputStream(new File("Game/resource/sound_effect/shoot.wav")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(int bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public BulletType getBullet() {
        return bullet;
    }

    public void setBullet(BulletType bullet) {
        this.bullet = bullet;
    }

    public void updateBullet(BulletType b){
        if (b == BulletType.SINGLE){
            setBullet(BulletType.DOUBLE);
        } else if (b == BulletType.DOUBLE){
            setBullet(BulletType.TRIPLE);
        } else {
            setBullet(BulletType.TRIPLE);
        }
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
        if (shootSound != null) {
            if (shootSound.isRunning()) {
                shootSound.stop();   // Stop the player if it is still running
            }
            shootSound.setFramePosition(0); // rewind to the beginning
            shootSound.start();     // Start playing
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
