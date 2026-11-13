package com.dragonslayers.model;

/**
 * A character controlled by the game.
 */
public abstract class Enemy extends GameCharacter {

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

}
