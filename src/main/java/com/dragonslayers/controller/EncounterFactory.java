package com.dragonslayers.controller;

import com.dragonslayers.model.*;

/**
 * Creates the enemy group for each of the five encounters.
 * <p>The encounters get harder in order, and the last one is the boss.</p>
 */
public final class EncounterFactory {

    /**
     * The number of the last encounter, the boss fight.
     */
    public static final int LAST_ENCOUNTER = 5;

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
}
