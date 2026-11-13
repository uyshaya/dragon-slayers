package com.dragonslayers.model;

public class Cleric extends Hero {

    public Cleric(String name) {
        super(name,
                GameBalance.CLERIC_MAX_HP,
                GameBalance.CLERIC_ATTACK,
                GameBalance.CLERIC_DEFENSE,
                GameBalance.CLERIC_SPEED
        );
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
