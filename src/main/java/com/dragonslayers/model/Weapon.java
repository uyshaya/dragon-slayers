package com.dragonslayers.model;

import com.dragonslayers.exceptions.InvalidActionException;

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
     * Equips this item on the target.
     *
     * @param target the hero to equip
     * @throws InvalidActionException if the target is not a hero
     */
    @Override
    public void useOn(GameCharacter target) {
        if (target instanceof Hero hero) {
            hero.equipWeapon(this);
        } else {
            throw new InvalidActionException("Only heroes can equip " +getName());
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
