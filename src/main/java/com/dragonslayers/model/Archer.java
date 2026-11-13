package com.dragonslayers.model;

/**
 * A fast hero with moderate stats.
 * <p>Ability: Double Shot, attacks one enemy twice.</p>
 */
public class Archer extends Hero {

    private static final int DOUBLE_SHOT_HITS = 2;

    /**
     * Creates an archer with its starting stats from {@link GameBalance}.
     *
     * @param name the character's name
     */
    public Archer(String name) {
        super(name,
                GameBalance.ARCHER_MAX_HP,
                GameBalance.ARCHER_ATTACK,
                GameBalance.ARCHER_DEFENSE,
                GameBalance.ARCHER_SPEED
        );
    }

    /**
     * Returns the display name of this class.
     *
     * @return the class name
     */
    @Override
    public String getClassName() {
        return "Archer";
    }

    /**
     * Uses Double Shot on the target.
     *
     * @param target the character the ability is used on
     * @return the damage dealt
     */
    @Override
    public int useAbility(GameCharacter target) {
        int attackCount = 1, damage = 0;
        while (attackCount <= DOUBLE_SHOT_HITS && !target.isKnockedOut()) {
            damage += attack(target);
            attackCount++;
        }
        return damage;
    }
}
