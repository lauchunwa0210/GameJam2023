///*** In The Name of Allah ***/
//package game.sample.logic;
//
//import game.sample.entity.Girl;
//
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//
//
///**
// * This class holds the state of game and all of its elements.
// * This class also handles user inputs, which affect the game state.
// *
// */
//public class GameState {
//
//	public int locX, locY, diam;
//	public boolean gameOver, gamePass;
//
//	private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT, keySpace;
//	private boolean mousePress;
//	private int mouseX, mouseY;
//	private KeyHandler keyHandler;
//	private Girl girl;
//
//	private final int GRAVITY = 1; // Gravity pulling the girl down every frame
//	private final int JUMP_STRENGTH = -15; // The initial upward velocity when jumping
//	private int verticalVelocity = 0; // The current vertical velocity of the girl
//
//
//	public GameState() {
//		locX = 100;
//		locY = 100;
//		diam = 32;
//		gameOver = false;
//		//
//		keyUP = false;
//		keyDOWN = false;
//		keyRIGHT = false;
//		keyLEFT = false;
//		//
//		mousePress = false;
//		mouseX = 0;
//		mouseY = 0;
//		//
//		keyHandler = new KeyHandler();
////		mouseHandler = new MouseHandler();
//
//		girl = new Girl(100, 100);
//	}
//
//	public Girl getGirl(){
//		return this.girl;
//	}
//
//	/**
//	 * The method which updates the game state.
//	 */
//	public void update() {
//		if (keyLEFT)
//			girl.setX(girl.getX() - 8);
//		if (keyRIGHT)
//			girl.setX(girl.getX() + 8);
//
//		girl.updatePosition();
//
//		girl.setX(Math.max(girl.getX(), 0));
//		girl.setX(Math.min(girl.getX(), GameFrame.GAME_WIDTH - diam));
//
//		if(keySpace && verticalVelocity == 0) {
//			girl.jump(); // Start the jump
//		}
//
////		// Apply vertical velocity to the girl's Y position
////		girl.setY(girl.getY() + verticalVelocity);
////		girl.setY(Math.max(girl.getY(), 0));
////		girl.setY(Math.min(girl.getY(), GameFrame.GAME_HEIGHT - diam));
////
////		// Apply gravity
////		verticalVelocity += GRAVITY;
//////		// Stop the girl from going below the ground (assuming ground is at GameFrame.GAME_HEIGHT - girl.getImageHeight())
////		if(girl.getY() >= GameFrame.GAME_HEIGHT - 400) {
////			girl.setY(GameFrame.GAME_HEIGHT - 400);
////			verticalVelocity = 0; // Reset vertical velocity when on the ground
////		}
////		girl.toggleImage();
//
//
//	}
//
//
//	public KeyListener getKeyListener() {
//		return keyHandler;
//	}
////	public MouseListener getMouseListener() {
////		return mouseHandler;
////	}
////	public MouseMotionListener getMouseMotionListener() {
////		return mouseHandler;
////	}
//
//
//
//	/**
//	 * The keyboard handler.
//	 */
//	class KeyHandler extends KeyAdapter {
//
//		@Override
//		public void keyPressed(KeyEvent e) {
//			switch (e.getKeyCode())
//			{
//				case KeyEvent.VK_W:
//					keyUP = true;
//					break;
//				case KeyEvent.VK_S:
//					keyDOWN = true;
//					break;
//				case KeyEvent.VK_A:
//					keyLEFT = true;
//					break;
//				case KeyEvent.VK_D:
//					keyRIGHT = true;
//					break;
//				case KeyEvent.VK_SPACE:
//					keySpace = true;
//					break;
//				case KeyEvent.VK_ESCAPE:
//					gameOver = true;
//					break;
//			}
//		}
//
//		@Override
//		public void keyReleased(KeyEvent e) {
//			switch (e.getKeyCode())
//			{
//				case KeyEvent.VK_W:
//					keyUP = false;
//					break;
//				case KeyEvent.VK_S:
//					keyDOWN = false;
//					break;
//				case KeyEvent.VK_A:
//					keyLEFT = false;
//					break;
//				case KeyEvent.VK_D:
//					keyRIGHT = false;
//					break;
//				case KeyEvent.VK_SPACE:
//					keySpace = false;
//			}
//		}
//
//	}
//
//	/**
//	 * The mouse handler.
//	 */
////	class MouseHandler extends MouseAdapter {
////
////		@Override
////		public void mousePressed(MouseEvent e) {
////			mouseX = e.getX();
////			mouseY = e.getY();
////			mousePress = true;
////		}
////
////		@Override
////		public void mouseReleased(MouseEvent e) {
////			mousePress = false;
////		}
////
////		@Override
////		public void mouseDragged(MouseEvent e) {
////			mouseX = e.getX();
////			mouseY = e.getY();
////		}
////	}
//}
//

/*** In The Name of Allah ***/
package game.sample.logic;

import game.sample.entity.Girl;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class holds the state of game and all of its elements.
 * This class also handles user inputs, which affect the game state.
 */
public class GameState {

	private Girl girl;
	private KeyHandler keyHandler;

	private boolean keyRIGHT, keyLEFT, keySpace;
	public boolean gameOver, gamePass;
	private final int GRAVITY = 1; // Gravity pulling the girl down every frame
	private final int JUMP_STRENGTH = -15; // The initial upward velocity when jumping
	private int verticalVelocity = 0; // The current vertical velocity of the girl


	public GameState() {
		gameOver = false;
		gamePass = false;

		keyRIGHT = false;
		keyLEFT = false;
		keySpace = false;

		keyHandler = new KeyHandler();
		girl = new Girl(0, 60);
	}

	public Girl getGirl(){
		return this.girl;
	}

	/**
	 * The method which updates the game state.
	 */
	public void update() {
		if(keySpace)
			girl.jump();
		if(keySpace && verticalVelocity == 0) {
			girl.jump(); // Start the jump
		}

		girl.updatePosition();

		// Apply vertical velocity to the girl's Y position
		girl.setY(girl.getY() + verticalVelocity);
		girl.setY(Math.max(girl.getY(), 0));
		girl.setY(Math.min(girl.getY(), GameFrame.GAME_HEIGHT));

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
		girl.setX(Math.max(girl.getX(), 0));
		girl.setX(Math.min(girl.getX(), GameFrame.GAME_WIDTH));

		girl.toggleImage();
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
			switch (e.getKeyCode())
			{
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
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode())
			{
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
