package com.dragonslayers.model;

/**
 * An item a hero can equip to raise a stat.
 */
public abstract class Equipment extends Item implements Usable {

    private final int bonus;

    /**
     * Creates equipment with the given name and bonus.
     *
     * @param name  the item's name
     * @param bonus how much it raises the stat
     */
    protected Equipment(String name, int bonus) {
        super(name);
        this.bonus = bonus;
    }

    /**
     * Returns how much this item raises the stat.
     *
     * @return the bonus
     */
    public int getBonus() {
        return bonus;
    }
}
