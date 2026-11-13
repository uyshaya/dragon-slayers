package com.dragonslayers.model;

public class Warrior extends Hero {

    public Warrior(String name) {
        super(name,
                GameBalance.WARRIOR_MAX_HP,
                GameBalance.WARRIOR_ATTACK,
                GameBalance.WARRIOR_DEFENSE,
                GameBalance.WARRIOR_SPEED
        );
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
