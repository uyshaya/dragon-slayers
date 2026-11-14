package com.dragonslayers.model;

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
     * Equips this item on the target if it is a hero.
     *
     * @param target the hero to equip
     */
    @Override
    public void useOn(GameCharacter target) {
        if (target instanceof Hero hero) {
            hero.equipArmor(this);
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
