package com.dragonslayers.model;

import java.util.List;
import java.util.Random;

/**
 * A character controlled by the game.
 */
public abstract class Enemy extends GameCharacter {

    private static final int ABILITY_CHANCE = 3;

    private static final Random RANDOM = new Random();

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
        } else {
            damage = attack(targetHero);
        }

        return getName() + " hits " + targetHero.getName() + " for " + damage;
    }
}
