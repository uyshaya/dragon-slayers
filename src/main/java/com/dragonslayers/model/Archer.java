package com.dragonslayers.model;

public class Archer extends Hero {

    public Archer(String name) {
        super(name,
                GameBalance.ARCHER_MAX_HP,
                GameBalance.ARCHER_ATTACK,
                GameBalance.ARCHER_DEFENSE,
                GameBalance.ARCHER_SPEED
        );
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
