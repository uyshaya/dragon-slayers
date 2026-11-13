package com.dragonslayers.model;

public class Goblin extends Enemy {

    public Goblin(String name) {
        super(name,
                GameBalance.GOBLIN_MAX_HP,
                GameBalance.GOBLIN_ATTACK,
                GameBalance.GOBLIN_DEFENSE,
                GameBalance.GOBLIN_SPEED
        );
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
