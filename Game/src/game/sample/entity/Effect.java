package game.sample.entity;

import game.sample.logic.PipeFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Effect {
    enum effectTypes{
        INCREASE_MAX_HEALTH,
        INCREASE_DAMAGE,
        DOUBLE_SHOOT,
        BULLET_SPREAD,
        INCREASE_SPEED,
    }

    private Image maxHealth;
    private Image damage;
    private Image doubleShoot;
    private Image spread;
    private Image shootSpeed;
    private effectTypes currentEffect;
    private Image currentImage;
    private int x; // x position
    private int y; // y position will be randomized
    private final int speed = 10;

    private final int imgWidth = 50;  // Adjusted for the size of heart pickup
    private final int imgHeight = 50; // Adjusted for the size of heart pickup

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Effect(){
        this.x = 400 + new Random().nextInt(PipeFrame.GAME_WIDTH-400);
        this.y = 300 + new Random().nextInt(100); // Randomize y position between 300 and 500
        effectTypes[] effects = effectTypes.values();
        int randomIndex = new Random().nextInt(effects.length);
        currentEffect =  effects[randomIndex];


        try {
            maxHealth = ImageIO.read(new File("Game/resource/image/effect_max_health.png"));
            damage = ImageIO.read(new File("Game/resource/image/effect_damage.png"));
            doubleShoot = ImageIO.read(new File("Game/resource/image/effect_bullet.png"));
            spread = ImageIO.read(new File("Game/resource/image/effect_spread.png"));
            shootSpeed = ImageIO.read(new File("Game/resource/image/effect_interval.png"));

            // resize
            maxHealth = maxHealth.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            damage = damage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            doubleShoot = doubleShoot.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            spread = spread.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            shootSpeed = shootSpeed.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);

        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (currentEffect){
            case INCREASE_MAX_HEALTH:
                currentImage = maxHealth;
                break;
            case INCREASE_DAMAGE:
                currentImage = damage;
                break;
            case DOUBLE_SHOOT:
                currentImage = doubleShoot;
                break;
            case BULLET_SPREAD:
                currentImage = spread;
                break;
            case INCREASE_SPEED:
                currentImage = shootSpeed;
                break;
        }

    }

    public void applyEffect(Girl girl){
        switch (currentEffect){
            case INCREASE_MAX_HEALTH:
                girl.setMaxHealth(girl.getMaxHealth()+10);
                break;
            case INCREASE_DAMAGE:
                girl.getGun().setDamage(girl.getGun().getDamage()+10);
                break;
            case DOUBLE_SHOOT:
                girl.getGun().setCurrentBulletType(BulletType.DOUBLE);
                break;
            case BULLET_SPREAD:
                girl.getGun().setCurrentBulletType(BulletType.TRIPLE);
                break;
            case INCREASE_SPEED:
                girl.getGun().setFireInterval(girl.getGun().getFireInterval() - 100);
                break;
        }
    }

    public void render(Graphics2D g2d) {
        g2d.drawImage(currentImage, x, y, null);
    }

    public void move() {
        this.x -= speed;  // Move the heart leftward by the speed
    }


}
