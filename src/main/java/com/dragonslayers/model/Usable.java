package com.dragonslayers.model;

/**
 * An item that can be used on a character.
 */
public interface Usable {

    /**
     * Uses this item on the target.
     *
     * @param target the character the item is used on
     */
    void useOn(GameCharacter target);

}
