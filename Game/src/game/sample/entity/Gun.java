package game.sample.entity;

enum AttackPattern{
    STANDARD,
    SPREAD,
    DOUBLE,
}

public class Gun {
    int damage;
    int speed;

    private void Gun(){
        this.damage = 10;
        this.speed = 5;
    }

    void fire(){

    }

}
