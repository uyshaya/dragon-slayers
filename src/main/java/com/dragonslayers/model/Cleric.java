package com.dragonslayers.model;

public class Cleric extends Hero {

    public Cleric(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    @Override
    public String getClassName() {
        return "Cleric";
    }

    @Override
    public int useAbility(GameCharacter target) {
        return 0;
    }
}
