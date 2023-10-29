//package game.sample.entity;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//enum AttackPattern {
//    STANDARD,
//    SPREAD,
//    DOUBLE,
//}
//
//public class Gun {
//    private final int imgWidth = 130;
//    private final int imgHeight = 130;
//    private Image gunImage;
//    private AttackPattern currentAttackPattern = AttackPattern.STANDARD;
//
//    {
//        try {
//            gunImage = ImageIO.read(new File("Game/resource/image/gun.png"));
//            gunImage = gunImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    int damage = 10;
//    int speed = 5;
//
////    public void setX(int x) {
////        this.x = x;
////    }
////    public void setY(int y) {
////        this.y = y;
////        this.currentImage = girlMove2;
////    }
////    public int getX() {
////        return x;
////    }
////    public int getY() {
////        return y;
////    }
//
////    public Gun(int StartX, int StartY) {
////
////    }
//
//    public List<Bullet> fire(Point startPosition) {
//        List<Bullet> bullets = new ArrayList<>();
//        Image bulletImage = null; // Load bullet image if you have one
//        switch (currentAttackPattern) {
//            case STANDARD:
//                bullets.add(new Bullet(startPosition, speed, 0, bulletImage));
//                break;
//            case SPREAD:
//                bullets.add(new Bullet(startPosition, speed, -10, bulletImage));  // Left spread
//                bullets.add(new Bullet(startPosition, speed, 0, bulletImage));    // Straight
//                bullets.add(new Bullet(startPosition, speed, 10, bulletImage));   // Right spread
//                break;
//            case DOUBLE:
//                bullets.add(new Bullet(startPosition, speed, 0, bulletImage));
//                bullets.add(new Bullet(new Point(startPosition.x + 15, startPosition.y), speed, 0, bulletImage));  // Slightly right
//                break;
//        }
//        return bullets;
//    }
//
////    public Image getCurrentImage(){
////        return this.currentImage;
////    }
//
//    public int getDamage() {
//        return damage;
//    }
//
//    public int getSpeed() {
//        return speed;
//    }
//
//    public void setDamage(int damage) {
//        this.damage = damage;
//    }
//
//    public void setSpeed(int speed) {
//        this.speed = speed;
//    }
//
//    public void setAttackPattern(AttackPattern attackPattern) {
//        this.currentAttackPattern = attackPattern;
//    }
//
//    public Image getGunImage() {
//        return gunImage;
//    }
//}

package game.sample.entity;

import java.awt.Image;
import java.awt.Point;

public class Gun {
    public Bullet fire(Point position, int speed, int direction, Image bulletImage) {
        return new Bullet(position, speed, direction, bulletImage);
    }
}
