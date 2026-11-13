package com.dragonslayers.model;

public class Mage extends Hero {

    public Mage(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    @Override
    public String getClassName() {
        return "Mage";
    }

    @Override
    public int useAbility(GameCharacter target) {
        return 0;
    }
}
