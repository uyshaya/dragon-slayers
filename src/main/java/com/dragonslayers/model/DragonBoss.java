package com.dragonslayers.model;

/**
 * The final boss, much stronger than any other enemy.
 * <p>Ability: Flame Breath, triple damage to hero.</p>
 */
public class DragonBoss extends Enemy {

    private static final int FLAME_BREATH_MULTIPLIER = 3;

    private static final int FLAME_BREATH_TURN = 3;

    private int turnCount = 0;

    /**
     * Creates a dragon boss with its starting stats from {@link GameBalance}.
     *
     * @param name the character's name
     */
    public DragonBoss(String name) {
        super(name,
                GameBalance.DRAGON_BOSS_MAX_HP,
                GameBalance.DRAGON_BOSS_ATTACK,
                GameBalance.DRAGON_BOSS_DEFENSE,
                GameBalance.DRAGON_BOSS_SPEED
        );
    }

    /**
     * Returns the display name of this class.
     *
     * @return the class name
     */
    @Override
    public String getClassName() {
        return "Dragon Boss";
    }

    /**
     * Uses Flame Breath.
     *
     * @param target the character the ability is used on
     * @return the damage dealt
     */
    @Override
    public int useAbility(GameCharacter target) {
        int damage = calculateDamage(target) * FLAME_BREATH_MULTIPLIER;
        target.takeDamage(damage);
        return damage;
    }

    @Override
    public String getAbilityName() {
        return "Flame Breath";
    }

    /**
     * Uses Flame Breath every third turn.
     *
     * @return true on every third turn
     */
    @Override
    protected boolean shouldUseAbility() {
        turnCount++;

        return turnCount % FLAME_BREATH_TURN == 0;
    }
}
