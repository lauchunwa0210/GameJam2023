package game.sample.logic;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        // Initialize the global thread-pool
        ThreadPool.init();

        // Show the game menu ...

        // After the palyer clicks 'PLAY' ...
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                PipeFrame frame = new PipeFrame("Toxic Tide");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.showMenu();

            }
        });
    }
}
