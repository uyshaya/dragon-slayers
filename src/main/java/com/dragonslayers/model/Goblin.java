package com.dragonslayers.model;

public class Goblin extends Enemy {

    public Goblin(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    @Override
    public String getClassName() {
        return "Goblin";
    }

    @Override
    public int useAbility(GameCharacter target) {
        return 0;
    }
}
