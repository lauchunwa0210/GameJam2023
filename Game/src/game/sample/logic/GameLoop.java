/*** In The Name of Allah ***/
package game.sample.logic;

import javax.swing.*;

/**
 * A very simple structure for the main game loop.
 * THIS IS NOT PERFECT, but works for most situations.
 * Note that to make this work, none of the 2 methods 
 * in the while loop (update() and render()) should be 
 * long running! Both must execute very quickly, without 
 * any waiting and blocking!
 * 
 * Detailed discussion on different game loop design
 * patterns is available in the following link:
 *    http://gameprogrammingpatterns.com/game-loop.html
 * 
 * @author Seyed Mohammad Ghaffarian
 */
public class GameLoop implements Runnable {
	
	/**
	 * Frame Per Second.
	 * Higher is better, but any value above 24 is fine.
	 */
	public static final int FPS = 30;
	
	private PipeFrame pipeCanvas;
	private BossFrame bossCanvas;
	private GameState state;
	boolean isPipe;

	public GameLoop(PipeFrame frame) {
		pipeCanvas = frame;
		isPipe = true;
	}
	public GameLoop(BossFrame frame) throws InterruptedException {
		isPipe = false;

		bossCanvas = frame;
		Thread.sleep(1000);
	}

	/**
	 * This must be called before the game loop starts.
	 */
	public void init() {
		state = new GameState();
		if (isPipe) pipeCanvas.addKeyListener(state.getKeyListener());
		else bossCanvas.addKeyListener(state.getKeyListener());
	}

	@Override
	public void run() {
		boolean gameOver = false;
		while (!gameOver && isPipe) {
			try {
				long start = System.currentTimeMillis();
				//
				state.update();
				if (pipeCanvas.isVisible()) {
					pipeCanvas.render(state);
				}
				gameOver = state.gameOver;
				//
				long delay = (1000 / FPS) - (System.currentTimeMillis() - start);
				if (delay > 0)
					Thread.sleep(delay);
			} catch (InterruptedException ex) {
			}
		}
			while (!gameOver) {
				try {
					long start = System.currentTimeMillis();
					//
					state.update();

					if (bossCanvas.isVisible()) {
						bossCanvas.render(state);
					}
					gameOver = state.gameOver;
					//
					long delay = (1000 / FPS) - (System.currentTimeMillis() - start);
					if (delay > 0)
						Thread.sleep(delay);
				}catch (InterruptedException ex) {
				}

			try {
				bossCanvas.render(state);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
