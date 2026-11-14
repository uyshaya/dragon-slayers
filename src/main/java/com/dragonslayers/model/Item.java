package com.dragonslayers.model;

/**
 * Something the party can carry in its inventory.
 */
public abstract class Item implements Saveable {

    private final String name;

    /**
     * Creates an item with the given name.
     *
     * @param name the item's name
     */
    protected Item(String name) {
        this.name = name;
    }

    /**
     * Returns the item's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
