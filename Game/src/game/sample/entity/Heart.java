package game.sample.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Heart {
    private final Girl girl;

    private final int imgWidth = 30;
    private final int imgHeight = 30;
    private Image heartImg;

    private int x,y; //position

    public Heart(Girl girl) throws IOException {
        this.girl = girl;
        try {
            heartImg = ImageIO.read(new File("Game/resource/image/pixel-heart.png"));
            // resize
            heartImg = heartImg.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getHeart(Girl girl){
        if (this.x >= girl.getX() - 30 && this.x <= girl.getX() + 130){
            addBlood();
        }
    }

    public void addBlood(){
        if (girl.getHealth() <= 90){
            girl.setHealth(girl.getHealth()+10);
        }
        if (girl.getHealth() >90){
            girl.setHealth(100);
        }
    }
}
