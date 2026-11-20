package com.dragonslayers.model;

import java.util.List;
import java.util.Random;

/**
 * A character controlled by the game.
 */
public abstract class Enemy extends GameCharacter {

    private static final int ABILITY_CHANCE = 3;

    private static final Random RANDOM = new Random();

    private GameCharacter lastTarget; // who this enemy's last turn affected, for the battle screen

    private boolean usedAbilityLastTurn;

    /**
     * Creates an enemy with the given stats.
     *
     * @param name    the enemy's name
     * @param maxHp   maximum hit points
     * @param attack  attack stat
     * @param defense defense stat
     * @param speed   speed stat
     */
    protected Enemy(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    /**
     * Decides whether to use the ability this turn.
     *
     * @return true to use the ability
     */
    protected boolean shouldUseAbility() {
        return RANDOM.nextInt(ABILITY_CHANCE) == 0;
    }

    /**
     * Says whether this enemy's ability affects the enemy itself instead of a hero.
     * Enemies that protect themselves override this to return true.
     *
     * @return false by default
     */
    protected boolean abilityTargetsSelf() {
        return false;
    }

    /**
     * Returns the character affected by this enemy's last turn: the hero it hit,
     * or the enemy itself if its ability targets itself.
     *
     * @return the last target, or null before the enemy's first turn
     */
    public GameCharacter getLastTarget() {
        return lastTarget;
    }

    /**
     * Says whether this enemy used its ability on its last turn.
     *
     * @return true for the ability, false for a normal attack
     */
    public boolean usedAbilityLastTurn() {
        return usedAbilityLastTurn;
    }

    /**
     * Takes this enemy's turn: picks a random alive hero, then attacks or uses its ability.
     *
     * @param heroes the hero party to attack
     * @return the combat log line
     */
    public String takeTurn(Party<Hero> heroes) {
        final List<Hero> aliveHeroes = heroes.getAliveMembers();
        final Hero targetHero = aliveHeroes.get(RANDOM.nextInt(aliveHeroes.size()));

        int damage;
        if (shouldUseAbility()) {
            damage = useAbility(targetHero);
            usedAbilityLastTurn = true;
            lastTarget = abilityTargetsSelf() ? this : targetHero;
            return getName() + " uses " + getAbilityName() + " on " + targetHero.getName() + " for " + damage;
        } else {
            damage = attack(targetHero);
            usedAbilityLastTurn = false;
            lastTarget = targetHero;
        }

        return getName() + " attacks " + targetHero.getName() + " for " + damage;
    }
}
