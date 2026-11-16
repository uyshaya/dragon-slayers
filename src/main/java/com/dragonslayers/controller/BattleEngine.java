package com.dragonslayers.controller;

import com.dragonslayers.model.*;

/**
 * The engine that controls a {@code Battle}.
 */
public class BattleEngine {

    private final Battle battle;

    private final Inventory inventory;

    /**
     * Creates an engine for the given battle.
     *
     * @param battle    the battle to control
     * @param inventory the party's shared inventory
     */
    public BattleEngine(Battle battle, Inventory inventory) {
        this.battle = battle;
        this.inventory = inventory;
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
        battle.addLog(hero.getName() + " uses " + hero.getAbilityName() + " on " + target.getName() + " for " + amount);
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
     * Uses an item from the inventory on the target.
     *
     * @param hero     the hero using the item
     * @param itemName the item's name
     * @param target   the character to use it on
     * @throws com.dragonslayers.exceptions.InvalidActionException if the item is missing, not usable, or the target is invalid
     */
    public void heroUseItem(Hero hero, String itemName, GameCharacter target) {
        inventory.useItem(itemName, target);
        hero.stopDefending();
        battle.addLog(hero.getName() + " uses " + itemName + " on " + target.getName());
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
