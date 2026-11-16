package com.dragonslayers;

import com.dragonslayers.controller.GameController;

import javax.swing.*;

/**
 * Entry point for Dragon Slayers.
 *
 * <p>Opens the game window on the Swing Event Dispatch Thread.</p>
 */
public final class Main {

    /**
     * Prevents creating instances of this class.
     */
    private Main() {
    }

    /**
     * Launches the game.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameController().start();
        });
    }
}
