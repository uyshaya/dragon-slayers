package com.dragonslayers.model;

public abstract class Enemy extends GameCharacter {

    protected Enemy(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
    }

}
