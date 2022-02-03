package com.greenbeard.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPC {
    private String name;
    private String greeting;
    private boolean ableToRecruit;
    private String recruitMessage;
    private String image;
    private String weapon;
    private String intro;
    private Map<String, Map<String, List<String>>> dialogue = new HashMap<>();

    public NPC(String name, String greeting, boolean ableToRecruit, String recruitMessage, String image) {
        this.name = name;
        this.greeting = greeting;
        this.ableToRecruit = ableToRecruit;
        this.recruitMessage = recruitMessage;
        this.image = image;
    }

    public NPC(String name, String greeting, boolean ableToRecruit, String recruitMessage, String image, HashMap<String, Map<String, List<String>>> dialogue) {
        this.name = name;
        this.greeting = greeting;
        this.ableToRecruit = ableToRecruit;
        this.recruitMessage = recruitMessage;
        this.image = image;
        this.dialogue = dialogue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "NPC{" +
                "name='" + name + '\'' +
                ", greeting='" + greeting + '\'' +
                ", ableToRecruit=" + ableToRecruit +
                ", recruitMessage='" + recruitMessage + '\'' +
                ", image='" + image + '\'' +
                ", dialogue=" + dialogue +
                '}';
    }
}