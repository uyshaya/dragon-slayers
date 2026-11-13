package com.dragonslayers.model;

public abstract class GameCharacter {

    private final String name;

    private int hp;

    private final int maxHp;

    private int attack;

    private int defense;

    private int speed;

    private int level;

    private boolean isDefending;

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

    public abstract String getClassName();

    public abstract int useAbility(GameCharacter target);

    public void defend() {
        this.isDefending = true;
    }

    public void stopDefending() {
        this.isDefending = false;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isDefending() {
        return isDefending;
    }

}
