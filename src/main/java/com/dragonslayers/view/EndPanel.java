package com.dragonslayers.view;

import com.dragonslayers.controller.GameController;

import javax.swing.*;
import java.awt.*;

/**
 * The last screen. Shows Victory when the boss is defeated, or Game Over when the whole party is knocked out.
 * <p>After a victory, Save Game saves the finished game, including the Dragon Key.
 * The Main Menu button goes back to the start.</p>
 */
public class EndPanel extends BackgroundPanel {

    private static final float TITLE_FONT_SIZE = 48f;

    private static final float MESSAGE_FONT_SIZE = 18f;

    private static final int PADDING = 60;

    private static final int MESSAGE_WIDTH = 600; // the message wraps onto more lines past this many pixels

    private final JLabel titleLabel = new JLabel("", SwingConstants.CENTER);

    private final JLabel messageLabel = new JLabel("", SwingConstants.CENTER);

    private final JButton saveButton = new JButton("Save Game");

    /**
     * Creates the end screen. The result is set later by {@link #showResult(boolean)}.
     *
     * @param controller the controller that goes back to the main menu
     */
    public EndPanel(GameController controller) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        messageLabel.setFont(messageLabel.getFont().deriveFont(MESSAGE_FONT_SIZE));
        titleLabel.setForeground(TEXT_COLOR);
        messageLabel.setForeground(TEXT_COLOR);
        add(titleLabel, BorderLayout.NORTH);
        add(messageLabel, BorderLayout.CENTER);

        JButton menuButton = new JButton("Main Menu");
        menuButton.addActionListener(e -> controller.showMainMenu());
        saveButton.addActionListener(e -> {
            Integer slot = chooseSlot("Save Game");
            if (slot != null) {
                controller.saveGame(slot);
            }
        });
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(saveButton);
        buttons.add(menuButton);
        add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Sets the title and message for how the game ended.
     * The Save Game button is only shown after a victory.
     *
     * @param won         true if the party defeated the boss, false if the party was knocked out
     * @param partyName   the party's name
     * @param totalRounds the battle rounds the party used, shown after a victory
     */
    public void showResult(boolean won, String partyName, int totalRounds) {
        if (won) {
            titleLabel.setText("Victory!");
            setMessage(partyName + " defeated the dragon in " + totalRounds
                    + " rounds and won the Dragon Key. The kingdom is saved.");
        } else {
            titleLabel.setText("Game Over");
            setMessage("All of " + partyName + " was knocked out.");
        }
        saveButton.setVisible(won); // a lost game has nothing new worth saving
    }

    /**
     * Shows the message centered and wrapped onto several lines if it is long.
     * <p>A JLabel can show simple HTML. A fixed width makes the text wrap instead of being cut off with "...".</p>
     *
     * @param message the message to show
     */
    private void setMessage(String message) {
        messageLabel.setText("<html><div style='text-align: center; width: " + MESSAGE_WIDTH + "px;'>"
                + message + "</div></html>");
    }
}
