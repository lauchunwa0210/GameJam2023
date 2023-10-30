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
    private final Gun gun;
    private Image girlMoveRight1;
    private Image girlMoveRight2;
    private Image girlMoveLeft1;
    private Image girlMoveLeft2;
    private Image girlStill;
    private Image girlSwimDownRight;
    private Image girlSwimUpRight;
    private Image girlSwimDownLeft;
    private Image girlSwimUpLeft;
    private Image currentImage;
    private final int switchInterval = 10;
    private int animationCounter = 0;
    private final int imgWidth = 130;
    private final int imgHeight = 130;
    private boolean jumping = false;
    private final int jumpHeight = 200;   // Adjust this to change the height of the jump
    private final int swimUpHeight = 60;
    private final int jumpSpeed = 20;    // Adjust this to change the speed of the jump
    private final int swimUpSpeed = 10;
    private int jumpCounter = 0;
    private int swimCounter = 0;
    private boolean swim = false;
    private boolean isRight;

    public boolean isRight() {
        return isRight;
    }
    public void setRight(boolean right) {
        isRight = right;
    }

    public void setX(int x) {
        this.x = x;
    }



    public void setY(int y) {
        this.y = y;
        if (isRight) {
            this.currentImage = girlMoveRight1;
        } else {
            this.currentImage = girlMoveLeft1;
        }
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
    public boolean isSwim() {
        return swim;
    }

    public void setSwim(boolean swim) {
        if(!this.swim){
            this.swim = swim;
        }
    }

    public Girl(int StartX, int StartY){
        try {
            girlMoveRight1 = ImageIO.read(new File("Game/resource/image/girl_move1.png"));
            girlMoveRight2 = ImageIO.read(new File("Game/resource/image/girl_move2.png"));
            girlMoveLeft1 = ImageIO.read(new File("Game/resource/image/girl_move_left_1.png"));
            girlMoveLeft2 = ImageIO.read(new File("Game/resource/image/girl_move_left_2.png"));

            girlSwimDownRight = ImageIO.read(new File("Game/resource/image/girl_swim_down.png"));
            girlSwimUpRight = ImageIO.read(new File("Game/resource/image/girl_swim_up.png"));

            girlSwimDownLeft = ImageIO.read(new File("Game/resource/image/girl_swim_down_left.png"));
            girlSwimUpLeft = ImageIO.read(new File("Game/resource/image/girl_swim_up_left.png"));

            girlStill = ImageIO.read(new File("Game/resource/image/girl_still.png"));

            // resize
            girlMoveRight1 = girlMoveRight1.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            girlMoveRight2 = girlMoveRight2.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            girlMoveLeft1 = girlMoveLeft1.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            girlMoveLeft2 = girlMoveLeft2.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            girlSwimDownRight = girlSwimDownRight.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            girlSwimUpRight = girlSwimUpRight.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            girlSwimDownLeft = girlSwimDownRight.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            girlSwimDownLeft = girlSwimUpRight.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);

            girlStill = girlStill.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);


        } catch (IOException e) {
            e.printStackTrace();
        }
        this.currentImage = girlMoveRight1;
        this.gun = new Gun();
        this.x = StartX;
        this.y = StartY;
        this.health = 100;
        this.isRight = true;
    }

    public Image getCurrentImage(){
        return this.currentImage;
    }

    public void jump() {
        if (!swim) {
            if (!jumping) {   // Check if she's not already jumping
                jumping = true;
                jumpCounter = 0; // Reset jumpCounter when starting a new jump
                if(isRight) {
                    currentImage = girlMoveRight1;
                }else{
                    currentImage = girlMoveLeft1;
                }
            }
        } else {
            if (!jumping) {   // Check if she's not already jumping
                jumping = true;
                swimCounter = 0; // Reset jumpCounter when starting a new jump
                if(isRight) {
                    currentImage = girlSwimUpRight;
                }else{
                    currentImage = girlSwimUpLeft;
                }
            }
        }
    }




    public void updatePosition() {
        if (!swim) {
            if (jumping) {
                if (isRight) {
                    currentImage = girlMoveRight1;
                }else{
                    currentImage = girlMoveLeft1;
                }
                // Move the girl up quickly
                this.y -= jumpSpeed;

                jumpCounter += jumpSpeed;

                // Stop jumping when the desired height is reached
                if (jumpCounter >= jumpHeight) {
                    jumping = false;
                }
            }
        }
        else{
            if (jumping){
                if(isRight) {
                    currentImage = girlSwimUpRight;
                }else{
                    currentImage = girlSwimUpLeft;
                }
                this.y -= swimUpSpeed;

                swimCounter += swimUpSpeed;

                if (swimCounter >= swimUpHeight) {
                    jumping = false;
                }
            }
        }
    }

    public void toggleImage() {
        if (!swim) {
            if (!jumping) {
                animationCounter++;
                if (animationCounter % switchInterval == 0) {
                    if (isRight) {
                        if (currentImage == girlMoveRight1) {
                            currentImage = girlMoveRight2;
                        } else {
                            currentImage = girlMoveRight1;
                        }
                    } else {
                        if (currentImage == girlMoveLeft1) {
                            currentImage = girlMoveLeft2;
                        } else {
                            currentImage = girlMoveLeft1;
                        }
                    }
                }
            }
        }
        else if (!jumping){
            if (isRight) {
                currentImage = girlSwimDownRight;
            } else {
                currentImage = girlSwimDownLeft;
            }
        }
    }


    public Bullet shoot() {
        Point bulletStartPosition = new Point(this.x + this.imgWidth, this.y + this.imgHeight / 2 + 10); // Adjust as per the desired start position of the bullet
        return gun.fire(bulletStartPosition, isRight, isSwim());
    }
}
