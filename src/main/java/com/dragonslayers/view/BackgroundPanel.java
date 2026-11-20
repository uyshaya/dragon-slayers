package com.dragonslayers.view;

import com.dragonslayers.assets.SpriteLoader;
import com.dragonslayers.io.SaveManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A screen with the pixel-art dungeon background behind its contents.
 * <p>Every screen extends this class, so all screens share the same background.
 * It is abstract because it is only a base for the real screens, not a screen of its own.</p>
 */
public abstract class BackgroundPanel extends JPanel {

    /**
     * The color for text drawn straight on the background, so it is easy to read on the dark picture.
     */
    protected static final Color TEXT_COLOR = Color.WHITE;

    private static final String BACKGROUND_SPRITE = "background";

    private static final Color BACKGROUND_SHADE = new Color(0, 0, 0, 110); // see-through black

    private final BufferedImage background = SpriteLoader.getSprite(BACKGROUND_SPRITE);

    /**
     * Creates the panel. Only subclasses call this.
     */
    protected BackgroundPanel() {
    }

    /**
     * Draws the background picture stretched over the whole panel, then darkens it a little
     * so text and buttons are easy to see. Swing draws the panel's buttons and labels on top.
     *
     * @param g the graphics context Swing gives the panel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        g2.setColor(BACKGROUND_SHADE);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Asks the player to pick a save slot. Used by every screen that saves or loads.
     *
     * @param title the dialog's title, for example {@code "Save Game"}
     * @return the chosen slot number, or null if the player cancels
     */
    protected Integer chooseSlot(String title) {
        Integer[] slots = new Integer[SaveManager.SLOT_COUNT];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = SaveManager.FIRST_SLOT + i;
        }
        return (Integer) JOptionPane.showInputDialog(this, "Choose a save slot:", title,
                JOptionPane.QUESTION_MESSAGE, null, slots, slots[0]);
    }

    /**
     * Puts a component in a scroll pane with a white title, so the title is readable on the background.
     *
     * @param content the component to scroll, for example a text area
     * @param title   the title shown on the border
     * @return the scroll pane
     */
    protected static JScrollPane createTitledScrollPane(Component content, String title) {
        JScrollPane scrollPane = new JScrollPane(content);
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(TEXT_COLOR);
        scrollPane.setBorder(border);
        scrollPane.setOpaque(false); // see-through around the text area, so the background shows behind the title
        return scrollPane;
    }
}
