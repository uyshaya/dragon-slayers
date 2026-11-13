package com.dragonslayers.model;

/**
 * Something that can get back hit points.
 */
public interface Healable {

    /**
     * Restores hit points.
     *
     * @param amount how many hit points to restore
     */
    void heal(int amount);

}
