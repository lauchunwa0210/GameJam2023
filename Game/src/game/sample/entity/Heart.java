package game.sample.entity;

public class Heart {
    private final Girl girl;

    public Heart(Girl girl) {
        this.girl = girl;
    }

    public void addBlood(){
        if (girl.blood <= 90){
            girl.blood = girl.blood + 10;
        }
        if (girl.blood >90){
            girl.blood = 100;
        }
    }
}
