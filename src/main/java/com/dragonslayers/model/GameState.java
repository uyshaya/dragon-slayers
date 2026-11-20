package com.dragonslayers.model;

import java.io.Serializable;

/**
 * Everything a save file holds: the party name, the hero party, the inventory, the next encounter,
 * and how many battle rounds the party has used so far.
 */
public class GameState implements Serializable {

    /**
     * The number of the first encounter, where a new game starts.
     */
    public static final int FIRST_ENCOUNTER = 1;

    private static final int NO_ROUNDS = 0;

    private final String partyName;

    private final Party<Hero> heroes;

    private final Inventory inventory;

    private int encounter;

    private int totalRounds;

    /**
     * Creates a game state at the given encounter. Used when loading a save.
     *
     * @param partyName   the name the player gave the party
     * @param heroes      the hero party
     * @param inventory   the party's shared inventory
     * @param encounter   the next encounter number
     * @param totalRounds the battle rounds used so far
     */
    public GameState(String partyName, Party<Hero> heroes, Inventory inventory, int encounter, int totalRounds) {
        this.partyName = partyName;
        this.heroes = heroes;
        this.inventory = inventory;
        this.encounter = encounter;
        this.totalRounds = totalRounds;
    }

    /**
     * Creates a game state for a new game, starting at the first encounter with no rounds used.
     *
     * @param partyName the name the player gave the party
     * @param heroes    the hero party
     * @param inventory the party's shared inventory
     */
    public GameState(String partyName, Party<Hero> heroes, Inventory inventory) {
        this(partyName, heroes, inventory, FIRST_ENCOUNTER, NO_ROUNDS);
    }

    /**
     * Returns the name the player gave the party.
     *
     * @return the party name
     */
    public String getPartyName() {
        return partyName;
    }

    /**
     * Returns how many battle rounds the party has used in all its won battles.
     * Fewer rounds means a faster win.
     *
     * @return the total rounds
     */
    public int getTotalRounds() {
        return totalRounds;
    }

    /**
     * Adds the rounds of a won battle to the total.
     *
     * @param rounds the rounds the battle took
     */
    public void addRounds(int rounds) {
        totalRounds += rounds;
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
