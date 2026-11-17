package com.dragonslayers.view;

import com.dragonslayers.controller.GameController;
import com.dragonslayers.model.GameState;
import com.dragonslayers.model.Hero;
import com.dragonslayers.model.Inventory;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

/**
 * The inventory screen.
 */
public class InventoryPanel extends JPanel {

    private static final int PADDING = 10;

    private static final int INVENTORY_WIDTH = 360;

    private static final float TITLE_FONT_SIZE = 20f;

    private static final String NOTHING_EQUIPPED = "none";

    private static final String EMPTY_INVENTORY = "(empty)";

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

        heroArea.setEditable(false);
        JScrollPane partyScroll = new JScrollPane(heroArea);
        partyScroll.setBorder(BorderFactory.createTitledBorder("Party"));
        add(partyScroll, BorderLayout.CENTER);

        inventoryArea.setEditable(false);
        inventoryArea.setLineWrap(true);
        inventoryArea.setWrapStyleWord(true);
        JScrollPane inventoryScroll = new JScrollPane(inventoryArea);
        inventoryScroll.setBorder(BorderFactory.createTitledBorder("Inventory"));
        inventoryScroll.setPreferredSize(new Dimension(INVENTORY_WIDTH, 0));
        add(inventoryScroll, BorderLayout.EAST);
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
     * Updates the title, the hero stats, and the inventory list from the game state.
     */
    private void refresh() {
        titleLabel.setText("Before encounter " + gameState.getEncounter());

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
