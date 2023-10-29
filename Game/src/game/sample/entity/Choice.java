package game.sample.entity;

class Choice {
    String description;
    Runnable effect;

    public Choice(String description, Runnable effect) {
        this.description = description;
        this.effect = effect;
    }
}