package com.dragonslayers.view;

import com.dragonslayers.controller.BattleEngine;
import com.dragonslayers.controller.EncounterFactory;
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

/**
 * The battle screen. Shows the party, the enemies, the combat log, and the action buttons.
 * <p>The player acts with one hero at a time, in speed order. The enemies act after the last hero.</p>
 */
public class BattlePanel extends BackgroundPanel {

    private static final String ATTACK = "Attack";

    private static final String DEFEND = "Defend";

    private static final String ABILITY = "Ability";

    private static final String ITEM = "Item";

    private static final String ATTACK_SPRITE = "attack";

    private static final int PADDING = 10;

    private static final int LOG_WIDTH = 360;

    private static final float TURN_FONT_SIZE = 20f;

    private static final int BUTTON_GROUP_GAP = 40;

    private final GameController controller;

    private final JLabel encounterLabel = new JLabel("", SwingConstants.LEFT);

    private final JLabel turnLabel = new JLabel("", SwingConstants.CENTER);

    private final JPanel headerBalance = new JPanel();

    private final BattleCanvas canvas = new BattleCanvas();

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

        encounterLabel.setFont(encounterLabel.getFont().deriveFont(Font.BOLD, TURN_FONT_SIZE));
        turnLabel.setFont(turnLabel.getFont().deriveFont(Font.BOLD, TURN_FONT_SIZE));
        encounterLabel.setForeground(TEXT_COLOR);
        turnLabel.setForeground(TEXT_COLOR);
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        headerBalance.setOpaque(false);
        header.add(encounterLabel, BorderLayout.WEST);
        header.add(turnLabel, BorderLayout.CENTER);
        header.add(headerBalance, BorderLayout.EAST); // empty, same width as the encounter label
        add(header, BorderLayout.NORTH);

        add(canvas, BorderLayout.CENTER);

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane logScroll = createTitledScrollPane(logArea, "Combat log");
        logScroll.setPreferredSize(new Dimension(LOG_WIDTH, 0));
        add(logScroll, BorderLayout.EAST);

        ActionButtonListener listener = new ActionButtonListener();
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        for (String action : List.of(ATTACK, ABILITY, DEFEND, ITEM)) {
            JButton button = new JButton(action);
            button.addActionListener(listener);
            buttons.add(button);
        }

        JButton menuButton = new JButton("Main Menu"); // not a battle action, so it has its own listener
        menuButton.addActionListener(e -> confirmMainMenu());
        buttons.add(Box.createHorizontalStrut(BUTTON_GROUP_GAP)); // empty space before the leave button
        buttons.add(menuButton);
        add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Shows a new battle on the screen.
     *
     * @param battle    the battle to show
     * @param engine    the engine that runs the battle
     * @param inventory the party's shared inventory, for the Item button
     * @param encounter the encounter number, shown at the top
     */
    public void startBattle(Battle battle, BattleEngine engine, Inventory inventory, int encounter) {
        this.battle = battle;
        this.engine = engine;
        this.inventory = inventory;
        encounterLabel.setText("Encounter " + encounter + " of " + EncounterFactory.LAST_ENCOUNTER);
        // an empty panel on the right as wide as the encounter label keeps the turn label centered on the screen
        headerBalance.setPreferredSize(encounterLabel.getPreferredSize());
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
            int roundBefore = battle.getRound();
            try {
                switch (e.getActionCommand()) {
                    case ATTACK -> {
                        Enemy target = chooseTarget("Attack whom?", battle.getEnemies().getAliveMembers());
                        if (target != null) {
                            engine.heroAttack(hero, target);
                            canvas.playProjectile(hero, target, ATTACK_SPRITE); // only for show, same for every hero and enemy
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
                            // only for show: the effect sprite is named after the ability, for example fireball.png
                            canvas.playProjectile(hero, target, hero.getAbilityName());
                        }
                    }
                    case DEFEND -> engine.heroDefend(hero);
                    case ITEM -> useItem(hero);
                    default -> throw new InvalidActionException("Unknown action: " + e.getActionCommand());
                }
                if (battle.getRound() > roundBefore) {
                    playEnemyTurns(); // the round went up, so the enemies acted after this hero
                }
            } catch (InvalidActionException ex) {
                JOptionPane.showMessageDialog(BattlePanel.this, ex.getMessage(),
                        "Cannot do that", JOptionPane.WARNING_MESSAGE);
            }
            refresh();
        }
    }

    /**
     * Plays an animation for each enemy that acted in the last enemy phase, one after another.
     * <p>A normal attack shows the attack sprite. An ability shows the sprite named after it,
     * for example {@code flame_breath.png}. Bone Guard has the Skeleton as its target, so it shows on the Skeleton.</p>
     */
    private void playEnemyTurns() {
        for (Enemy enemy : engine.getEnemiesThatActed()) {
            String spriteName = ATTACK_SPRITE;
            if (enemy.usedAbilityLastTurn()) {
                spriteName = enemy.getAbilityName();
            }
            canvas.playProjectile(enemy, enemy.getLastTarget(), spriteName);
        }
    }

    /**
     * Asks the player to confirm, then leaves the battle and goes back to the main menu.
     * The battle and any progress since the last save are lost, so the player is warned first.
     */
    private void confirmMainMenu() {
        int answer = JOptionPane.showConfirmDialog(this,
                "Leave the battle and go to the main menu? Progress since your last save will be lost.",
                "Main Menu", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            controller.showMainMenu();
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
     * Updates the log, the turn label, and the battle drawing. Tells the controller if the battle is over.
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
        canvas.showBattle(battle, hero);
    }
}
