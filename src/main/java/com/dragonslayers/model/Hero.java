package com.dragonslayers.model;

/**
 * A character controlled by the player.
 *
 * <p>Heroes can be written to a text save file.</p>
 */
public abstract class Hero extends GameCharacter implements Saveable {

    private Weapon weapon;

    private Armor armor;

    private int abilityUsesLeft = GameBalance.ABILITY_USES_PER_ENCOUNTER;

    /**
     * Creates a hero with the given stats.
     *
     * @param name    the hero's name
     * @param maxHp   maximum hit points
     * @param attack  attack stat
     * @param defense defense stat
     * @param speed   speed stat
     */
    protected Hero(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    /**
     * Returns the equipped weapon.
     *
     * @return the weapon, or null if none is equipped
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Returns the equipped armor.
     *
     * @return the armor, or null if none is equipped
     */
    public Armor getArmor() {
        return armor;
    }

    /**
     * Returns this hero as one line for a text save file.
     *
     * @return the save line, for example {@code Hero=Warrior,Aria,1,95}
     */
    @Override
    public String toSaveFormat() {
        return "Hero=" + this.getClassName() +
                "," + this.getName() +
                "," + this.getLevel() +
                "," + this.getHp();
    }

    void equipWeapon(Weapon newWeapon) {
        if (weapon != null) {
            setAttack(getAttack() - weapon.getBonus());
        }
        this.weapon = newWeapon;
        setAttack(getAttack() + newWeapon.getBonus());
    }

    void equipArmor(Armor newArmor) {
        if (armor != null) {
            setDefense(getDefense() - armor.getBonus());
        }
        this.armor = newArmor;
        setDefense(getDefense() + newArmor.getBonus());
    }

    /**
     * Returns whether this hero's ability targets allies instead of enemies.
     * Most abilities target enemies.
     *
     * @return true if the ability targets allies
     */
    public boolean abilityTargetsAllies() {
        return false;
    }

    /**
     * Returns how many times this hero can still use its ability in the current encounter.
     *
     * @return the uses left
     */
    public int getAbilityUsesLeft() {
        return abilityUsesLeft;
    }

    /**
     * Returns how many ability uses every hero gets at the start of an encounter.
     *
     * @return the uses per encounter
     */
    public int getMaxAbilityUses() {
        return GameBalance.ABILITY_USES_PER_ENCOUNTER;
    }

    /**
     * Says whether this hero can still use its ability in the current encounter.
     *
     * @return true if at least one use is left
     */
    public boolean hasAbilityUses() {
        return abilityUsesLeft > 0;
    }

    /**
     * Takes away one ability use. Called by the battle engine after the ability is used.
     */
    public void spendAbilityUse() {
        if (abilityUsesLeft > 0) {
            abilityUsesLeft--;
        }
    }

    /**
     * Gives back all ability uses. Called at the start of every encounter.
     */
    public void resetAbilityUses() {
        abilityUsesLeft = GameBalance.ABILITY_USES_PER_ENCOUNTER;
    }

}
