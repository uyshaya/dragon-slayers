package com.dragonslayers.model;

/**
 * The weakest enemy. Fast but fragile.
 * <p>Ability: Dirty Trick, cancels the target's defend, then attacks.</p>
 */
public class Goblin extends Enemy {

    /**
     * Creates a goblin with its starting stats from {@link GameBalance}.
     *
     * @param name the character's name
     */
    public Goblin(String name) {
        super(name,
                GameBalance.GOBLIN_MAX_HP,
                GameBalance.GOBLIN_ATTACK,
                GameBalance.GOBLIN_DEFENSE,
                GameBalance.GOBLIN_SPEED
        );
    }

    /**
     * Returns the display name of this class.
     *
     * @return the class name
     */
    @Override
    public String getClassName() {
        return "Goblin";
    }

    /**
     * Uses Dirty Trick on the target.
     *
     * @param target the character the ability is used on
     * @return the damage dealt
     */
    @Override
    public int useAbility(GameCharacter target) {
        target.stopDefending();
        return attack(target);
    }

    @Override
    public String getAbilityName() {
        return "Dirty Trick";
    }
}
