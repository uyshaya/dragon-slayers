package com.dragonslayers.view;

import com.dragonslayers.controller.GameController;
import com.dragonslayers.io.SaveManager;

import javax.swing.*;
import java.awt.*;

/**
 * The first screen. Shows the title and the New Game, Load Game, and Quit buttons.
 */
public class MainMenuPanel extends JPanel {

    private static final float TITLE_FONT_SIZE = 48f;

    private static final int TITLE_PADDING = 60;

    private static final int BUTTON_ROWS = 3;

    private static final int BUTTON_COLUMNS = 1;

    private static final int BUTTON_GAP = 10;

    private static final int NORMAL_EXIT = 0;

    /**
     * Creates the main menu.
     *
     * @param gameController the controller that handles each button
     */
    public MainMenuPanel(GameController gameController) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel(GameFrame.GAME_TITLE, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        title.setBorder(BorderFactory.createEmptyBorder(TITLE_PADDING, 0, TITLE_PADDING, 0));
        add(title, BorderLayout.NORTH);

        JButton newGameButton = new JButton("New Game");
        JButton loadButton = new JButton("Load Game");
        JButton quitButton = new JButton("Quit");

        newGameButton.addActionListener(e -> gameController.newGame());
        loadButton.addActionListener(e -> chooseSlotAndLoad(gameController));
        quitButton.addActionListener(e -> System.exit(NORMAL_EXIT));

        JPanel buttons = new JPanel(new GridLayout(BUTTON_ROWS, BUTTON_COLUMNS, 0, BUTTON_GAP));
        buttons.add(newGameButton);
        buttons.add(loadButton);
        buttons.add(quitButton);

        JPanel center = new JPanel();
        center.add(buttons);
        add(center, BorderLayout.CENTER);
    }

    /**
     * Asks the player for a save slot, then loads it.
     * Does nothing if the player cancels.
     *
     * @param gameController the controller that loads the game
     */
    private void chooseSlotAndLoad(GameController gameController) {
        Integer[] slots = new Integer[SaveManager.SLOT_COUNT];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = SaveManager.FIRST_SLOT + i;
        }

        Integer slot = (Integer) JOptionPane.showInputDialog(this, "Choose a save slot:", "Load Game",
                JOptionPane.QUESTION_MESSAGE, null, slots, slots[0]);
        if (slot != null) {
            gameController.loadGame(slot);
        }
    }
}
