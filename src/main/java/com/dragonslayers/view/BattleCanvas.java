package com.dragonslayers.view;

import com.dragonslayers.assets.SpriteLoader;
import com.dragonslayers.model.Battle;
import com.dragonslayers.model.GameCharacter;
import com.dragonslayers.model.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Draws the battle scene with Graphics2D: a tiled floor, the heroes on the left, and the enemies on the right.
 * <p>Each character has its sprite, its name, a health bar, and its HP. A knocked-out character is drawn faded,
 * and the hero whose turn it is has a gold frame. Enemies are mirrored so they face the heroes.
 * Sprites are scaled with nearest-neighbour interpolation so the pixels stay sharp.</p>
 */
public class BattleCanvas extends JPanel {

    private static final int SPRITE_SCALE = 6;

    private static final int MARGIN = 20;

    private static final int ROW_HEIGHT = 120;

    private static final int TEXT_GAP = 12;

    private static final int LINE_HEIGHT = 16;

    private static final int BAR_WIDTH = 120;

    private static final int BAR_HEIGHT = 10;

    private static final int BAR_GAP = 6;

    private static final int FRAME_THICKNESS = 3;

    private static final float KNOCKED_OUT_ALPHA = 0.35f;

    private static final String FLOOR_SPRITE = "floor";

    private static final Color TEXT_COLOR = Color.WHITE;

    private static final Color BAR_BACKGROUND = new Color(90, 20, 20);

    private static final Color BAR_FILL = new Color(60, 190, 80);

    private static final Color FRAME_COLOR = new Color(255, 215, 0);

    private Battle battle;

    private Hero currentHero;

    /**
     * Creates an empty canvas. The battle is given later by {@link #showBattle}.
     */
    public BattleCanvas() {
    }

    /**
     * Shows a battle and redraws the canvas.
     *
     * @param battle      the battle to draw
     * @param currentHero the hero whose turn it is, drawn with a frame
     */
    public void showBattle(Battle battle, Hero currentHero) {
        this.battle = battle;
        this.currentHero = currentHero;
        repaint(); // asks Swing to call paintComponent soon
    }

    /**
     * Draws the floor and both sides of the battle.
     *
     * @param g the graphics context Swing gives the panel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (battle == null) {
            return; // nothing to draw before the first battle
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        drawFloor(g2);
        drawSide(g2, battle.getHeroes().getMembers(), MARGIN, false);
        drawSide(g2, battle.getEnemies().getMembers(), getWidth() / 2 + MARGIN, true);
    }

    /**
     * Fills the whole canvas with the floor tile, row by row.
     *
     * @param g2 the graphics to draw with
     */
    private void drawFloor(Graphics2D g2) {
        BufferedImage tile = SpriteLoader.getSprite(FLOOR_SPRITE);
        int size = tile.getWidth() * SPRITE_SCALE;
        for (int y = 0; y < getHeight(); y += size) {
            for (int x = 0; x < getWidth(); x += size) {
                g2.drawImage(tile, x, y, size, size, null);
            }
        }
    }

    /**
     * Draws one side of the battle as a column, one character per row.
     *
     * @param g2         the graphics to draw with
     * @param characters the characters on this side
     * @param x          the left edge of the column
     * @param faceLeft   true to mirror the sprites so they face left
     */
    private void drawSide(Graphics2D g2, List<? extends GameCharacter> characters, int x, boolean faceLeft) {
        int y = MARGIN;
        for (GameCharacter character : characters) {
            drawCharacter(g2, character, x, y, faceLeft);
            y += ROW_HEIGHT;
        }
    }

    /**
     * Draws one character: the sprite, then the name, health bar, and HP to its right.
     *
     * @param g2        the graphics to draw with
     * @param character the character to draw
     * @param x         the left edge of the sprite
     * @param y         the top edge of the sprite
     * @param faceLeft  true to mirror the sprite so it faces left
     */
    private void drawCharacter(Graphics2D g2, GameCharacter character, int x, int y, boolean faceLeft) {
        BufferedImage sprite = SpriteLoader.getSprite(character.getClassName());
        int width = sprite.getWidth() * SPRITE_SCALE;
        int height = sprite.getHeight() * SPRITE_SCALE;

        Composite normal = g2.getComposite();
        if (character.isKnockedOut()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, KNOCKED_OUT_ALPHA)); // see-through
        }
        if (faceLeft) {
            g2.drawImage(sprite, x + width, y, -width, height, null); // a negative width draws the image mirrored
        } else {
            g2.drawImage(sprite, x, y, width, height, null);
        }
        g2.setComposite(normal);

        if (character == currentHero) {
            g2.setColor(FRAME_COLOR);
            g2.setStroke(new BasicStroke(FRAME_THICKNESS));
            g2.drawRect(x, y, width, height);
        }

        int textX = x + width + TEXT_GAP;
        int barY = y + LINE_HEIGHT + BAR_GAP;
        g2.setColor(TEXT_COLOR);
        g2.drawString(character.getName() + " (" + character.getClassName() + ")", textX, y + LINE_HEIGHT);
        drawHealthBar(g2, character, textX, barY);
        g2.setColor(TEXT_COLOR);
        g2.drawString(describeState(character), textX, barY + BAR_HEIGHT + LINE_HEIGHT);
    }

    /**
     * Draws a health bar: a dark red background with a green part for the HP left.
     *
     * @param g2        the graphics to draw with
     * @param character the character whose HP to show
     * @param x         the left edge of the bar
     * @param y         the top edge of the bar
     */
    private void drawHealthBar(Graphics2D g2, GameCharacter character, int x, int y) {
        int filled = BAR_WIDTH * character.getHp() / character.getMaxHp();
        g2.setColor(BAR_BACKGROUND);
        g2.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);
        g2.setColor(BAR_FILL);
        g2.fillRect(x, y, filled, BAR_HEIGHT);
    }

    /**
     * Returns the HP text, plus KO or Defending when they apply.
     *
     * @param character the character to describe
     * @return for example {@code "35/50 HP  Defending"}
     */
    private String describeState(GameCharacter character) {
        String state = character.getHp() + "/" + character.getMaxHp() + " HP";
        if (character.isKnockedOut()) {
            state += "  KO";
        }
        if (character.isDefending()) {
            state += "  Defending";
        }
        return state;
    }
}
