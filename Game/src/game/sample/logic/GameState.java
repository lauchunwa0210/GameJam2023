/*** In The Name of Allah ***/
package game.sample.logic;

import game.sample.entity.Girl;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This class holds the state of game and all of its elements.
 * This class also handles user inputs, which affect the game state.
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class GameState {
	
	public int locX, locY, diam;
	public boolean gameOver, gamePass;
	
	private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT;
	private boolean mousePress;
	private int mouseX, mouseY;	
	private KeyHandler keyHandler;
//	private MouseHandler mouseHandler;

	private Girl girl;
	
	public GameState() {
		locX = 100;
		locY = 100;
		diam = 32;
		gameOver = false;
		//
		keyUP = false;
		keyDOWN = false;
		keyRIGHT = false;
		keyLEFT = false;
		//
		mousePress = false;
		mouseX = 0;
		mouseY = 0;
		//
		keyHandler = new KeyHandler();
//		mouseHandler = new MouseHandler();

		girl = new Girl(100, 100);
	}

	public Girl getGirl(){
		return this.girl;
	}
	
	/**
	 * The method which updates the game state.
	 */
	public void update() {
		if (mousePress) {
			locY = mouseY - diam / 2;
			locX = mouseX - diam / 2;
		}
		if (keyUP)
			girl.setY(girl.getY() - 8);
		if (keyDOWN)
			girl.setY(girl.getY() + 8);
		if (keyLEFT)
			girl.setX(girl.getX() - 8);
		if (keyRIGHT)
			girl.setX(girl.getX() + 8);

		locX = Math.max(locX, 0);
		locX = Math.min(locX, GameFrame.GAME_WIDTH - diam);
		locY = Math.max(locY, 0);
		locY = Math.min(locY, GameFrame.GAME_HEIGHT - diam);
	}
	
	
	public KeyListener getKeyListener() {
		return keyHandler;
	}
//	public MouseListener getMouseListener() {
//		return mouseHandler;
//	}
//	public MouseMotionListener getMouseMotionListener() {
//		return mouseHandler;
//	}



	/**
	 * The keyboard handler.
	 */
	class KeyHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_W:
					keyUP = true;
					break;
				case KeyEvent.VK_S:
					keyDOWN = true;
					break;
				case KeyEvent.VK_A:
					keyLEFT = true;
					break;
				case KeyEvent.VK_D:
					keyRIGHT = true;
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
				case KeyEvent.VK_W:
					keyUP = false;
					break;
				case KeyEvent.VK_S:
					keyDOWN = false;
					break;
				case KeyEvent.VK_A:
					keyLEFT = false;
					break;
				case KeyEvent.VK_D:
					keyRIGHT = false;
					break;
			}
		}

	}

	/**
	 * The mouse handler.
	 */
//	class MouseHandler extends MouseAdapter {
//
//		@Override
//		public void mousePressed(MouseEvent e) {
//			mouseX = e.getX();
//			mouseY = e.getY();
//			mousePress = true;
//		}
//
//		@Override
//		public void mouseReleased(MouseEvent e) {
//			mousePress = false;
//		}
//
//		@Override
//		public void mouseDragged(MouseEvent e) {
//			mouseX = e.getX();
//			mouseY = e.getY();
//		}
//	}
}

