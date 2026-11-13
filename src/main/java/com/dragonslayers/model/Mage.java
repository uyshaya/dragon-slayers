package com.dragonslayers.model;

public class Mage extends Hero {

    public Mage(String name) {
        super(name,
                GameBalance.MAGE_MAX_HP,
                GameBalance.MAGE_ATTACK,
                GameBalance.MAGE_DEFENSE,
                GameBalance.MAGE_SPEED
        );
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
