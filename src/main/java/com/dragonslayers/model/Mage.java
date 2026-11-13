package com.dragonslayers.model;

/**
 * A hero with low HP and a strong attack.
 * <p>Ability: Fireball, ignores the target's defense.</p>
 */
public class Mage extends Hero {

    /**
     * Creates a mage with its starting stats from {@link GameBalance}.
     *
     * @param name the character's name
     */
    public Mage(String name) {
        super(name,
                GameBalance.MAGE_MAX_HP,
                GameBalance.MAGE_ATTACK,
                GameBalance.MAGE_DEFENSE,
                GameBalance.MAGE_SPEED
        );
    }

    /**
     * Returns the display name of this class.
     *
     * @return the class name
     */
    @Override
    public String getClassName() {
        return "Mage";
    }

    /**
     * Uses Fireball.
     *
     * @param target the character the ability is used on
     * @return the damage dealt
     */
    @Override
    public int useAbility(GameCharacter target) {
        int damage = getAttack() + rollRandomDamage();
        target.takeDamage(damage);
        return damage;
    }
}
