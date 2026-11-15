package com.dragonslayers.model;

import java.io.Serializable;
import java.util.Random;

import static com.dragonslayers.model.GameBalance.*;

/**
 * The base class for every hero and enemy.
 *
 * <p>Holds the stats every character has and the shared combat rules:
 * attacking, taking damage, healing, and defending.</p>
 */
public abstract class GameCharacter implements Serializable, Attackable, Healable {

    // Every character should share one final random
    private static final Random RANDOM = new Random();

    private final String name;

    private int hp;

    private final int maxHp;

    private int attack;

    private int defense;

    private int speed;

    private int level;

    private boolean isDefending;

    /**
     * Creates a character with full HP at the starting level.
     *
     * @param name    the character's name
     * @param maxHp   maximum hit points
     * @param attack  attack stat
     * @param defense defense stat
     * @param speed   speed stat
     */
    protected GameCharacter(String name,
                            int maxHp,
                            int attack,
                            int defense,
                            int speed) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.level = GameBalance.STARTING_LEVEL;
        this.isDefending = false;
    }

    /**
     * Returns the display name of this character's class, such as "Warrior".
     *
     * @return the class name
     */
    public abstract String getClassName();

    /**
     * Uses this character's special ability.
     *
     * @param target the character the ability is used on
     * @return the damage dealt or HP restored
     */
    public abstract int useAbility(GameCharacter target);

    /**
     * Starts defending, so damage taken is reduced until the next turn.
     */
    public void defend() {
        this.isDefending = true;
    }

    /**
     * Stops defending. Called at the start of this character's turn.
     */
    public void stopDefending() {
        this.isDefending = false;
    }

    /**
     * Lowers HP by the damage. Damage is reduced while defending.
     * HP never goes below 0.
     *
     * @param damage the damage to take
     */
    public void takeDamage(int damage) {
        if (isDefending()) {
            damage = damage / DEFEND_DAMAGE_DIVISOR;
        }

        this.hp = Math.max(0, hp - damage);
    }

    /**
     * Checks whether this character is knocked out.
     *
     * @return true if HP is 0
     */
    public boolean isKnockedOut() {
        return this.hp == 0;
    }

    /**
     * Restores HP, up to max HP. Does nothing if knocked out.
     *
     * @param amount how many hit points to restore
     */
    @Override
    public void heal(int amount) {
        if (isKnockedOut()) {
            return;
        }

        this.hp = Math.min(maxHp, hp + amount);
    }

    /**
     * Attacks the target with a normal hit.
     *
     * <p>Damage is this character's attack.
     *
     * @param target the character being hit
     * @return the damage dealt, before the target's defending reduction
     */
    @Override
    public int attack(GameCharacter target) {
        int damage = calculateDamage(target);
        target.takeDamage(damage);

        return damage;
    }

    /**
     * Calculates the damage dealt to the target.
     *
     * <p>Damage is this character's attack, minus part of the target's
     * defense, plus a random amount. It is always at least
     * {@code MIN_DAMAGE}.</p>
     *
     * @param target the character being hit
     * @return the damage dealt, before the target's defending reduction
     */
    protected int calculateDamage(GameCharacter target) {
        final int targetDefense = target.getDefense() / DEFENSE_DIVISOR;
        // nextInt is exclusive, so + 1 makes the random damage 0 to RANDOM_DAMAGE_MAX
        int damage = this.attack - targetDefense + rollRandomDamage();
        // Floor at MIN_DAMAGE so a negative result can't heal the target
        // Defense can never fully block
        return Math.max(MIN_DAMAGE, damage);
    }

    /**
     * Returns a random extra damage amount from 0 to RANDOM_DAMAGE_MAX
     *
     * @return the random damage
     */
    protected int rollRandomDamage() {
        return RANDOM.nextInt(RANDOM_DAMAGE_MAX + 1);
    }

    /**
     * Returns the character's name.
     *
     * @return the character's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns current hit points.
     *
     * @return current hit points
     */
    public int getHp() {
        return hp;
    }

    /**
     * Returns maximum hit points.
     *
     * @return maximum hit points
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Returns the attack stat.
     *
     * @return the attack stat
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Sets the attack stat.
     *
     * @param attack the new attack stat
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * Returns the defense stat.
     *
     * @return the defense stat
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Sets the defense stat.
     *
     * @param defense the new defense stat
     */
    public void setDefense(int defense) {
        this.defense = defense;
    }

    /**
     * Returns the speed stat.
     *
     * @return the speed stat
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Returns the current level.
     *
     * @return the current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Checks whether this character is defending.
     *
     * @return true if defending this turn
     */
    public boolean isDefending() {
        return isDefending;
    }

    /**
     * Sets the level and HP from a save file.
     *
     * @param level the saved level
     * @param hp    the saved hit points
     * @throws IllegalArgumentException if the level is below the starting level or the HP is out of range
     */
    public void restore(int level, int hp) {
        if (level < GameBalance.STARTING_LEVEL || hp < 0 || hp > maxHp) {
            throw new IllegalArgumentException("Invalid level or HP");
        }
        this.level = level;
        this.hp = hp;
    }

}
