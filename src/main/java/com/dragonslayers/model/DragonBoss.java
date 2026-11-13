package com.dragonslayers.model;

public class DragonBoss extends Enemy {

    public DragonBoss(String name) {
        super(name,
                GameBalance.DRAGON_BOSS_MAX_HP,
                GameBalance.DRAGON_BOSS_ATTACK,
                GameBalance.DRAGON_BOSS_DEFENSE,
                GameBalance.DRAGON_BOSS_SPEED
        );
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
