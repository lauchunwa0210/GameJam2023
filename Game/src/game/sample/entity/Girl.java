package game.sample.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Girl {
    private Image girlImage;
    private int health;
    private int maxHealth;
    private int x, y; // position
    private boolean canMoveHorizontal; // true when in boss fight
    private final Gun gun;
    private int damage;
    private Image girlMove1;
    private Image girlMove2;
    private Image girlStill;
    private Image currentImage;
    private final int switchInterval = 4;
    private int animationCounter = 0;
    private final int imgWidth = 130;
    private final int imgHeight = 130;
    private boolean jumping = false;
    private final int jumpHeight = 500;   // Adjust this to change the height of the jump
    private final int jumpSpeed = 100;// Adjust this to change the speed of the jump
    private int jumpCounter = 0;
    private final int GRAVITY = 1; // Gravity pulling the girl down every frame
	private final int JUMP_STRENGTH = -15; // The initial upward velocity when jumping
	private int verticalVelocity = 0; // The current vertical velocity of the girl


    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
        this.currentImage = girlMove2;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setHealth(int health){
        this.health = health;
    }

    public int getHealth(){
        return this.health;
    }
    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }
    public int getMaxHealth(){
        return this.maxHealth;
    }

    public Girl(int StartX, int StartY){
        try {
            girlMove1 = ImageIO.read(new File("Game/resource/image/girl_move1.png"));
            girlMove2 = ImageIO.read(new File("Game/resource/image/girl_move2.png"));
            girlStill = ImageIO.read(new File("Game/resource/image/girl_still.png"));

            // resize
            girlMove1 = girlMove1.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            girlMove2 = girlMove2.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            girlStill = girlStill.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);


        } catch (IOException e) {
            e.printStackTrace();
        }
        this.currentImage = girlMove1;
        this.gun = new Gun();
        this.x = StartX;
        this.y = StartY;
        this.health = 100;
        this.maxHealth = this.health;
        this.damage = 10;
    }

    public Image getCurrentImage(){
        return this.currentImage;
    }

    public void jump() {
        if (!jumping) {   // Check if she's not already jumping
            jumping = true;
            jumpCounter = 0; // Reset jumpCounter when starting a new jump
            currentImage = girlMove1;
        }
    }

    public void updatePosition() {
        if(jumping) {
            currentImage = girlMove1;
            // Move the girl up quickly
            this.y -= jumpSpeed;

            jumpCounter += jumpSpeed;

            // Stop jumping when the desired height is reached
            if(jumpCounter >= jumpHeight) {
                jumping = false;
            }
        }
    }

    public void toggleImage() {
        if (!jumping) {
            animationCounter++;
            // Change this value to control the speed of the animation
            if (animationCounter % switchInterval == 0) {
                if (currentImage == girlMove1) {
                    currentImage = girlMove2;
                } else {
                    currentImage = girlMove1;
                }
            }
        }
    }

    public Bullet shoot() {
        Point bulletStartPosition = new Point(this.x + this.imgWidth, this.y + this.imgHeight / 2 + 10); // Adjust as per the desired start position of the bullet
        return gun.fire(bulletStartPosition);
    }

    public void drawBar(Graphics2D g2d){
        int x = 70;
        int y = 600;
        int barWidth = 150;
        int barHeight = 20;
        int healthWidth = (int) ((health / (double) maxHealth) * barWidth);

        // Draw the background of the health bar (empty part)
        g2d.setColor(new Color(50, 50, 50)); // Dark color for the background
        g2d.fillRect(x, y, barWidth, barHeight);

        // Draw the health portion of the bar
        g2d.setColor(new Color(50, 255, 50)); // Bright green for health
        g2d.fillRect(x, y, healthWidth, barHeight);

        // Draw the border of the health bar
        g2d.setColor(Color.WHITE); // White color for the border
        g2d.drawRect(x, y, barWidth, barHeight);
    }
    public void drawDamgeInfo(Graphics2D g2d){
        int x = 70;
        int y = 600;
        int width = 150;
        int height = 20;
        g2d.setColor(Color.darkGray);
        g2d.fillRect(x, y-30, width, height);

        // Draw the damage bar fill
        g2d.setColor(Color.red);
        for (int i = 0; i < damage; i += 2) { // Increment by 2 for larger 'pixels'
            for (int j = 0; j < height; j += 2) {
                g2d.fillRect(x, y-30, i, height);
            }
        }

        // Draw a simple border
        g2d.setColor(Color.white);
        g2d.drawRect(x, y-30, width, height);
    }

}
