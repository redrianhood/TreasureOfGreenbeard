package com.greenbeard.model;

import com.greenbeard.util.TextParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Enemy extends Character {
    private JSONParser jsonParser = new JSONParser();
    private String enemyName;
    private int health;
//    private String intro;
//    private String victory;
//    private String defeat;
    private Map<String, String> dialogue = new HashMap<>();

    //constructor
    public Enemy(String location, String name) {
        JSONObject jObj = TextParser.readJsonFile("data/npc.json");
        JSONObject locationJObj = (JSONObject) jObj.get(location);
        JSONObject enemyJObj = (JSONObject) locationJObj.get(name);

        // set weapon and names of enemy
        this.setName(name);
        this.setEnemyName((String) enemyJObj.get("name"));
        this.setWeapon((String) enemyJObj.get("weapon"));

        // set dialogue
//        this.setIntro((String) enemyJObj.get("intro"));
        dialogue.put("intro", (String) enemyJObj.get("intro"));
        dialogue.put("victory", (String) enemyJObj.get("victory"));
        dialogue.put("defeat", (String) enemyJObj.get("defeat"));

    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public Map<String, String> getDialogue() {
        return dialogue;
    }

    //    public String getIntro() {
//        return intro;
//    }
//
//    public void setIntro(String intro) {
//        this.intro = intro;
//    }

    @Override
    public String toString() {
        return "Enemy{" +
                "name=" + getName() +
                ", enemyName='" + getEnemyName() + '\'' +
                ", health=" + getHealth() +
                ", weapon=" + getWeapon() +
                '}';
    }
}