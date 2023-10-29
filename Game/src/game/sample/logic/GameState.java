/*** In The Name of Allah ***/
package game.sample.logic;

import game.sample.entity.Boss;
import game.sample.entity.Girl;
import game.sample.entity.Bullet; // Importing Bullet class

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List; // Import for list of bullets
import java.util.ArrayList; // Import for ArrayList

public class GameState {

	private Girl girl;
	private Boss boss;
	private List<Bullet> bullets;
	private KeyHandler keyHandler;

	private boolean keyRIGHT, keyLEFT, keySpace;
	public boolean gameOver, gamePass;
	private final int GRAVITY = 3; // Gravity pulling the girl down every frame
	private final int JUMP_STRENGTH = -15; // The initial upward velocity when jumping
	private int verticalVelocity = 0; // The current vertical velocity of the girl


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
	}

	public Girl getGirl(){
		return this.girl;
	}
	public Boss getBoss() {return this.boss;}
	public List<Bullet> getBullets(){
		return bullets;
	}
	/**
	 * The method which updates the game state.
	 */
	public void update() {
		if(keySpace && verticalVelocity == 0) {
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
		if(girl.getY() >= 390) {
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

        girl.toggleImage();

        // Update the bullets' positions
        for (Bullet bullet : bullets) {
            bullet.move();
        }

        // Remove bullets that are off the screen
        bullets.removeIf(Bullet::isOffScreen);
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
                    bullets.add(bullet);
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

