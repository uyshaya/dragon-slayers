package com.dragonslayers.model;

/**
 * A slow enemy with good defense.
 * <p>Ability: Bone Guard, defends this turn.</p>
 */
public class Skeleton extends Enemy {

    /**
     * Creates a skeleton with its starting stats from {@link GameBalance}.
     *
     * @param name the character's name
     */
    public Skeleton(String name) {
        super(name,
                GameBalance.SKELETON_MAX_HP,
                GameBalance.SKELETON_ATTACK,
                GameBalance.SKELETON_DEFENSE,
                GameBalance.SKELETON_SPEED
        );
    }

    /**
     * Returns the display name of this class.
     *
     * @return the class name
     */
    @Override
    public String getClassName() {
        return "Skeleton";
    }

    /**
     * Uses Bone Guard.
     *
     * @param target not used, since Bone Guard only affects the Skeleton
     * @return always 0, since Bone Guard deals no damage
     */
    @Override
    public int useAbility(GameCharacter target) {
        defend();
        return 0;
    }

    @Override
    public String getAbilityName() {
        return "Bone Guard";
    }

    /**
     * Bone Guard protects the Skeleton itself, not a hero.
     *
     * @return always true
     */
    @Override
    protected boolean abilityTargetsSelf() {
        return true;
    }
}
