package com.dragonslayers.model;

/**
 * A hero with a weak attack who supports the party.
 * <p>Ability: Heal, restores HP to one ally.</p>
 */
public class Cleric extends Hero {

    private static final int HEAL_AMOUNT = 30;

    /**
     * Creates a cleric with its starting stats from {@link GameBalance}.
     *
     * @param name the character's name
     */
    public Cleric(String name) {
        super(name,
                GameBalance.CLERIC_MAX_HP,
                GameBalance.CLERIC_ATTACK,
                GameBalance.CLERIC_DEFENSE,
                GameBalance.CLERIC_SPEED
        );
    }

    /**
     * Returns the display name of this class.
     *
     * @return the class name
     */
    @Override
    public String getClassName() {
        return "Cleric";
    }

    /**
     * Uses Heal on the target.
     *
     * @param target the character the ability is used on
     * @return HP restored
     */
    @Override
    public int useAbility(GameCharacter target) {
        int currentHp = target.getHp();
        target.heal(HEAL_AMOUNT);
        return target.getHp() - currentHp;
    }

    @Override
    public String getAbilityName() {
        return "Heal";
    }
}
