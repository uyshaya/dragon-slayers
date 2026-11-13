package com.dragonslayers.model;

public abstract class Hero extends GameCharacter {

    protected Hero(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

}
