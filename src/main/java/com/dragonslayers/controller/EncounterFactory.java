package com.dragonslayers.controller;

import com.dragonslayers.model.*;

import java.util.List;

/**
 * Creates the enemy group and the reward for each of the five encounters.
 * <p>The encounters get harder in order, and the last one is the boss.</p>
 */
public final class EncounterFactory {

    /**
     * The number of the last encounter, the boss fight.
     */
    public static final int LAST_ENCOUNTER = 5;

    private static final String CLOTH_ARMOR_NAME = "Cloth Armor";

    private static final int CLOTH_ARMOR_DEFENSE_BONUS = 1;

    private static final String SWORD_NAME = "Iron Sword";

    private static final int SWORD_ATTACK_BONUS = 5;

    private static final String PADDED_ARMOR_NAME = "Padded Armor";

    private static final int PADDED_ARMOR_DEFENSE_BONUS = 3;

    private static final String LEATHER_ARMOR_NAME = "Leather Armor";

    private static final int LEATHER_ARMOR_DEFENSE_BONUS = 5;

    /**
     * The name of the key item won by defeating the dragon. A save that holds it is a finished game.
     */
    public static final String DRAGON_KEY_NAME = "Dragon Key";

    private EncounterFactory() {
    }

    /**
     * Creates the enemies for an encounter.
     *
     * @param encounter the encounter number, from 1 to {@link #LAST_ENCOUNTER}
     * @return a new enemy party
     * @throws IllegalArgumentException if there is no such encounter
     */
    public static Party<Enemy> create(int encounter) {
        Party<Enemy> enemies = new Party<>();
        switch (encounter) {
            case 1 -> {
                enemies.addMember(new Goblin("Gobly"));
                enemies.addMember(new Goblin("Goblier"));
            }
            case 2 -> {
                enemies.addMember(new Goblin("Gobly"));
                enemies.addMember(new Skeleton("Skely"));
            }
            case 3 -> {
                enemies.addMember(new Skeleton("Skely"));
                enemies.addMember(new Skeleton("Skelier"));
            }
            case 4 -> {
                enemies.addMember(new Goblin("Gobly"));
                enemies.addMember(new Goblin("Goblier"));
                enemies.addMember(new Goblin("Gobliest"));
                enemies.addMember(new Skeleton("Skelier"));
            }
            case 5 -> enemies.addMember(new DragonBoss("Dragoy"));
            default -> throw new IllegalArgumentException("No encounter " + encounter);
        }
        return enemies;
    }

    /**
     * Creates the items the party finds after winning an encounter.
     * <p>Every encounter gives one item: 1 gives Cloth Armor, 2 an Iron Sword, 3 Padded Armor, and 4 Leather Armor.
     * Each new armor is better and replaces the old one when equipped. Defeating the dragon in encounter 5 gives
     * the Dragon Key, a key item that cannot be used but is kept in saves.</p>
     *
     * @param encounter the encounter that was won
     * @return the reward items, or an empty list if there is no reward
     */
    public static List<Item> createReward(int encounter) {
        return switch (encounter) {
            case 1 -> List.of(new Armor(CLOTH_ARMOR_NAME, CLOTH_ARMOR_DEFENSE_BONUS));
            case 2 -> List.of(new Weapon(SWORD_NAME, SWORD_ATTACK_BONUS));
            case 3 -> List.of(new Armor(PADDED_ARMOR_NAME, PADDED_ARMOR_DEFENSE_BONUS));
            case 4 -> List.of(new Armor(LEATHER_ARMOR_NAME, LEATHER_ARMOR_DEFENSE_BONUS));
            case 5 -> List.of(new KeyItem(DRAGON_KEY_NAME));
            default -> List.of(); // no reward for an unknown encounter
        };
    }
}
