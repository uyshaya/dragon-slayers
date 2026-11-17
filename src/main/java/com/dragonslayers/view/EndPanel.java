package com.dragonslayers.view;

import com.dragonslayers.controller.GameController;

import javax.swing.*;
import java.awt.*;

/**
 * The last screen. Shows Victory when the boss is defeated, or Game Over when the whole party is knocked out.
 * <p>The Main Menu button goes back to the start.</p>
 */
public class EndPanel extends JPanel {

    private static final float TITLE_FONT_SIZE = 48f;

    private static final float MESSAGE_FONT_SIZE = 18f;

    private static final int PADDING = 60;

    private final JLabel titleLabel = new JLabel("", SwingConstants.CENTER);

    private final JLabel messageLabel = new JLabel("", SwingConstants.CENTER);

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
        add(titleLabel, BorderLayout.NORTH);
        add(messageLabel, BorderLayout.CENTER);

        JButton menuButton = new JButton("Main Menu");
        menuButton.addActionListener(e -> controller.showMainMenu());
        JPanel buttons = new JPanel();
        buttons.add(menuButton);
        add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Sets the title and message for how the game ended.
     *
     * @param won true if the party defeated the boss, false if the party was knocked out
     */
    public void showResult(boolean won) {
        if (won) {
            titleLabel.setText("Victory!");
            messageLabel.setText("The dragon is defeated. Your party saved the kingdom.");
        } else {
            titleLabel.setText("Game Over");
            messageLabel.setText("Your whole party was knocked out.");
        }
    }
}
