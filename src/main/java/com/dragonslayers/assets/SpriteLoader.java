package com.dragonslayers.assets;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads the pixel-art sprites from the JAR and keeps each one in a cache.
 * <p>A sprite is read from disk only the first time it is asked for. After that it comes from the cache.</p>
 */
public final class SpriteLoader {

    private static final String SPRITE_FOLDER = "/assets/sprites/";

    private static final String FILE_EXTENSION = ".png";

    private static final int PLACEHOLDER_SIZE = 16;

    private static final Color PLACEHOLDER_COLOR = Color.MAGENTA;

    private static final Map<String, BufferedImage> CACHE = new HashMap<>();

    private SpriteLoader() {
    }

    /**
     * Returns a sprite by name. The name is turned into a file name in lower case with
     * underscores, so {@code "Dragon Boss"} loads {@code dragon_boss.png}.
     * <p>If the file is missing or cannot be read, a magenta square is returned instead, so the game keeps running.</p>
     *
     * @param name the sprite name, for example a class name such as {@code "Warrior"}
     * @return the sprite image
     */
    public static BufferedImage getSprite(String name) {
        String fileName = name.toLowerCase().replace(" ", "_");
        BufferedImage sprite = CACHE.get(fileName);
        if (sprite == null) {
            sprite = load(fileName);
            CACHE.put(fileName, sprite);
        }
        return sprite;
    }

    /**
     * Returns a sprite scaled up for a Swing label or button.
     * <p>{@code SCALE_REPLICATE} copies each pixel into a bigger square (nearest neighbour), so the pixels stay sharp.</p>
     *
     * @param name  the sprite name, as in {@link #getSprite(String)}
     * @param scale how many times bigger to draw it
     * @return the scaled sprite as an icon
     */
    public static ImageIcon getIcon(String name, int scale) {
        BufferedImage sprite = getSprite(name);
        Image scaled = sprite.getScaledInstance(sprite.getWidth() * scale, sprite.getHeight() * scale, Image.SCALE_REPLICATE);
        return new ImageIcon(scaled);
    }

    /**
     * Reads one sprite file from the JAR.
     *
     * @param fileName the file name without folder or extension
     * @return the image, or a placeholder if it cannot be loaded
     */
    private static BufferedImage load(String fileName) {
        URL url = SpriteLoader.class.getResource(SPRITE_FOLDER + fileName + FILE_EXTENSION);
        if (url == null) {
            System.err.println("Missing sprite: " + fileName);
            return createPlaceholder();
        }
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            System.err.println("Could not read sprite " + fileName + ": " + e.getMessage());
            return createPlaceholder();
        }
    }

    /**
     * Creates a plain magenta square to draw in place of a missing sprite.
     *
     * @return the placeholder image
     */
    private static BufferedImage createPlaceholder() {
        BufferedImage placeholder = new BufferedImage(PLACEHOLDER_SIZE, PLACEHOLDER_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = placeholder.createGraphics();
        g2.setColor(PLACEHOLDER_COLOR);
        g2.fillRect(0, 0, PLACEHOLDER_SIZE, PLACEHOLDER_SIZE);
        g2.dispose();
        return placeholder;
    }
}
