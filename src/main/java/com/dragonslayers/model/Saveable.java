package com.dragonslayers.model;

/**
 * Something that can be written to a text save file.
 */
public interface Saveable {

    /**
     * Returns this object as one line of plain text for a save file.
     *
     * @return the save line
     */
    String toSaveFormat();

}
