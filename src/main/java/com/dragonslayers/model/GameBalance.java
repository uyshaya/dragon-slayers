package com.dragonslayers.model;

/**
 * All game-balance numbers in one place.
 *
 * <p>Other model classes use these constants instead of raw numbers.</p>
 */
public final class GameBalance {

    /** Prevents creating instances. This class only holds constants. */
    private GameBalance() {
    }

    static final int STARTING_LEVEL = 1;
    static final int MIN_DAMAGE = 1;
    static final int DEFENSE_DIVISOR = 2;
    static final int RANDOM_DAMAGE_MAX = 4;
    static final int DEFEND_DAMAGE_DIVISOR = 2;

    // ----- Heroes -----

    // Warrior: high HP and defense, slow
    static final int WARRIOR_MAX_HP = 120;
    static final int WARRIOR_ATTACK = 14;
    static final int WARRIOR_DEFENSE = 10;
    static final int WARRIOR_SPEED = 6;

    // Mage: low HP and defense, strong attack
    static final int MAGE_MAX_HP = 70;
    static final int MAGE_ATTACK = 18;
    static final int MAGE_DEFENSE = 4;
    static final int MAGE_SPEED = 8;

    // Archer: moderate stats, fastest hero
    static final int ARCHER_MAX_HP = 90;
    static final int ARCHER_ATTACK = 13;
    static final int ARCHER_DEFENSE = 6;
    static final int ARCHER_SPEED = 14;

    // Cleric: moderate HP, weak attack
    static final int CLERIC_MAX_HP = 85;
    static final int CLERIC_ATTACK = 8;
    static final int CLERIC_DEFENSE = 7;
    static final int CLERIC_SPEED = 9;

    // ----- Enemies -----

    // Goblin: weakest enemy, fast
    static final int GOBLIN_MAX_HP = 40;
    static final int GOBLIN_ATTACK = 9;
    static final int GOBLIN_DEFENSE = 3;
    static final int GOBLIN_SPEED = 11;

    // Skeleton: tougher than a goblin, slow
    static final int SKELETON_MAX_HP = 60;
    static final int SKELETON_ATTACK = 11;
    static final int SKELETON_DEFENSE = 8;
    static final int SKELETON_SPEED = 5;

    // Dragon boss: much more HP and attack than any other enemy
    static final int DRAGON_BOSS_MAX_HP = 300;
    static final int DRAGON_BOSS_ATTACK = 20;
    static final int DRAGON_BOSS_DEFENSE = 12;
    static final int DRAGON_BOSS_SPEED = 7;

}
