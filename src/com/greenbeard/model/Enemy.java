package com.greenbeard.model;

import java.util.HashMap;
import java.util.Map;

public class Enemy extends Character {
    private String intro;
    private String victory;
    private String defeat;

    //constructor
    public Enemy(String name, String realName, String intro, String victory, String defeat, String image, String recruitMessage, boolean ableToRecruit, String occupation) {
        setName(name);
        setRealName(realName);
        setIntro(intro);
        setVictory(victory);
        setDefeat(defeat);
        setImage(image);
        setAbleToRecruit(ableToRecruit);
        setRecruitMessage(recruitMessage);
        setOccupation(occupation);
    }


    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getVictory() {
        return victory;
    }

    public void setVictory(String victory) {
        this.victory = victory;
    }

    public String getDefeat() {
        return defeat;
    }

    public void setDefeat(String defeat) {
        this.defeat = defeat;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "name=" + getName() +
                ", enemyName='" + getName() + '\'' +
                ", health=" + getHealth() +
                ", weapon=" + getWeapon() +
                '}';
    }
}