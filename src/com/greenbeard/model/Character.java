package com.greenbeard.model;

class Character {
    // Fields
    private String name;
    private int health = 100;
    private String weapon;

    // Accessors
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

    public Enum<Weapon> getWeapon() {
        return Weapon.valueOf(weapon.toUpperCase());
    }

    public void setWeapon(String weapon) {
        // may want to add validation here
        this.weapon = weapon.toUpperCase();
    }

    // Weapon Accessors
    public String getWeaponName() {
        return Weapon.valueOf(weapon).getName();
    }

    public int getWeaponDmg() {
        return Weapon.valueOf(weapon).getDamage();
    }


    @Override
    public String toString() {
        return "Character{" +
                "name='" + getName() + '\'' +
                ", health=" + getHealth() +
                ", weapon='" + getWeapon() + '\'' +
                '}';
    }

    // inner enum for Weapons
    enum Weapon {
        PISTOL("Pistol", 20, 50),
        SWORD("Cutlass", 15, 75);

        // Fields
        private final String name;
        private final int damage;
        private final int goldValue;

        // Ctor: set values of ENUM
        Weapon(String name, int damage, int goldValue) {
            this.name = name;
            this.damage = damage;
            this.goldValue = goldValue;
        }

        // Accessor Methods
        public String getName() {
            return name;
        }

        public int getDamage() {
            return damage;
        }

        public int getGoldValue() {
            return goldValue;
        }
    }

}