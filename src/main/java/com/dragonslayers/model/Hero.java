package com.dragonslayers.model;

/**
 * A character controlled by the player.
 *
 * <p>Heroes can be written to a text save file.</p>
 */
public abstract class Hero extends GameCharacter implements Saveable {

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
}
