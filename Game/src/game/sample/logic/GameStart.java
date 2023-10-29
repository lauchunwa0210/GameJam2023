package game.sample.logic;

public class GameStart {
    public void gameStart(int GAME_WIDTH, int GAME_HEIGHT, BaseFrameOld frame){
        frame.getContentPane().removeAll();
        frame.setResizable(false);
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.initBufferStrategy();
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        // Create and execute the game-loop
//        GameLoop game = new GameLoop(frame);
//        game.init();
//        ThreadPool.execute(game);
    }
}
