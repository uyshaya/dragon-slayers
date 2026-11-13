package com.dragonslayers.model;

public class Archer extends Hero {

    public Archer(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    @Override
    public String getClassName() {
        return "Archer";
    }

    @Override
    public int useAbility(GameCharacter target) {
        return 0;
    }
}
