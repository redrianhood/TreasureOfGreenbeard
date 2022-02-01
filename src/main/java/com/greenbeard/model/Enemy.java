package com.greenbeard.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Enemy extends Character {
    private JSONParser jsonParser = new JSONParser();
    private String enemyName;
    private int health;
    private String intro;

    //constructor
    public Enemy(String location, String name) {
        try (Reader reader = new FileReader("data/npc.json")){
            // read who the enemy is
            JSONObject jObj = (JSONObject) jsonParser.parse(reader);
            // RVB - is there a better way to dig into the JSON?
            JSONObject locationJObj = (JSONObject) jObj.get(location);
            JSONObject enemyJObj = (JSONObject) locationJObj.get(name);

            // set weapon and names of enemy
            this.setName(name);
            this.setEnemyName((String) enemyJObj.get("name"));
            this.setWeapon((String) enemyJObj.get("weapon"));

            // set dialogue
            this.setIntro((String) enemyJObj.get("intro"));
        }
        catch (IOException | ParseException e){
            e.printStackTrace();
        }
    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

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