package com.dragonslayers.model;

public class Warrior extends Hero {

    public Warrior(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    @Override
    public String getClassName() {
        return "Warrior";
    }

    @Override
    public int useAbility(GameCharacter target) {
        return 0;
    }
}
