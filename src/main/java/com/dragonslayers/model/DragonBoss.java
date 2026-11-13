package com.dragonslayers.model;

public class DragonBoss extends Enemy {

    public DragonBoss(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    @Override
    public String getClassName() {
        return "Dragon Boss";
    }

    @Override
    public int useAbility(GameCharacter target) {
        return 0;
    }
}
