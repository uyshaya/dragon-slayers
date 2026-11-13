package com.dragonslayers.model;

public class Skeleton extends Enemy {

    public Skeleton(String name) {
        super(name,
                GameBalance.SKELETON_MAX_HP,
                GameBalance.SKELETON_ATTACK,
                GameBalance.SKELETON_DEFENSE,
                GameBalance.SKELETON_SPEED
        );
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
