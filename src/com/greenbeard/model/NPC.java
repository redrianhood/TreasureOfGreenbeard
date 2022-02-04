package com.greenbeard.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPC extends Character {

    private Map<String, Map<String, List<String>>> dialogue = new HashMap<>();

    public NPC(String name, String greeting, boolean ableToRecruit, String recruitMessage, String image, String realName) {
        setName(name);
        setGreeting(greeting);
        setAbleToRecruit(ableToRecruit);
        setRecruitMessage(recruitMessage);
        setImage(image);
        setRealName(realName);
    }

    public NPC(String name, String greeting, boolean ableToRecruit, String recruitMessage, String image, String realName, HashMap<String, Map<String, List<String>>> dialogue) {
        this(name, greeting, ableToRecruit, recruitMessage, image, realName);
        this.dialogue = dialogue;
    }


    @Override
    public String toString() {
        return "NPC{" +
                "name='" + getName() + '\'' +
                ", greeting='" + getGreeting() + '\'' +
                ", ableToRecruit=" + isAbleToRecruit() +
                ", recruitMessage='" + getRecruitMessage() + '\'' +
                ", image='" + getImage() + '\'' +
                ", dialogue=" + dialogue +
                '}';
    }
}