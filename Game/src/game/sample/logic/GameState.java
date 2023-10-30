/*** In The Name of Allah ***/
package game.sample.logic;

import game.sample.entity.Boss;
import game.sample.entity.Girl;
import game.sample.entity.Slime;
import game.sample.entity.Bullet; // Importing Bullet class


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List; // Import for list of bullets
import java.util.ArrayList; // Import for ArrayList
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Rectangle;

public class GameState {

	private Girl girl;
	private Slime slime;
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
	private ArrayList<Slime> slimes = new ArrayList<>(); // 怪物列表



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
        long randomDelay = (random.nextInt(3) + 1) * 1000; // This will produce a delay between 1 to 5 seconds. Adjust as needed.

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


    /**
	 * The method which updates the game state.
	 */
	public void update() {
        if (keySpace && verticalVelocity == 0 && !girl.isSwim()) {
            girl.jump(); // Start the jump
        }
        else if (keySpace && girl.isSwim()){
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
        }
        else{
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

        if (spawning) {
            slime = new Slime(1280, random.nextInt(4) + 8);
            slimes.add(slime);
            spawning = false;
        }

        if (slimes != null && !slimes.isEmpty()){
            for (int i = 0; i < slimes.size(); i++) {
                Slime slime = slimes.get(i);
                slime.attack(girl);
                slime.move();

                // 检查怪物是否出屏幕，并从列表中移除
                if (slime.getX() < 0 || slime.getHealth()<=0) {
                    slimes.remove(i);
                    i--; // 需要减小索引以避免跳过元素
                }
            }
        }
        girl.toggleImage();


        // Update the bullets' positions
        for (Bullet bullet : bullets) {
            bullet.move();
        }
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

	}

    private boolean bulletCollidesWithSlime(Bullet bullet, Slime slime) {
        // Assuming Bullet and Slime classes have methods to get their positions and dimensions
        Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
        Rectangle slimeBounds = new Rectangle(slime.getX(), slime.getY(), slime.getWidth(), slime.getHeight());

        return bulletBounds.intersects(slimeBounds);
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



