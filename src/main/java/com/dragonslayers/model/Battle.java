package com.dragonslayers.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A fight between the hero party and an enemy party.
 */
public class Battle {

    private static final int FIRST_ROUND = 1;

    private static final Comparator<GameCharacter> BY_SPEED =
            Comparator.comparingInt(GameCharacter::getSpeed).reversed();

    private final Party<Hero> heroes;

    private final Party<Enemy> enemies;

    private final List<String> log = new ArrayList<>();

    private int round;

    /**
     * Constructs a battle between hero and enemy party.
     *
     * @param heroes the player's party
     * @param enemies the enemy party
     */
    public Battle(Party<Hero> heroes, Party<Enemy> enemies) {
        this.heroes = heroes;
        this.enemies = enemies;
        round = FIRST_ROUND;
    }

    /**
     * Returns heroes party in a battle.
     *
     * @return heroes
     */
    public Party<Hero> getHeroes() {
        return heroes;
    }

    /**
     * Returns enemies party in a battle.
     *
     * @return enemies
     */
    public Party<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the alive heroes, fastest first.
     *
     * @return heroes
     */
    public List<Hero> getHeroTurnOrder() {
        return heroes.getAliveMembers().stream()
                .sorted(BY_SPEED)
                .toList();
    }

    /**
     * Returns the alive enemies, fastest first.
     *
     * @return enemies
     */
    public List<Enemy> getEnemyTurnOrder() {
        return enemies.getAliveMembers().stream()
                .sorted(BY_SPEED)
                .toList();
    }

    /**
     * Adds a line to the combat log.
     *
     * @param line the line to add
     */
    public void addLog(String line) {
        log.add(line);
    }

    /**
     * Returns list of logs.
     *
     * @return unmodifiable log
     */
    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }

    /**
     * Increments the current round.
     */
    public void nextRound() {
        round++;
    }

    /**
     * Returns the current round.
     *
     * @return round
     */
    public int getRound() {
        return round;
    }

    /**
     * Returns true if either party is defeated.
     *
     * @return true if either party is defeated
     */
    public boolean isOver() {
        return heroes.isDefeated() || enemies.isDefeated();
    }

    /**
     * Returns true if enemies are defeated.
     *
     * @return true if enemies are defeated
     */
    public boolean isWon() {
        return enemies.isDefeated();
    }
}
