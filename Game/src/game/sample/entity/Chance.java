package game.sample.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Chance {
    List<Choice> choices;
    Random random;

    public Chance(Girl girl) {
        this.choices = new ArrayList<>();
        this.random = new Random();
        Gun gun = girl.getGun();
//         Define all possible choices
        choices.add(new Choice("+10 Max Health", () -> girl.setHealth(girl.getHealth()+10)));
        choices.add(new Choice("+10 damage", () -> gun.setDamage(gun.getDamage()+10)));
        choices.add(new Choice("+10 bullet speed", () -> gun.setBulletSpeed(gun.getBulletSpeed()+10)));
        choices.add(new Choice("+1 bullet shoot", () -> gun.updateBullet(gun.getBullet())));
    }

    public List<Choice> getRandomChoices() {
        Collections.shuffle(choices);
        return choices.subList(0, 2);
    }
}