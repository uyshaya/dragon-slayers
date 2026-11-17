package com.dragonslayers.controller;

import com.dragonslayers.exceptions.CorruptedSaveException;
import com.dragonslayers.exceptions.SaveNotFoundException;
import com.dragonslayers.io.BinarySaveManager;
import com.dragonslayers.io.SaveManager;
import com.dragonslayers.model.*;
import com.dragonslayers.view.*;

import javax.swing.*;

/**
 * Handles the game flow.
 */
public class GameController {

    private static final String STARTING_POTION_NAME = "Small Potion";

    private static final int STARTING_POTIONS = 2;

    private final GameFrame gameFrame = new GameFrame();

    private final SaveManager binarySaveManager = new BinarySaveManager();

    private GameState gameState;

    private BattlePanel battlePanel;

    private InventoryPanel inventoryPanel;

    private CharacterSelectPanel characterSelectPanel;

    /**
     * Adds the screens, shows the main menu, and opens the window.
     */
    public void start() {
        characterSelectPanel = new CharacterSelectPanel(this);
        battlePanel = new BattlePanel(this);
        inventoryPanel = new InventoryPanel(this);
        gameFrame.addScreen(new MainMenuPanel(this), GameFrame.MAIN_MENU);
        gameFrame.addScreen(characterSelectPanel, GameFrame.CHARACTER_SELECT);
        gameFrame.addScreen(battlePanel, GameFrame.BATTLE);
        gameFrame.addScreen(inventoryPanel, GameFrame.INVENTORY);
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
     * Creates the game state for the chosen party, gives it the starting items, and starts the first battle.
     *
     * @param heroes the heroes the player chose
     */
    public void startGame(Party<Hero> heroes) {
        Inventory inventory = new Inventory();
        inventory.addItem(new Potion(STARTING_POTION_NAME), STARTING_POTIONS);
        gameState = new GameState(heroes, inventory);
        startEncounter();
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
            showInventory();
        } catch (SaveNotFoundException | CorruptedSaveException e) {
            JOptionPane.showMessageDialog(gameFrame, e.getMessage(), "Load failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Starts the battle for the current encounter and shows the battle screen.
     */
    private void startEncounter() {
        for (Hero hero : gameState.getHeroes().getMembers()) {
            hero.stopDefending(); // a defend does not carry over to the next battle
        }
        Party<Enemy> enemies = EncounterFactory.create(gameState.getEncounter());
        Battle battle = new Battle(gameState.getHeroes(), enemies);
        BattleEngine engine = new BattleEngine(battle, gameState.getInventory());
        battlePanel.startBattle(battle, engine, gameState.getInventory());
        gameFrame.showScreen(GameFrame.BATTLE);
    }

    /**
     * Called by the battle screen when a battle ends.
     * <p>A loss or a win against the boss ends the game. Any other win moves on to the next encounter.</p>
     *
     * @param won true if the party won
     */
    public void battleEnded(boolean won) {
        if (!won) {
            JOptionPane.showMessageDialog(gameFrame, "Your party was defeated.", "Game over", JOptionPane.INFORMATION_MESSAGE);
            showMainMenu();
            return;
        }
        if (gameState.getEncounter() == EncounterFactory.LAST_ENCOUNTER) {
            JOptionPane.showMessageDialog(gameFrame, "The dragon is defeated. You win!", "Victory", JOptionPane.INFORMATION_MESSAGE);
            showMainMenu();
            return;
        }
        gameState.nextEncounter();
        JOptionPane.showMessageDialog(gameFrame, "Battle won! Next: encounter " + gameState.getEncounter() + ".");
        showInventory();
    }

    /**
     * Shows the inventory and status screen for the current game.
     */
    private void showInventory() {
        inventoryPanel.showState(gameState);
        gameFrame.showScreen(GameFrame.INVENTORY);
    }
}
