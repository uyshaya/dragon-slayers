package com.dragonslayers.model;

/**
 * A weapon that raises attack.
 */
public class Weapon extends Equipment {

    /**
     * Creates a weapon.
     *
     * @param name  the item's name
     * @param bonus how much it raises attack
     */
    public Weapon(String name, int bonus) {
        super(name, bonus);
    }

    /**
     * Equips this item on the target if it is a hero.
     *
     * @param target the hero to equip
     */
    @Override
    public void useOn(GameCharacter target) {
        if (target instanceof Hero hero) {
            hero.equipWeapon(this);
        }
    }

    /**
     * Returns this item as one line for a text save file.
     *
     * @return the save line, for example {@code Item=Weapon,Iron Sword,5}
     */
    @Override
    public String toSaveFormat() {
        return "Item=Weapon," + getName() + "," + getBonus();
    }
}
