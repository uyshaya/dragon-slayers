package com.dragonslayers.model;

/**
 * Something that can attack a character.
 */
public interface Attackable {

    /**
     * Attacks the target and lowers its HP.
     *
     * @param target the character being hit
     * @return the damage dealt
     */
    int attack(GameCharacter target);

}
