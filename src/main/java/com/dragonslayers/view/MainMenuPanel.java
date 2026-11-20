package com.dragonslayers.view;

import com.dragonslayers.assets.SpriteLoader;
import com.dragonslayers.controller.GameController;
import com.dragonslayers.io.SaveManager;

import javax.swing.*;
import java.awt.*;

/**
 * The first screen. Shows the title and the New Game, Load Game, and Quit buttons
 * on top of a pixel-art dungeon background.
 */
public class MainMenuPanel extends BackgroundPanel {

    private static final float TITLE_FONT_SIZE = 48f;

    private static final int TITLE_GAP = 30;

    private static final String TITLE_SPRITE = "Dragon Boss";

    private static final int TITLE_SPRITE_SCALE = 10;

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
        setLayout(new GridBagLayout()); // keeps its one child, the content panel, in the middle of the screen

        JLabel title = new JLabel(GameFrame.GAME_TITLE, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        title.setForeground(TEXT_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, TITLE_GAP, 0)); // space between title and buttons
        title.setIcon(SpriteLoader.getIcon(TITLE_SPRITE, TITLE_SPRITE_SCALE));
        title.setHorizontalTextPosition(SwingConstants.CENTER); // text centered under the picture
        title.setVerticalTextPosition(SwingConstants.BOTTOM);

        JButton newGameButton = new JButton("New Game");
        JButton loadButton = new JButton("Load Game");
        JButton quitButton = new JButton("Quit");

        newGameButton.addActionListener(e -> gameController.newGame());
        loadButton.addActionListener(e -> chooseSlotAndLoad(gameController));
        quitButton.addActionListener(e -> System.exit(NORMAL_EXIT));

        JPanel buttons = new JPanel(new GridLayout(BUTTON_ROWS, BUTTON_COLUMNS, 0, BUTTON_GAP));
        buttons.setOpaque(false); // see-through, so the background shows around the buttons
        buttons.add(newGameButton);
        buttons.add(loadButton);
        buttons.add(quitButton);

        JPanel center = new JPanel(); // FlowLayout keeps the buttons at their normal width
        center.setOpaque(false);
        center.add(buttons);

        JPanel content = new JPanel(new BorderLayout()); // the title, with the buttons under it
        content.setOpaque(false);
        content.add(title, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        add(content);
    }

    /**
     * Asks the player for a save slot and a save format, then loads it.
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
        if (slot == null) {
            return; // the player pressed Cancel
        }

        String[] formats = {GameController.BINARY_FORMAT, GameController.TEXT_FORMAT};
        String format = (String) JOptionPane.showInputDialog(this, "Choose a save format:", "Load Game",
                JOptionPane.QUESTION_MESSAGE, null, formats, formats[0]);
        if (format != null) {
            gameController.loadGame(slot, format);
        }
    }
}
