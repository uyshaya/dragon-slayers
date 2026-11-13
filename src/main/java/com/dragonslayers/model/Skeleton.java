package com.dragonslayers.model;

public class Skeleton extends Enemy {

    public Skeleton(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

    @Override
    public String getClassName() {
        return "Skeleton";
    }

    @Override
    public int useAbility(GameCharacter target) {
        return 0;
    }
}
