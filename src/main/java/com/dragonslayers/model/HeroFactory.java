package com.dragonslayers.model;

import java.util.List;

/**
 * Creates heroes from their class name. Used by character select and the text save loader.
 */
public final class HeroFactory {

    /**
     * The hero classes the player can choose from.
     */
    public static final List<String> HERO_CLASSES = List.of("Warrior", "Mage", "Archer", "Cleric");

    private HeroFactory() {
    }

    /**
     * Creates a hero from its class name.
     *
     * @param className the class name, for example {@code Warrior}
     * @param name      the hero's name
     * @return the new hero
     * @throws IllegalArgumentException if the class name is unknown
     */
    public static Hero create(String className, String name) {
        return switch (className) {
            case "Archer" -> new Archer(name);
            case "Cleric" -> new Cleric(name);
            case "Mage" -> new Mage(name);
            case "Warrior" -> new Warrior(name);
            default -> throw new IllegalArgumentException("Unknown hero class: " + className);
        };
    }

}
