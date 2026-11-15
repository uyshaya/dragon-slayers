package com.dragonslayers.model;

import java.io.Serializable;

/**
 * Everything a save file holds: the hero party, the inventory, and the next encounter.
 */
public class GameState implements Serializable {

    public static final int FIRST_ENCOUNTER = 1;

    private final Party<Hero> heroes;

    private final Inventory inventory;

    private int encounter;

    /**
     * Creates a game state at the given encounter. Used when loading a save.
     *
     * @param heroes    the hero party
     * @param inventory the party's shared inventory
     * @param encounter the next encounter number
     */
    public GameState(Party<Hero> heroes, Inventory inventory, int encounter) {
        this.heroes = heroes;
        this.inventory = inventory;
        this.encounter = encounter;
    }

    /**
     * Creates a game state for a new game, starting at the first encounter.
     *
     * @param heroes    the hero party
     * @param inventory the party's shared inventory
     */
    public GameState(Party<Hero> heroes, Inventory inventory) {
        this(heroes, inventory, FIRST_ENCOUNTER);
    }

    /**
     * Returns the hero party.
     *
     * @return the heroes
     */
    public Party<Hero> getHeroes() {
        return heroes;
    }

    /**
     * Returns the party's shared inventory.
     *
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Returns the next encounter number.
     *
     * @return the encounter number
     */
    public int getEncounter() {
        return encounter;
    }

    /**
     * Moves on to the next encounter, after a win.
     */
    public void nextEncounter() {
        encounter++;
    }
}
