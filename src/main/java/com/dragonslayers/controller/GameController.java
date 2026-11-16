package com.dragonslayers.controller;

import com.dragonslayers.exceptions.CorruptedSaveException;
import com.dragonslayers.exceptions.SaveNotFoundException;
import com.dragonslayers.io.BinarySaveManager;
import com.dragonslayers.io.SaveManager;
import com.dragonslayers.model.GameState;
import com.dragonslayers.model.Hero;
import com.dragonslayers.model.Inventory;
import com.dragonslayers.model.Party;
import com.dragonslayers.view.CharacterSelectPanel;
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

    private CharacterSelectPanel characterSelectPanel;

    /**
     * Adds the screens, shows the main menu, and opens the window.
     */
    public void start() {
        characterSelectPanel = new CharacterSelectPanel(this);
        gameFrame.addScreen(new MainMenuPanel(this), GameFrame.MAIN_MENU);
        gameFrame.addScreen(characterSelectPanel, GameFrame.CHARACTER_SELECT);
        gameFrame.showScreen(GameFrame.MAIN_MENU);
        gameFrame.setVisible(true);
    }

    /**
     * Shows the main menu.
     */
    public void showMainMenu() {
        gameFrame.showScreen(GameFrame.MAIN_MENU);
    }

    /**
     * Starts a new game by going to character selection with an empty party.
     */
    public void newGame() {
        characterSelectPanel.reset();
        gameFrame.showScreen(GameFrame.CHARACTER_SELECT);
    }

    /**
     * Creates the game state for the chosen party with an empty inventory.
     *
     * @param heroes the heroes the player chose
     */
    public void startGame(Party<Hero> heroes) {
        gameState = new GameState(heroes, new Inventory());
        JOptionPane.showMessageDialog(gameFrame, "Party ready. The battle screen comes next.");
        // later: start the first battle
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
