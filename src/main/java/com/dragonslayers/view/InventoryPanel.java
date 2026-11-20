package com.dragonslayers.view;

import com.dragonslayers.controller.EncounterFactory;
import com.dragonslayers.controller.GameController;
import com.dragonslayers.exceptions.InvalidActionException;
import com.dragonslayers.model.GameState;
import com.dragonslayers.model.Hero;
import com.dragonslayers.model.Inventory;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The inventory and status screen, shown between battles.
 * <p>Shows each hero's stats and equipment and the shared inventory.
 * The player can use or equip items, save the game, or go on to the next battle.</p>
 */
public class InventoryPanel extends BackgroundPanel {

    private static final int PADDING = 10;

    private static final int INVENTORY_WIDTH = 360;

    private static final float TITLE_FONT_SIZE = 20f;

    private static final String NOTHING_EQUIPPED = "none";

    private static final String EMPTY_INVENTORY = "(empty)";

    private static final int NORMAL_EXIT = 0;

    private static final int BUTTON_GROUP_GAP = 40;

    private final JLabel titleLabel = new JLabel("", SwingConstants.CENTER);

    private final JTextArea heroArea = new JTextArea();

    private final JTextArea inventoryArea = new JTextArea();

    private final GameController controller;

    private GameState gameState;

    /**
     * Creates the inventory screen.
     *
     * @param controller the game controller
     */
    public InventoryPanel(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(PADDING, PADDING));

        add(titleLabel, BorderLayout.NORTH);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(TEXT_COLOR);

        heroArea.setEditable(false);
        JScrollPane partyScroll = createTitledScrollPane(heroArea, "Party");
        add(partyScroll, BorderLayout.CENTER);

        inventoryArea.setEditable(false);
        inventoryArea.setLineWrap(true);
        inventoryArea.setWrapStyleWord(true);
        JScrollPane inventoryScroll = createTitledScrollPane(inventoryArea, "Inventory");
        inventoryScroll.setPreferredSize(new Dimension(INVENTORY_WIDTH, 0));
        add(inventoryScroll, BorderLayout.EAST);

        JButton useButton = new JButton("Use Item");
        JButton saveButton = new JButton("Save Game");
        JButton saveQuitButton = new JButton("Save and Quit");
        JButton menuButton = new JButton("Main Menu");
        JButton nextButton = new JButton("Next Battle");
        useButton.addActionListener(e -> useItem());
        saveButton.addActionListener(e -> chooseSlotAndSave());
        saveQuitButton.addActionListener(e -> saveAndQuit());
        menuButton.addActionListener(e -> confirmMainMenu());
        nextButton.addActionListener(e -> controller.continueGame());

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(useButton); // buttons for this screen, in the order the player uses them
        buttons.add(saveButton);
        buttons.add(nextButton);
        buttons.add(Box.createHorizontalStrut(BUTTON_GROUP_GAP)); // empty space between the two groups
        buttons.add(saveQuitButton); // buttons that leave the game
        buttons.add(menuButton);
        add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Shows the given game on the screen.
     *
     * @param gameState the game state
     */
    public void showState(GameState gameState) {
        this.gameState = gameState;
        refresh();
    }

    /**
     * Asks the player for an item and a hero, then uses the item on that hero.
     * <p>A potion heals the hero. A weapon or armor is equipped by the hero.
     * Does nothing if the player cancels.</p>
     */
    private void useItem() {
        Inventory inventory = gameState.getInventory();
        List<String> itemNames = inventory.getUsableItemNames();
        if (itemNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No usable items");
            return;
        }

        String itemName = (String) JOptionPane.showInputDialog(this, "Use which item?", "Choose item",
                JOptionPane.QUESTION_MESSAGE, null, itemNames.toArray(), itemNames.get(0));
        if (itemName == null) {
            return; // the player pressed Cancel
        }

        List<Hero> heroes = gameState.getHeroes().getAliveMembers(); // a knocked-out hero cannot be healed
        String[] heroNames = heroes.stream()
                .map(Hero::getName)
                .toArray(String[]::new);
        String heroName = (String) JOptionPane.showInputDialog(this, "Use " + itemName + " on whom?", "Choose hero",
                JOptionPane.QUESTION_MESSAGE, null, heroNames, heroNames[0]);
        if (heroName == null) {
            return;
        }
        Hero target = heroes.get(List.of(heroNames).indexOf(heroName));

        try {
            inventory.useItem(itemName, target);
        } catch (InvalidActionException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Cannot do that", JOptionPane.WARNING_MESSAGE);
        }
        refresh();
    }

    /**
     * Asks the player for a save slot, then saves the game there.
     * Does nothing if the player cancels.
     */
    private void chooseSlotAndSave() {
        Integer slot = chooseSlot("Save Game");
        if (slot != null) {
            controller.saveGame(slot);
        }
    }

    /**
     * Asks the player for a save slot, saves the game there, then closes the game.
     * The game stays open if the player cancels or the save fails, so no progress is lost.
     */
    private void saveAndQuit() {
        Integer slot = chooseSlot("Save and Quit");
        if (slot != null && controller.saveGame(slot)) {
            System.exit(NORMAL_EXIT);
        }
    }

    /**
     * Asks the player to confirm, then goes back to the main menu.
     * Progress since the last save is lost, so the player is warned first.
     */
    private void confirmMainMenu() {
        int answer = JOptionPane.showConfirmDialog(this,
                "Go to the main menu? Progress since your last save will be lost.",
                "Main Menu", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            controller.showMainMenu();
        }
    }

    /**
     * Updates the title, the hero stats, and the inventory list from the game state.
     */
    private void refresh() {
        titleLabel.setText(gameState.getPartyName() + "  -  before encounter " + gameState.getEncounter()
                + " of " + EncounterFactory.LAST_ENCOUNTER);

        StringBuilder heroText = new StringBuilder();
        for (Hero hero : gameState.getHeroes().getMembers()) {
            heroText.append(describe(hero)).append("\n\n"); // blank line between heroes
        }
        heroArea.setText(heroText.toString());

        Inventory inventory = gameState.getInventory();
        String itemText = inventory.getItems().values().stream()
                .map(item -> item.getName() + " x" + inventory.getCount(item.getName()))
                .sorted()
                .collect(Collectors.joining("\n"));
        if (itemText.isEmpty()) {
            inventoryArea.setText(EMPTY_INVENTORY);
        } else {
            inventoryArea.setText(itemText);
        }
    }

    /**
     * Returns one hero's name, class, level, stats, and equipped items on three lines.
     *
     * @param hero the hero to describe
     * @return the hero's description
     */
    private String describe(Hero hero) {
        String weapon = NOTHING_EQUIPPED;
        if (hero.getWeapon() != null) {
            weapon = hero.getWeapon().getName();
        }
        String armor = NOTHING_EQUIPPED;
        if (hero.getArmor() != null) {
            armor = hero.getArmor().getName();
        }

        String knockedOut = "";
        if (hero.isKnockedOut()) {
            knockedOut = "  KO";
        }

        return hero.getName() + "  " + hero.getClassName() + "  Lv " + hero.getLevel() + knockedOut
                + "\nHP " + hero.getHp() + "/" + hero.getMaxHp()
                + "   ATK " + hero.getAttack() + "   DEF " + hero.getDefense() + "   SPD " + hero.getSpeed()
                + "\nWeapon: " + weapon + "   Armor: " + armor;
    }
}
