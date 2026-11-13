package com.dragonslayers.model;

public interface Saveable {

    /**
     * Serves as a marker for text persistence
     *
     * @return the save formatted text
     */
    String toSaveFormat();

}
