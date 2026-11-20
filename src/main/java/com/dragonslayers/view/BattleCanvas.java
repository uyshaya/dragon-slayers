package com.dragonslayers.view;

import com.dragonslayers.assets.SpriteLoader;
import com.dragonslayers.model.Battle;
import com.dragonslayers.model.GameCharacter;
import com.dragonslayers.model.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
 * <p>Living characters gently bob up and down while idle, driven by a second Timer.</p>
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

    private static final Color ENEMY_TARGET_COLOR = new Color(230, 60, 60); // red: an enemy you can hit

    private static final Color ALLY_TARGET_COLOR = new Color(80, 220, 100); // green: an ally you can help

    private static final int TARGET_OUTLINE_THICKNESS = 3;

    private static final int FRAME_DELAY_MS = 20;

    private static final int EFFECT_FRAMES = 40;

    private static final int EFFECT_SCALE = 5;

    private static final int IDLE_DELAY_MS = 50;

    private static final int BOB_HEIGHT = 2; // how many pixels a sprite moves up and down

    private static final int ACTIVE_BOB_HEIGHT = 5; // a bigger bounce for the hero whose turn it is

    private static final double BOB_SPEED = 0.15; // how far the bob moves on each tick, in radians

    private static final double BOB_ROW_DELAY = 1.0; // each row starts a little behind the one above

    private final Timer effectTimer = new Timer(FRAME_DELAY_MS, e -> nextEffectFrame());

    private Battle battle;

    private Hero currentHero;

    private BufferedImage effectSprite; // null when no projectile is flying

    private Point effectStart;

    private Point effectEnd;

    private int effectFrame;

    private final Timer idleTimer = new Timer(IDLE_DELAY_MS, e -> nextIdleTick());

    private int idleTick;

    private List<? extends GameCharacter> targets = List.of(); // characters the player can click right now

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
        addMouseMotionListener(new MouseMotionAdapter() {
            /**
             * Shows a hand cursor while the mouse is over a character the player can click.
             *
             * @param e the mouse movement
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                updateCursor(e.getX(), e.getY());
            }
        });
    }

    /**
     * Sets which characters the player can click right now. They get a colored outline:
     * red for enemies, green for allies. An empty list means the player is not choosing a target.
     *
     * @param targets the characters that can be clicked
     */
    public void setTargets(List<? extends GameCharacter> targets) {
        this.targets = targets;
        if (targets.isEmpty()) {
            setCursor(Cursor.getDefaultCursor());
        }
        repaint();
    }

    /**
     * Returns the character whose plate is at the given point, for example where the player clicked.
     *
     * @param x the x position inside the canvas
     * @param y the y position inside the canvas
     * @return the character there, or null if the point is not on any plate
     */
    public GameCharacter getCharacterAt(int x, int y) {
        if (battle == null) {
            return null;
        }
        for (GameCharacter character : battle.getHeroes().getMembers()) {
            if (getPlateBounds(character).contains(x, y)) {
                return character;
            }
        }
        for (GameCharacter character : battle.getEnemies().getMembers()) {
            if (getPlateBounds(character).contains(x, y)) {
                return character;
            }
        }
        return null;
    }

    /**
     * Shows a hand cursor over a character that can be clicked, and the normal arrow everywhere else.
     *
     * @param x the mouse's x position
     * @param y the mouse's y position
     */
    private void updateCursor(int x, int y) {
        GameCharacter character = getCharacterAt(x, y);
        if (character != null && targets.contains(character)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Returns the rectangle of a character's plate: the sprite plus the text beside it, with padding.
     * Drawing the plate, drawing the target outline, and checking clicks all use this rectangle.
     *
     * @param character the character
     * @return the plate's rectangle inside the canvas
     */
    private Rectangle getPlateBounds(GameCharacter character) {
        Point position = getSpritePosition(character);
        BufferedImage sprite = SpriteLoader.getSprite(character.getClassName());
        int width = sprite.getWidth() * SPRITE_SCALE;
        int height = sprite.getHeight() * SPRITE_SCALE;
        return new Rectangle(position.x - PLATE_PADDING, position.y - PLATE_PADDING,
                width + TEXT_GAP + PLATE_TEXT_WIDTH + 2 * PLATE_PADDING, height + 2 * PLATE_PADDING);
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
        if (!idleTimer.isRunning()) {
            idleTimer.start(); // keeps the idle bob going for as long as the game runs
        }
        repaint(); // asks Swing to call paintComponent soon
    }

    /**
     * Moves the idle bob one step and redraws. Called by the idle timer on every tick.
     */
    private void nextIdleTick() {
        idleTick++;
        repaint();
    }

    /**
     * Returns how far a sprite is moved up or down right now, for the idle bob.
     * <p>{@code Math.sin} goes smoothly between -1 and 1 as the tick count grows,
     * so the sprite moves smoothly up and down. The hero whose turn it is bounces higher
     * ({@code ACTIVE_BOB_HEIGHT}) than everyone else ({@code BOB_HEIGHT}). Knocked-out characters stay still.</p>
     *
     * @param character the character to move
     * @param row       the character's row, so each row bobs a little after the one above
     * @return the vertical offset in pixels
     */
    private int getBobOffset(GameCharacter character, int row) {
        if (character.isKnockedOut()) {
            return 0;
        }
        int height = (character == currentHero) ? ACTIVE_BOB_HEIGHT : BOB_HEIGHT;
        double angle = idleTick * BOB_SPEED + row * BOB_ROW_DELAY;
        return (int) Math.round(Math.sin(angle) * height);
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
        for (int row = 0; row < characters.size(); row++) {
            GameCharacter character = characters.get(row);
            Point position = getSpritePosition(character);
            drawCharacter(g2, character, position.x, position.y, faceLeft, getBobOffset(character, row));
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
     * @param bob       how many pixels to move only the sprite up or down, for the idle animation
     */
    private void drawCharacter(Graphics2D g2, GameCharacter character, int x, int y, boolean faceLeft, int bob) {
        BufferedImage sprite = SpriteLoader.getSprite(character.getClassName());
        int width = sprite.getWidth() * SPRITE_SCALE;
        int height = sprite.getHeight() * SPRITE_SCALE;

        // a dark rounded plate behind the sprite and its text, so the character stands out from the background
        Rectangle plate = getPlateBounds(character);
        g2.setColor(PLATE_COLOR);
        g2.fillRoundRect(plate.x, plate.y, plate.width, plate.height, PLATE_CORNER, PLATE_CORNER);
        if (targets.contains(character)) { // the player can click this character right now
            g2.setColor(character instanceof Hero ? ALLY_TARGET_COLOR : ENEMY_TARGET_COLOR);
            g2.setStroke(new BasicStroke(TARGET_OUTLINE_THICKNESS));
            g2.drawRoundRect(plate.x, plate.y, plate.width, plate.height, PLATE_CORNER, PLATE_CORNER);
        }

        Composite normal = g2.getComposite();
        if (character.isKnockedOut()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, KNOCKED_OUT_ALPHA)); // see-through
        }
        int spriteY = y + bob; // only the sprite bobs, the plate and text stay still
        if (faceLeft) {
            g2.drawImage(sprite, x + width, spriteY, -width, height, null); // a negative width draws the image mirrored
        } else {
            g2.drawImage(sprite, x, spriteY, width, height, null);
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
        if (character instanceof Hero hero) { // only heroes have limited ability uses
            g2.drawString(hero.getAbilityName() + " " + hero.getAbilityUsesLeft() + "/" + hero.getMaxAbilityUses(),
                    textX, barY + BAR_HEIGHT + 2 * LINE_HEIGHT);
        }
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
