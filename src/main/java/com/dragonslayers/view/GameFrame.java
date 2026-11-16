package com.dragonslayers.view;

import javax.swing.*;
import java.awt.*;

/**
 * The game window. Holds every screen in a CardLayout and shows one at a time.
 */
public class GameFrame extends JFrame {

    /**
     * Title shown on the window bar.
     */
    public static final String GAME_TITLE = "Dragon Slayers";

    /**
     * Window width in pixels.
     */
    private static final int WINDOW_WIDTH = 960;

    /**
     * Window height in pixels.
     */
    private static final int WINDOW_HEIGHT = 640;

    /**
     * The main menu screen.
     */
    public static final String MAIN_MENU = "mainMenu";

    /**
     * The character selection screen.
     */
    public static final String CHARACTER_SELECT = "characterSelect";

    /**
     * The battle screen.
     */
    public static final String BATTLE = "battle";

    /**
     * The inventory screen.
     */
    public static final String INVENTORY = "inventory";

    /**
     * The end screen.
     */
    public static final String END = "end";

    private final CardLayout cardLayout = new CardLayout();

    private final JPanel cards = new JPanel(cardLayout);

    /**
     * Constructs a new game frame.
     */
    public GameFrame() {
        super(GAME_TITLE); // window title
        setDefaultCloseOperation(EXIT_ON_CLOSE); // closing the window ends the program
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null); // center on screen
        setResizable(false);
        add(cards); // the card panel fills the window
    }

    /**
     * Adds a screen to the window.
     *
     * @param screen the panel to add
     * @param name   the screen's name, one of the screen constants
     */
    public void addScreen(JPanel screen, String name) {
        cards.add(screen, name);
    }

    /**
     * Shows a screen and hides the current one.
     *
     * @param name the screen to show
     */
    public void showScreen(String name) {
        cardLayout.show(cards, name);
    }

}
