package com.dragonslayers.controller;

import com.dragonslayers.exceptions.CorruptedSaveException;
import com.dragonslayers.exceptions.SaveNotFoundException;
import com.dragonslayers.io.BinarySaveManager;
import com.dragonslayers.io.LeaderboardManager;
import com.dragonslayers.io.SaveManager;
import com.dragonslayers.io.TextSaveManager;
import com.dragonslayers.model.*;
import com.dragonslayers.view.*;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    private final LeaderboardManager leaderboardManager = new LeaderboardManager();

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
     * @param heroes    the heroes the player chose
     * @param partyName the name the player gave the party
     */
    public void startGame(Party<Hero> heroes, String partyName) {
        Inventory inventory = new Inventory();
        inventory.addItem(new Potion(STARTING_POTION_NAME), STARTING_POTIONS);
        gameState = new GameState(partyName, heroes, inventory);
        startEncounter();
    }

    /**
     * Loads a saved game from a slot, or shows an error message if it fails.
     * <p>The format picks the save manager. Both are used through the {@link SaveManager} interface.
     * A save that holds the Dragon Key is a finished game, so it only shows the party's result
     * and returns to the main menu.</p>
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
            if (gameState.getInventory().getCount(EncounterFactory.DRAGON_KEY_NAME) > 0) {
                JOptionPane.showMessageDialog(gameFrame, gameState.getPartyName() + " saved the kingdom in "
                        + gameState.getTotalRounds() + " rounds.", "Finished game", JOptionPane.INFORMATION_MESSAGE);
                showMainMenu();
                return;
            }
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
     * Every hero stops defending and gets all ability uses back first.
     */
    private void startEncounter() {
        for (Hero hero : gameState.getHeroes().getMembers()) {
            hero.stopDefending(); // a defend does not carry over to the next battle
            hero.resetAbilityUses(); // every hero gets all ability uses back for the new battle
        }
        Party<Enemy> enemies = EncounterFactory.create(gameState.getEncounter());
        Battle battle = new Battle(gameState.getHeroes(), enemies);
        BattleEngine engine = new BattleEngine(battle, gameState.getInventory());
        battlePanel.startBattle(battle, engine, gameState.getInventory(), gameState.getEncounter());
        gameFrame.showScreen(GameFrame.BATTLE);
    }

    /**
     * Called by the battle screen when a battle ends.
     * <p>A loss shows the Game Over screen. A win first adds the battle's rounds to the total and the encounter's
     * reward to the inventory. Then a win against the boss records the win on the leaderboard and shows the
     * Victory screen, and any other win moves on to the next encounter and shows the inventory screen.</p>
     *
     * @param won    true if the party won
     * @param rounds how many rounds the battle took
     */
    public void battleEnded(boolean won, int rounds) {
        if (!won) {
            endPanel.showResult(false, gameState.getPartyName(), gameState.getTotalRounds());
            gameFrame.showScreen(GameFrame.END);
            return;
        }

        gameState.addRounds(rounds);

        List<Item> reward = EncounterFactory.createReward(gameState.getEncounter());
        for (Item item : reward) {
            gameState.getInventory().addItem(item);
        }
        String message = "Battle won!";
        if (!reward.isEmpty()) {
            message += " You found: " + reward.stream().map(Item::getName).collect(Collectors.joining(", ")) + ".";
        }
        JOptionPane.showMessageDialog(gameFrame, message);

        if (gameState.getEncounter() == EncounterFactory.LAST_ENCOUNTER) {
            recordVictory(); // the dragon is defeated, so the game is won
            endPanel.showResult(true, gameState.getPartyName(), gameState.getTotalRounds());
            gameFrame.showScreen(GameFrame.END);
            return;
        }
        gameState.nextEncounter();
        showInventory();
    }

    /**
     * Adds the party's win to the leaderboard file. Shows a message if the file cannot be read or written.
     */
    private void recordVictory() {
        try {
            Leaderboard leaderboard = leaderboardManager.load();
            leaderboard.recordWin(gameState.getPartyName(), gameState.getTotalRounds());
            leaderboardManager.save(leaderboard);
        } catch (CorruptedSaveException | IOException e) {
            JOptionPane.showMessageDialog(gameFrame, "Could not update the leaderboard: " + e.getMessage(),
                    "Leaderboard", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows the top three parties: most wins first, then fewest rounds.
     * Shows a message instead if the leaderboard file is damaged.
     */
    public void showLeaderboard() {
        try {
            List<LeaderboardEntry> top = leaderboardManager.load().getTopThreeParties();
            String text = "No party has defeated the dragon yet.";
            if (!top.isEmpty()) {
                StringBuilder lines = new StringBuilder();
                for (int i = 0; i < top.size(); i++) {
                    LeaderboardEntry entry = top.get(i);
                    lines.append(i + 1).append(". ").append(entry.getPartyName())
                            .append("  -  ").append(entry.getWins()).append(" wins, best ")
                            .append(entry.getBestRounds()).append(" rounds\n");
                }
                text = lines.toString();
            }
            JOptionPane.showMessageDialog(gameFrame, text, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
        } catch (CorruptedSaveException e) {
            JOptionPane.showMessageDialog(gameFrame, e.getMessage(), "Leaderboard", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows the inventory and status screen for the current game.
     */
    private void showInventory() {
        inventoryPanel.showState(gameState);
        gameFrame.showScreen(GameFrame.INVENTORY);
    }
}
