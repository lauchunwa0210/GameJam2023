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
	private final int JUMP_STRENGTH = -15; // The initial upward velocity when jumping
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
        // 启动一个计时器任务，用于触发怪物生成
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                spawning = true;
            }
        }, 0, 3000); // 初始延迟为0毫秒，然后每隔spawnInterval毫秒触发一次
    }

	/**
	 * The method which updates the game state.
	 */
	public void update() {
        if (keySpace && verticalVelocity == 0) {
            girl.jump(); // Start the jump
        }

        girl.updatePosition();

        // Apply vertical velocity to the girl's Y position
        girl.setY(girl.getY() + verticalVelocity);
        girl.setY(Math.max(girl.getY(), 0));
        girl.setY(Math.min(girl.getY(), PipeFrame.GAME_HEIGHT));

        // Apply gravity
        verticalVelocity += GRAVITY;

        // Stop the girl from going below the ground (assuming ground is at GameFrame.GAME_HEIGHT - girl.getImageHeight())
        if (girl.getY() >= 390) {
            girl.setY(390);
            verticalVelocity = 0; // Reset vertical velocity when on the ground
        }

        if (keyLEFT)
            girl.setX(girl.getX() - 8);
        if (keyRIGHT)
            girl.setX(girl.getX() + 8);
        // Ensure the girl's position remains within bounds
        girl.setX(Math.max(girl.getX(), 20));
        girl.setX(Math.min(girl.getX(), PipeFrame.GAME_WIDTH - 100));

        if (spawning) {
            slime = new Slime(1000, random.nextInt(4) + 8);
            slimes.add(slime);
            spawning = false;
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

		if (spawning){
			slime =  new Slime(1000, random.nextInt(4) + 8);
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

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            for (int j = 0; j < slimes.size(); j++) {
                Slime slime = slimes.get(j);
                if (bulletCollidesWithSlime(bullet, slime)) {
                    slime.decreaseHealth(10);
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
                    break;
                case KeyEvent.VK_D:
                    keyRIGHT = true;
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
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    keyLEFT = false;
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



