package com.dragonslayers.controller;

import com.dragonslayers.model.Battle;
import com.dragonslayers.model.Enemy;
import com.dragonslayers.model.GameCharacter;
import com.dragonslayers.model.Hero;

/**
 * The engine that controls a {@code Battle}.
 */
public class BattleEngine {

    private final Battle battle;

    /**
     * Creates an engine for the given battle.
     *
     * @param battle the battle to control
     */
    public BattleEngine(Battle battle) {
        this.battle = battle;
    }

    /**
     * Attacks the target.
     *
     * @param hero   the attacker
     * @param target the target of the attack
     */
    public void heroAttack(Hero hero, Enemy target) {
        hero.stopDefending();
        int damage = hero.attack(target);
        battle.addLog(hero.getName() + " attacks " + target.getName() + " for " + damage);
    }

    /**
     * Uses the hero's ability on the target.
     *
     * @param hero   the hero using the ability
     * @param target the enemy to hit, or the ally to heal
     */
    public void heroAbility(Hero hero, GameCharacter target) {
        hero.stopDefending();
        int amount = hero.useAbility(target);
        battle.addLog(hero.getName() + " uses ability on " + target.getName() + " for " + amount);
    }

    /**
     * Makes the hero defend until its next turn.
     *
     * @param hero the hero who defends
     */
    public void heroDefend(Hero hero) {
        hero.defend();
        battle.addLog(hero.getName() + " defends");
    }

    /**
     * Runs every alive enemy's turn, fastest first, then starts the next round.
     * <p>Stops early if the battle ends.</p>
     */
    public void runEnemyTurns() {
        for (Enemy enemy : battle.getEnemyTurnOrder()) {
            if (battle.isOver()) {
                return;
            }
            enemy.stopDefending();
            battle.addLog(enemy.takeTurn(battle.getHeroes()));
        }
        battle.nextRound();
    }
}
