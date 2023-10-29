package game.sample.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Heart {
    private final Girl girl;
    private final Image heart = ImageIO.read(new File("Game/resource/image/pixel-heart.png"));

    public Heart(Girl girl) throws IOException {
        this.girl = girl;
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
