package com.greenbeard.model;

class Character {
    private String name;
    private int health = 100;
    private String weapon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }


    @Override
    public String toString() {
        return "Character{" +
                "name='" + getName() + '\'' +
                ", health=" + getHealth() +
                ", weapon='" + getWeapon() + '\'' +
                '}';
    }
}