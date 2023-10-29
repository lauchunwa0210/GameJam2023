package game.sample.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Girl {
    private Image girlImage;
    private int health;
    private int x, y; // position
    private boolean canMoveHorizontal; // true when in boss fight
    private Gun gun;
    private Image girlMove1;
    private Image girlMove2;
    private Image girlStill;
    private Image currentImage;
    private boolean isMoving;
    private int animationCounter = 0;
    private int switchInterval = 10; // Change this value to control the speed of the animation
    private int imgWidth = 200;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHealth(int health){
        this.health = health;
    }

    public int getHealth(){
        return this.health;
    }

    private int imgHeight = 200;


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
        this.isMoving = true;
        this.currentImage = girlMove2;
        this.gun = new Gun();
        this.x = StartX;
        this.y = StartY;
        this.health = 100;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getCurrentImage(){
        return this.currentImage;
    }

    public void move() {
        isMoving = true;

        // ... (Your move logic here)

        // Increment animation counter
        animationCounter++;

        // If counter reaches switchInterval, switch image
        if (animationCounter % switchInterval == 0) {
            if (currentImage == girlMove1) {
                currentImage = girlMove2;
            } else {
                currentImage = girlMove1;
            }
        }
    }

    public void stopMoving() {
        isMoving = false;
        animationCounter = 0;
        currentImage = girlMove1; // Reset to default image when not moving
    }

    private void MoveUp(){
        y -= 5;
    }

    private void MoveDown(){
        y+= 5;
    }

    public void moveRight() {
        if (canMoveHorizontal) {
            x += 5;
        }
    }

    public void moveLeft() {
        if (canMoveHorizontal) {
            x -= 5;
        }
    }
    public void shoot(){
        gun.fire();
    }
}
