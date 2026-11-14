package com.dragonslayers.model;

/**
 * A consumable that restores hit points.
 */
public class Potion extends Item implements Usable {

    private static final int HEAL_AMOUNT = 20;

    /**
     * Creates a potion.
     *
     * @param name the potion's name
     */
    public Potion(String name) {
        super(name);
    }

    /**
     * Heals the target.
     *
     * @param target the character to heal
     */
    @Override
    public void useOn(GameCharacter target) {
        target.heal(HEAL_AMOUNT);
    }

    /**
     * Returns this potion as one line for a text save file.
     *
     * @return the save line, for example {@code Item=Potion,Small Potion}
     */
    @Override
    public String toSaveFormat() {
        return "Item=Potion," + getName();
    }
}
