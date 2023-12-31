/*** In The Name of Allah ***/
package game.sample.logic;

import game.sample.entity.*;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class GameState {

	private Girl girl;
	private Slime slime;
    private Effect effect;
	private Boss boss;
	private List<Bullet> bullets;
	private KeyHandler keyHandler;

	private boolean keyRIGHT, keyLEFT, keySpace;
	public boolean gameOver, gamePass;
	private final int GRAVITY = 3; // Gravity pulling the girl down every frame
    private final int SWIMGRAVITY = 4; // Gravity pulling the girl down every frame


    private int verticalVelocity = 0; // The current vertical velocity of the girl

	private Timer timer = new Timer();
	private Random random = new Random();
	private boolean spawning = false;
    private boolean effectSpawning = false;
	private ArrayList<Slime> slimes = new ArrayList<>(); // 怪物列表
    private ArrayList<Heart> hearts = new ArrayList<>();

    private int score = 0;

	public GameState() {
		gameOver = false;
		gamePass = false;

		keyRIGHT = false;
		keyLEFT = false;
		keySpace = false;

		keyHandler = new KeyHandler();

		girl = new Girl(0, 400);
		boss = new Boss(980, 300, 300, 250, 1000);
		bullets = new ArrayList<>();
        timeStart();
        spawnHeart();
        spawnEffect();
	}

    public Effect getEffect() {
        return effect;
    }

    public ArrayList<Heart> getHearts() {
        return this.hearts;
    }

    public void spawnHeart() {
        long randomDelay = (random.nextInt(3) + 1) * 1000; // This will produce a delay between 1 to 5 seconds. Adjust as needed.

        // Schedule a task with the random delay
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Reschedule the task with a new random delay
                Heart heart = new Heart();
                hearts.add(heart);
            }

        }, randomDelay);

    }

    public Girl getGirl(){
		return this.girl;
	}
	public Boss getBoss() {return this.boss;}
	public List<Bullet> getBullets(){
		return bullets;
	}

    public ArrayList<Slime> getSlimes() {
        return this.slimes;
    }

    public void timeStart() {
        // Calculate a random delay in milliseconds
        long randomDelay = (random.nextInt(3) + 3) * 500; // This will produce a delay between 1 to 5 seconds. Adjust as needed.

        // Schedule a task with the random delay
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                spawning = true;
                // Reschedule the task with a new random delay
                timeStart();
            }
        }, randomDelay);
    }

    public void spawnEffect(){
        // Calculate a random delay in milliseconds
        long r1 = (random.nextInt(10) + 5) * 500; // This will produce a delay between 1 to 5 seconds. Adjust as needed.

        // Schedule a task with the random delay
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                effectSpawning = true;
                // Reschedule the task with a new random delay
            }
        }, r1);
    }

	/**
	 * The method which updates the game state.
	 */
	public void update() {
        if (keySpace && verticalVelocity == 0 && !girl.isSwim()) {
            girl.jump(); // Start the jump
        } else if (keySpace && girl.isSwim()) {
            girl.jump();
        }
        girl.setY(girl.getY() + verticalVelocity);

        girl.updatePosition();

        // Apply vertical velocity to the girl's Y position
        girl.setY(Math.max(girl.getY(), 0));
        girl.setY(Math.min(girl.getY(), PipeFrame.GAME_HEIGHT - 200));

        // Apply gravity
        if (!girl.isSwim()) {
            verticalVelocity += GRAVITY;

            // Stop the girl from going below the ground (assuming ground is at GameFrame.GAME_HEIGHT - girl.getImageHeight())
            if (girl.getY() >= 390) {
                girl.setY(390);
                verticalVelocity = 0; // Reset vertical velocity when on the ground
            }
        } else {
            verticalVelocity = SWIMGRAVITY;
        }

        if (keyLEFT) {
            girl.setX(girl.getX() - 15);
        }
        if (keyRIGHT) {
            girl.setX(girl.getX() + 8);
        }
        // Ensure the girl's position remains within bounds
        girl.setX(Math.max(girl.getX(), 20));
        girl.setX(Math.min(girl.getX(), PipeFrame.GAME_WIDTH - 100));

        if (!girl.isSwim()) {
            if (spawning) {
                slime = new Slime(1280, random.nextInt(10) + 13);
                slimes.add(slime);
                spawning = false;
            }

            if (effectSpawning) {
                effect = new Effect();
                effectSpawning = false;
            }

            if (slimes != null && !slimes.isEmpty()) {
                for (int i = 0; i < slimes.size(); i++) {
                    Slime slime = slimes.get(i);
                    slime.attack(girl);
                    slime.move();

                    // 检查怪物是否出屏幕，并从列表中移除
                    if (slime.getX() < 0 || slime.getHealth() <= 0) {
                        slimes.remove(i);
                        i--; // 需要减小索引以避免跳过元素
                        score++;
                    }
                }
            }
            for (Heart heart : hearts) {
                heart.move();
            }

            if (effect != null){
                effect.move();
            }

            Rectangle girlBounds = new Rectangle(girl.getX(), girl.getY(), girl.getImgWidth(), girl.getImgHeight());  // Using imgWidth and imgHeight from the Girl class directly

            for (int i = 0; i < hearts.size(); i++) {
                Heart heart = hearts.get(i);
                Rectangle heartBounds = new Rectangle(heart.getX(), heart.getY(), heart.getWidth(), heart.getHeight());

                if (girlBounds.intersects(heartBounds)) {
                    girl.setHealth(Math.min(girl.getMaxHealth(), girl.getHealth() + 30)); // Increase health by 10 but don't exceed 100
                    hearts.remove(i);
                    i--;
                    spawnHeart();  // Spawn a new heart after one is picked up
                }
            }
            if (getGirl().getHealth() <= 0) {
                gameOver = true;
            }

            // effect bound
            if (effect != null) {
                Rectangle effectBounds = new Rectangle(effect.getX(), effect.getY(), effect.getImgWidth(), effect.getImgHeight());
                if (girlBounds.intersects(effectBounds)) {
                    effect.applyEffect(girl);
                    effect = null;
                    spawnEffect();

                }
            }
        }

        girl.toggleImage();

        // Remove bullets that are off the screen
        bullets.removeIf(Bullet::isOffScreen);

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            for (int j = 0; j < slimes.size(); j++) {
                Slime slime = slimes.get(j);
                if (bulletCollidesWithSlime(bullet, slime)) {
                    slime.decreaseHealth(girl.getGun().getDamage());
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }

        // Update the bullets' positions
        for (Bullet bullet : bullets) {
            bullet.move();
        }

        Iterator<Missile> missileIterator = boss.getMissiles().iterator();
        while (missileIterator.hasNext()){
            Missile missile = missileIterator.next();
            if(missile.collidesWithGirl(girl)){
                girl.setHealth(girl.getHealth()-20);
                missileIterator.remove();
            }
        }


//        Boss vs gril
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bulletCollidesWithBoss(bullet, boss)) {
                boss.takeDamage(10);  // adjust the damage value as needed
                bullets.remove(i);
                i--;
            }
        }




    }
				// 检查怪物是否出屏幕，并从列表中移除



    private boolean bulletCollidesWithSlime(Bullet bullet, Slime slime) {
        // Assuming Bullet and Slime classes have methods to get their positions and dimensions
        Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
        Rectangle slimeBounds = new Rectangle(slime.getX(), slime.getY(), slime.getWidth(), slime.getHeight());

        return bulletBounds.intersects(slimeBounds);
    }

    private boolean bulletCollidesWithBoss(Bullet bullet, Boss boss) {
        Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
        Rectangle bossBounds = new Rectangle(boss.getX(), boss.getY(), boss.getWidth(), boss.getHeight());
        return bulletBounds.intersects(bossBounds);
    }

    public KeyListener getKeyListener() {
        return keyHandler;
    }


    /**
     * The keyboard handler.
     */
    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    keyLEFT = true;
                    girl.setRight(false);
                    break;
                case KeyEvent.VK_D:
                    keyRIGHT = true;
                    girl.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    keySpace = true;
                    break;
                case KeyEvent.VK_ESCAPE:
                    gameOver = true;
                    break;
                case KeyEvent.VK_ENTER: // "Entre" key for firing a bullet
                    Bullet bullet = girl.shoot();
                    if (bullet != null) bullets.add(bullet);
                    break;
                case KeyEvent.VK_H: // "H" key for reducing boss's health
                    boss.takeDamage(10);
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    keyLEFT = false;
                    if(!girl.isSwim()) {
                        girl.setRight(true);
                    }
                    break;
                case KeyEvent.VK_D:
                    keyRIGHT = false;
                    break;
                case KeyEvent.VK_SPACE:
                    keySpace = false;
                    break;
            }
        }
    }
}



