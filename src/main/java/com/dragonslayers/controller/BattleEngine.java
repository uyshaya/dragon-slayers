package com.dragonslayers.controller;

import com.dragonslayers.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The engine that controls a {@code Battle}.
 */
public class BattleEngine {

    private final Battle battle;

    private final Inventory inventory;

    private final List<Enemy> enemiesThatActed = new ArrayList<>();

    private List<Hero> heroOrder;

    private int heroIndex;

    /**
     * Creates an engine for the given battle.
     *
     * @param battle    the battle to control
     * @param inventory the party's shared inventory
     */
    public BattleEngine(Battle battle, Inventory inventory) {
        this.battle = battle;
        this.inventory = inventory;
        startRound();
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
        endHeroTurn();
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
        endHeroTurn();
    }

    /**
     * Makes the hero defend until its next turn.
     *
     * @param hero the hero who defends
     */
    public void heroDefend(Hero hero) {
        hero.defend();
        battle.addLog(hero.getName() + " defends");
        endHeroTurn();
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
        endHeroTurn();
    }

    /**
     * Runs every alive enemy's turn, fastest first, then starts the next round.
     * <p>Stops early if the battle ends.</p>
     */
    private void runEnemyTurns() {
        enemiesThatActed.clear(); // only keep the enemies from this enemy phase
        for (Enemy enemy : battle.getEnemyTurnOrder()) {
            if (battle.isOver()) {
                return;
            }
            enemy.stopDefending();
            battle.addLog(enemy.takeTurn(battle.getHeroes()));
            enemiesThatActed.add(enemy);
        }
        battle.nextRound();
    }

    /**
     * Returns the enemies that acted in the last enemy phase, in the order they acted.
     * The battle screen uses this to play their animations.
     * <p>The list is read-only.</p>
     *
     * @return the enemies from the last enemy phase
     */
    public List<Enemy> getEnemiesThatActed() {
        return Collections.unmodifiableList(enemiesThatActed);
    }

    /**
     * Starts a hero phase: sorts the alive heroes by speed and goes back to the first one.
     */
    private void startRound() {
        heroOrder = battle.getHeroTurnOrder();
        heroIndex = 0;
    }

    /**
     * Returns the hero whose turn it is.
     * <p>Check {@code battle.isOver()} first. After the battle ends there is no current hero.</p>
     *
     * @return the current hero
     */
    public Hero getCurrentHero() {
        return heroOrder.get(heroIndex);
    }

    /**
     * Moves to the next hero. After the last hero, runs the enemy phase and starts a new round,
     * unless the battle is over.
     */
    private void endHeroTurn() {
        heroIndex++;
        if (battle.isOver() || heroIndex < heroOrder.size()) {
            return;
        }
        runEnemyTurns();
        if (!battle.isOver()) {
            startRound();
        }
    }
}
