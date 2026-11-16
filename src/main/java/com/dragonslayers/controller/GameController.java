package com.dragonslayers.controller;

import com.dragonslayers.exceptions.CorruptedSaveException;
import com.dragonslayers.exceptions.SaveNotFoundException;
import com.dragonslayers.io.BinarySaveManager;
import com.dragonslayers.io.SaveManager;
import com.dragonslayers.model.GameState;
import com.dragonslayers.view.GameFrame;
import com.dragonslayers.view.MainMenuPanel;

import javax.swing.*;

/**
 * Handles the game flow.
 */
public class GameController {

    private final GameFrame gameFrame = new GameFrame();

    private GameState gameState;

    private final SaveManager binarySaveManager = new BinarySaveManager();

    /**
     * Shows the main menu and opens the window.
     */
    public void start() {
        gameFrame.addScreen(new MainMenuPanel(this), GameFrame.MAIN_MENU);
        gameFrame.showScreen(GameFrame.MAIN_MENU);
        gameFrame.setVisible(true);
    }

    /**
     * Starts a new game by going to character selection.
     */
    public void newGame() {
        // select character
        // gameState = new GameState(chosenHeroes, new Inventory());
    }

    /**
     * Loads a saved game from a slot, or shows an error message if it fails.
     *
     * @param slot the slot number
     */
    public void loadGame(int slot) {
        try {
            gameState = binarySaveManager.load(slot);
            JOptionPane.showMessageDialog(gameFrame, "Loaded slot " + slot);
            // later: show the inventory/status screen
        } catch (SaveNotFoundException | CorruptedSaveException e) {
            JOptionPane.showMessageDialog(gameFrame, e.getMessage(), "Load failed", JOptionPane.ERROR_MESSAGE);
        }
    }

}
