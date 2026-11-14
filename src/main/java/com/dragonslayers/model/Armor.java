package com.dragonslayers.model;

import com.dragonslayers.exceptions.InvalidActionException;

/**
 * Armor that raises defense.
 */
public class Armor extends Equipment {

    /**
     * Creates armor.
     *
     * @param name  the item's name
     * @param bonus how much it raises defense
     */
    public Armor(String name, int bonus) {
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
            hero.equipArmor(this);
        } else {
            throw new InvalidActionException("Only heroes can equip " +getName());
        }
    }

    /**
     * Returns this item as one line for a text save file.
     *
     * @return the save line, for example {@code Item=Armor,Leather,3}
     */
    @Override
    public String toSaveFormat() {
        return "Item=Armor," + getName() + "," + getBonus();
    }
}
