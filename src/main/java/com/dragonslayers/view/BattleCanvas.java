package com.dragonslayers.view;

import com.dragonslayers.assets.SpriteLoader;
import com.dragonslayers.model.Battle;
import com.dragonslayers.model.GameCharacter;
import com.dragonslayers.model.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * Draws the battle scene with Graphics2D: a dungeon arena background, the heroes on the left,
 * and the enemies on the right.
 * <p>Each character has its sprite, its name, a health bar, and its HP. A knocked-out character is drawn faded,
 * and the hero whose turn it is has a gold frame. Enemies are mirrored so they face the heroes.
 * Sprites are scaled with nearest-neighbour interpolation so the pixels stay sharp.</p>
 * <p>It can also play short projectile animations, such as a fireball flying from a hero to an enemy
 * or a healing orb flying to an ally. Several can be added at once; they play one after another.
 * A {@link Timer} moves the projectile a little on every tick and repaints the canvas.</p>
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

    private static final Color TEXT_COLOR = Color.WHITE;

    private static final Color BAR_BACKGROUND = new Color(90, 20, 20);

    private static final Color BAR_FILL = new Color(60, 190, 80);

    private static final Color FRAME_COLOR = new Color(255, 215, 0);

    private static final String ARENA_SPRITE = "battle_background";

    private static final Color PLATE_COLOR = new Color(20, 15, 25, 200); // dark and mostly solid

    private static final int PLATE_PADDING = 8;

    private static final int PLATE_TEXT_WIDTH = 140;

    private static final int PLATE_CORNER = 16;

    private static final int FRAME_DELAY_MS = 20;

    private static final int EFFECT_FRAMES = 40;

    private static final int EFFECT_SCALE = 5;

    private final Timer effectTimer = new Timer(FRAME_DELAY_MS, e -> nextEffectFrame());

    private Battle battle;

    private Hero currentHero;

    private BufferedImage effectSprite; // null when no projectile is flying

    private Point effectStart;

    private Point effectEnd;

    private int effectFrame;

    private final Queue<Effect> waitingEffects = new ArrayDeque<>(); // first in, first out

    /**
     * One projectile animation: who shoots, who is hit, and which sprite flies.
     *
     * @param from       the character that shoots
     * @param to         the character that is hit
     * @param spriteName the projectile sprite name
     */
    private record Effect(GameCharacter from, GameCharacter to, String spriteName) {
    }

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
        if (battle != this.battle) {
            stopEffect(); // a projectile from the last battle should not appear in the new one
        }
        this.battle = battle;
        this.currentHero = currentHero;
        repaint(); // asks Swing to call paintComponent soon
    }

    /**
     * Adds a projectile animation from one character to another, for example a fireball.
     * It starts right away, or waits until the animations before it have finished.
     * <p>This is only for show. The damage is already done by the battle engine.</p>
     *
     * @param from       the character that shoots
     * @param to         the character that is hit
     * @param spriteName the projectile sprite, for example {@code "Fireball"} for {@code fireball.png}
     */
    public void playProjectile(GameCharacter from, GameCharacter to, String spriteName) {
        waitingEffects.add(new Effect(from, to, spriteName));
        if (!effectTimer.isRunning()) {
            startNextEffect(); // nothing is playing, so start now
        }
    }

    /**
     * Takes the oldest waiting animation and starts it. Does nothing if none are waiting.
     */
    private void startNextEffect() {
        Effect next = waitingEffects.poll(); // removes and returns the oldest one, or null if the queue is empty
        if (next == null) {
            return;
        }
        effectSprite = SpriteLoader.getSprite(next.spriteName());
        effectStart = getSpriteCenter(next.from());
        effectEnd = getSpriteCenter(next.to());
        effectFrame = 0;
        effectTimer.start();
    }

    /**
     * Moves the projectile one step. Called by the timer on every tick.
     * When it reaches the target, it is removed and the next waiting animation starts.
     */
    private void nextEffectFrame() {
        effectFrame++;
        if (effectFrame >= EFFECT_FRAMES) {
            effectTimer.stop();
            effectSprite = null;
            startNextEffect();
        }
        repaint();
    }

    /**
     * Stops the timer, removes the projectile, and drops any waiting animations.
     */
    private void stopEffect() {
        effectTimer.stop();
        effectSprite = null;
        waitingEffects.clear();
    }

    /**
     * Draws the arena, both sides of the battle, and any flying projectile.
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

        g2.drawImage(SpriteLoader.getSprite(ARENA_SPRITE), 0, 0, getWidth(), getHeight(), null); // stretched to fill the canvas
        drawSide(g2, battle.getHeroes().getMembers(), false);
        drawSide(g2, battle.getEnemies().getMembers(), true);
        drawEffect(g2);
    }

    /**
     * Draws one side of the battle, each character at its own position.
     *
     * @param g2         the graphics to draw with
     * @param characters the characters on this side
     * @param faceLeft   true to mirror the sprites so they face left
     */
    private void drawSide(Graphics2D g2, List<? extends GameCharacter> characters, boolean faceLeft) {
        for (GameCharacter character : characters) {
            Point position = getSpritePosition(character);
            drawCharacter(g2, character, position.x, position.y, faceLeft);
        }
    }

    /**
     * Returns where a character's sprite is drawn. Heroes are in the left column and enemies in the right one,
     * one row per character in party order.
     *
     * @param character the character to find
     * @return the top-left corner of the character's sprite
     */
    private Point getSpritePosition(GameCharacter character) {
        int row = battle.getHeroes().getMembers().indexOf(character);
        int x = MARGIN;
        if (row == -1) { // not a hero, so it is an enemy
            row = battle.getEnemies().getMembers().indexOf(character);
            x = getWidth() / 2 + MARGIN;
        }
        return new Point(x, MARGIN + row * ROW_HEIGHT);
    }

    /**
     * Returns the middle of a character's sprite, where a projectile starts or lands.
     *
     * @param character the character to find
     * @return the center point of the character's sprite
     */
    private Point getSpriteCenter(GameCharacter character) {
        Point position = getSpritePosition(character);
        BufferedImage sprite = SpriteLoader.getSprite(character.getClassName());
        int halfSize = sprite.getWidth() * SPRITE_SCALE / 2;
        return new Point(position.x + halfSize, position.y + halfSize);
    }

    /**
     * Draws the flying projectile, if there is one. Its position moves in a straight line
     * from the start to the end as the frame count goes up.
     *
     * @param g2 the graphics to draw with
     */
    private void drawEffect(Graphics2D g2) {
        if (effectSprite == null) {
            return;
        }
        double progress = (double) effectFrame / EFFECT_FRAMES; // 0.0 at the start, 1.0 at the target
        int centerX = (int) (effectStart.x + (effectEnd.x - effectStart.x) * progress);
        int centerY = (int) (effectStart.y + (effectEnd.y - effectStart.y) * progress);
        int size = effectSprite.getWidth() * EFFECT_SCALE;
        int left = centerX - size / 2;
        int top = centerY - size / 2;
        if (effectEnd.x < effectStart.x) {
            g2.drawImage(effectSprite, left + size, top, -size, size, null); // flying left, so mirror it
        } else {
            g2.drawImage(effectSprite, left, top, size, size, null);
        }
    }

    /**
     * Draws one character on a dark plate: the sprite, then the name, health bar, and HP to its right.
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

        // a dark rounded plate behind the sprite and its text, so the character stands out from the background
        g2.setColor(PLATE_COLOR);
        g2.fillRoundRect(x - PLATE_PADDING, y - PLATE_PADDING,
                width + TEXT_GAP + PLATE_TEXT_WIDTH + 2 * PLATE_PADDING, height + 2 * PLATE_PADDING,
                PLATE_CORNER, PLATE_CORNER);

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
