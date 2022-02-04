package com.greenbeard.model;

public class Character {
    // Fields
    private String name;
    private String realName;
    private String greeting;
    private boolean ableToRecruit;
    private String recruitMessage;
    private String image;
    private int health = 100;
    private String weapon = "SWORD";

    // Accessors
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    // Weapon Accessors
    public String getWeaponName() {
        return Weapon.valueOf(weapon).getName();
    }

    public Enum<Weapon> getWeapon() {
        return Weapon.valueOf(weapon.toUpperCase());
    }

    public void setWeapon(String weapon) {
        // may want to add validation here
        this.weapon = weapon.toUpperCase();
    }

    public int getBaseDmg() {
        return Weapon.valueOf(weapon).getBaseDmg();
    }

    public int getVariableDmg() {
        return Weapon.valueOf(weapon).getDmgRoll();
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public boolean isAbleToRecruit() {
        return ableToRecruit;
    }

    public void setAbleToRecruit(boolean ableToRecruit) {
        this.ableToRecruit = ableToRecruit;
    }

    public String getRecruitMessage() {
        return recruitMessage;
    }

    public void setRecruitMessage(String recruitMessage) {
        this.recruitMessage = recruitMessage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        PISTOL("Pistol", 5, 15, 50),
        SWORD("Cutlass", 10, 5, 75);

        // Fields
        private final String name;
        private final int baseDmg;
        private final int dmgRoll;
        private final int goldValue;

        // Ctor: set values of ENUM
        Weapon(String name, int baseDmg, int dmgRoll, int goldValue) {
            this.name = name;
            this.baseDmg = baseDmg;
            this.dmgRoll = dmgRoll;
            this.goldValue = goldValue;
        }

        // Accessor Methods
        public String getName() {
            return name;
        }

        public int getBaseDmg() {
            return baseDmg;
        }

        public int getDmgRoll() {
            return dmgRoll;
        }

        public int getGoldValue() {
            return goldValue;
        }
    }
}