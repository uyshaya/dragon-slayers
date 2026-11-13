package com.dragonslayers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Entry point for Dragon Slayers.
 *
 * <p>Opens the game window on the Swing Event Dispatch Thread.</p>
 */
public final class Main {

    /** Title shown on the window bar. */
    public static final String GAME_TITLE = "Dragon Slayers";

    /** Window width in pixels. */
    public static final int WINDOW_WIDTH = 960;

    /** Window height in pixels. */
    public static final int WINDOW_HEIGHT = 640;

    /** Prevents creating instances of this class. */
    private Main() {
    }

    /**
     * Launches the game.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(GAME_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
