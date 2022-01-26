package com.greenbeard.model;

public class Enemy extends Character {
    private String enemyName;
    private int health;

    //constructor
    public Enemy() {

    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "enemyName='" + enemyName + '\'' +
                ", health=" + health +
                '}';
    }
}