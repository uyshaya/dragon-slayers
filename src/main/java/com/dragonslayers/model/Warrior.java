package com.dragonslayers.model;

/**
 * A hero with high HP and defense.
 * <p>Ability: Power Strike, double damage to one enemy.</p>
 */
public class Warrior extends Hero {

    private static final int POWER_STRIKE_MULTIPLIER = 2;

    /**
     * Creates a warrior with its starting stats from {@link GameBalance}.
     *
     * @param name the character's name
     */
    public Warrior(String name) {
        super(name,
                GameBalance.WARRIOR_MAX_HP,
                GameBalance.WARRIOR_ATTACK,
                GameBalance.WARRIOR_DEFENSE,
                GameBalance.WARRIOR_SPEED
        );
    }

    /**
     * Returns the display name of this class.
     *
     * @return the class name
     */
    @Override
    public String getClassName() {
        return "Warrior";
    }

    /**
     * Uses Power Strike on the target.
     *
     * @param target the character the ability is used on
     * @return the damage dealt
     */
    @Override
    public int useAbility(GameCharacter target) {
        int damage = calculateDamage(target) * POWER_STRIKE_MULTIPLIER;
        target.takeDamage(damage);
        return damage;
    }

    @Override
    public String getAbilityName() {
        return "Power Strike";
    }
}
