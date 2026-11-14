package com.dragonslayers.model;

/**
 * A story item. It cannot be used in battle but is kept in saves.
 */
public class KeyItem extends Item {

    /**
     * Creates a key item.
     *
     * @param name the item's name
     */
    public KeyItem(String name) {
        super(name);
    }

    /**
     * Returns this key item as one line for a text save file.
     *
     * @return the save line, for example {@code Item=KeyItem,Dragon Key}
     */
    @Override
    public String toSaveFormat() {
        return "Item=KeyItem," + getName();
    }
}
