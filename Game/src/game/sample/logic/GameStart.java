package game.sample.logic;

public class GameStart {
    public void gameStart(int GAME_WIDTH, int GAME_HEIGHT, GameFrame gameFrame){
        gameFrame.getContentPane().removeAll();
        gameFrame.setResizable(false);
        gameFrame.setSize(GAME_WIDTH, GAME_HEIGHT);
        gameFrame.initBufferStrategy();
        gameFrame.setFocusable(true);
        gameFrame.requestFocusInWindow();

        // Create and execute the game-loop
        GameLoop game = new GameLoop(gameFrame);
        game.init();
        ThreadPool.execute(game);
    }
}
