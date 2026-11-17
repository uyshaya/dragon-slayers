package com.dragonslayers.controller;

import com.dragonslayers.exceptions.CorruptedSaveException;
import com.dragonslayers.exceptions.SaveNotFoundException;
import com.dragonslayers.io.BinarySaveManager;
import com.dragonslayers.io.SaveManager;
import com.dragonslayers.io.TextSaveManager;
import com.dragonslayers.model.*;
import com.dragonslayers.view.*;

import javax.swing.*;
import java.io.IOException;

/**
 * Handles the game flow.
 */
public class GameController {

    private static final String STARTING_POTION_NAME = "Small Potion";

    private static final int STARTING_POTIONS = 2;

    /**
     * The save format name for binary saves ({@code .dat}), shown when loading.
     */
    public static final String BINARY_FORMAT = "Binary (.dat)";

    /**
     * The save format name for text saves ({@code .txt}), shown when loading.
     */
    public static final String TEXT_FORMAT = "Text (.txt)";

    private final GameFrame gameFrame = new GameFrame();

    private final SaveManager binarySaveManager = new BinarySaveManager();

    private final SaveManager textSaveManager = new TextSaveManager();

    private GameState gameState;

    private BattlePanel battlePanel;

    private InventoryPanel inventoryPanel;

    private EndPanel endPanel;

    private CharacterSelectPanel characterSelectPanel;

    /**
     * Adds the screens, shows the main menu, and opens the window.
     */
    public void start() {
        characterSelectPanel = new CharacterSelectPanel(this);
        battlePanel = new BattlePanel(this);
        inventoryPanel = new InventoryPanel(this);
        endPanel = new EndPanel(this);
        gameFrame.addScreen(new MainMenuPanel(this), GameFrame.MAIN_MENU);
        gameFrame.addScreen(characterSelectPanel, GameFrame.CHARACTER_SELECT);
        gameFrame.addScreen(battlePanel, GameFrame.BATTLE);
        gameFrame.addScreen(inventoryPanel, GameFrame.INVENTORY);
        gameFrame.addScreen(endPanel, GameFrame.END);
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
     * <p>The format picks the save manager. Both are used through the {@link SaveManager} interface.</p>
     *
     * @param slot   the slot number
     * @param format {@link #BINARY_FORMAT} or {@link #TEXT_FORMAT}
     */
    public void loadGame(int slot, String format) {
        SaveManager saveManager = binarySaveManager;
        if (TEXT_FORMAT.equals(format)) {
            saveManager = textSaveManager;
        }

        try {
            gameState = saveManager.load(slot);
            JOptionPane.showMessageDialog(gameFrame, "Loaded slot " + slot + " (" + format + ")");
            showInventory();
        } catch (SaveNotFoundException | CorruptedSaveException e) {
            JOptionPane.showMessageDialog(gameFrame, e.getMessage(), "Load failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves the game to a slot in both formats: binary (the full game state) and text (readable in an editor).
     * Shows a message when it is done or when it fails.
     *
     * @param slot the slot number
     * @return true if both files were saved, false if saving failed
     */
    public boolean saveGame(int slot) {
        try {
            binarySaveManager.save(gameState, slot);
            textSaveManager.save(gameState, slot);
            JOptionPane.showMessageDialog(gameFrame, "Saved to slot " + slot);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(gameFrame, "Could not save: " + e.getMessage(), "Save failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Leaves the inventory screen and starts the next battle.
     */
    public void continueGame() {
        startEncounter();
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
        battlePanel.startBattle(battle, engine, gameState.getInventory(), gameState.getEncounter());
        gameFrame.showScreen(GameFrame.BATTLE);
    }

    /**
     * Called by the battle screen when a battle ends.
     * <p>A loss or a win against the boss shows the end screen.
     * Any other win moves on to the next encounter and shows the inventory screen.</p>
     *
     * @param won true if the party won
     */
    public void battleEnded(boolean won) {
        if (!won || gameState.getEncounter() == EncounterFactory.LAST_ENCOUNTER) {
            endPanel.showResult(won);
            gameFrame.showScreen(GameFrame.END);
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
