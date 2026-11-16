package com.dragonslayers.view;

import com.dragonslayers.controller.BattleEngine;
import com.dragonslayers.controller.GameController;
import com.dragonslayers.exceptions.InvalidActionException;
import com.dragonslayers.model.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The battle screen. Shows the party, the enemies, the combat log, and the action buttons.
 * <p>The player acts with one hero at a time, in speed order. The enemies act after the last hero.</p>
 */
public class BattlePanel extends JPanel {

    private static final String ATTACK = "Attack";

    private static final String DEFEND = "Defend";

    private static final String ABILITY = "Ability";

    private static final String ITEM = "Item";

    private static final int PADDING = 10;

    private static final int LOG_WIDTH = 360;

    private static final float TURN_FONT_SIZE = 20f;

    private final GameController controller;

    private final JLabel turnLabel = new JLabel("", SwingConstants.CENTER);

    private final JTextArea statusArea = new JTextArea();

    private final JTextArea logArea = new JTextArea();

    private Battle battle;

    private BattleEngine engine;

    private Inventory inventory;

    /**
     * Creates the battle screen. The battle itself is given later by {@link #startBattle}.
     *
     * @param controller the controller to tell when a battle ends
     */
    public BattlePanel(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(PADDING, PADDING));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        turnLabel.setFont(turnLabel.getFont().deriveFont(Font.BOLD, TURN_FONT_SIZE));
        add(turnLabel, BorderLayout.NORTH);

        statusArea.setEditable(false);
        add(statusArea, BorderLayout.CENTER);

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Combat log"));
        logScroll.setPreferredSize(new Dimension(LOG_WIDTH, 0));
        add(logScroll, BorderLayout.EAST);

        ActionButtonListener listener = new ActionButtonListener();
        JPanel buttons = new JPanel();
        for (String action : List.of(ATTACK, ABILITY, DEFEND, ITEM)) {
            JButton button = new JButton(action);
            button.addActionListener(listener);
            buttons.add(button);
        }
        add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Shows a new battle on the screen.
     *
     * @param battle    the battle to show
     * @param engine    the engine that runs the battle
     * @param inventory the party's shared inventory, for the Item button
     */
    public void startBattle(Battle battle, BattleEngine engine, Inventory inventory) {
        this.battle = battle;
        this.engine = engine;
        this.inventory = inventory;
        refresh();
    }

    /**
     * Handles all four action buttons. The button's text says which action was chosen.
     */
    private class ActionButtonListener implements ActionListener {

        /**
         * Runs the chosen action for the current hero, then updates the screen.
         *
         * @param e the button click
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Hero hero = engine.getCurrentHero();
            try {
                switch (e.getActionCommand()) {
                    case ATTACK -> {
                        Enemy target = chooseTarget("Attack whom?", battle.getEnemies().getAliveMembers());
                        if (target != null) {
                            engine.heroAttack(hero, target);
                        }
                    }
                    case ABILITY -> {
                        GameCharacter target;
                        if (hero.abilityTargetsAllies()) {
                            target = chooseTarget(hero.getAbilityName() + " on whom?", battle.getHeroes().getAliveMembers());
                        } else {
                            target = chooseTarget(hero.getAbilityName() + " on whom?", battle.getEnemies().getAliveMembers());
                        }
                        if (target != null) {
                            engine.heroAbility(hero, target);
                        }
                    }
                    case DEFEND -> engine.heroDefend(hero);
                    case ITEM -> useItem(hero);
                    default -> throw new InvalidActionException("Unknown action: " + e.getActionCommand());
                }
            } catch (InvalidActionException ex) {
                JOptionPane.showMessageDialog(BattlePanel.this, ex.getMessage(),
                        "Cannot do that", JOptionPane.WARNING_MESSAGE);
            }
            refresh();
        }
    }

    /**
     * Asks the player to pick one character from a list.
     *
     * @param prompt  the question to show
     * @param targets the characters to choose from
     * @param <T>     the type of character, so the result needs no cast
     * @return the chosen character, or null if the player cancels
     */
    private <T extends GameCharacter> T chooseTarget(String prompt, List<T> targets) {
        String[] names = targets.stream()
                .map(t -> t.getName() + " (" + t.getHp() + "/" + t.getMaxHp() + " HP)")
                .toArray(String[]::new);
        String choice = (String) JOptionPane.showInputDialog(this, prompt, "Choose target",
                JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
        if (choice == null) {
            return null;
        }
        return targets.get(Arrays.asList(names).indexOf(choice));
    }

    /**
     * Asks the player for an item and a hero to use it on, then uses it.
     * Does nothing if the player cancels.
     *
     * @param hero the hero using the item
     */
    private void useItem(Hero hero) {
        List<String> itemNames = inventory.getUsableItemNames();
        if (itemNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No usable items");
            return;
        }

        String[] choices = itemNames.stream()
                .map(name -> name + " x" + inventory.getCount(name))
                .toArray(String[]::new);
        String choice = (String) JOptionPane.showInputDialog(this, "Use which item?", "Choose item",
                JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        if (choice == null) {
            return;
        }
        String itemName = itemNames.get(Arrays.asList(choices).indexOf(choice));

        Hero target = chooseTarget("Use " + itemName + " on whom?", battle.getHeroes().getAliveMembers());
        if (target != null) {
            engine.heroUseItem(hero, itemName, target);
        }
    }

    /**
     * Updates the log, the turn label, and the status. Tells the controller if the battle is over.
     */
    private void refresh() {
        logArea.setText(String.join("\n", battle.getLog()));
        logArea.setCaretPosition(logArea.getDocument().getLength()); // scroll to the newest line

        if (battle.isOver()) {
            controller.battleEnded(battle.isWon());
            return;
        }

        Hero hero = engine.getCurrentHero();
        turnLabel.setText("Round " + battle.getRound() + " - " + hero.getName() + "'s turn (" + hero.getClassName() + ")");
        statusArea.setText("HEROES\n" + describe(battle.getHeroes().getMembers())
                + "\n\nENEMIES\n" + describe(battle.getEnemies().getMembers()));
    }

    /**
     * Returns one line per character with its name, class, HP, and state.
     *
     * @param characters the characters to describe
     * @return the lines, joined with new lines
     */
    private String describe(List<? extends GameCharacter> characters) {
        return characters.stream()
                .map(c -> c.getName() + "  " + c.getClassName() + "  " + c.getHp() + "/" + c.getMaxHp() + " HP"
                        + (c.isKnockedOut() ? "  KO" : "")
                        + (c.isDefending() ? "  [defending]" : ""))
                .collect(Collectors.joining("\n"));
    }
}
