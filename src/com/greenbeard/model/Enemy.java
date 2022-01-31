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

    //constructor
    public Enemy(String name) {
        try (Reader reader = new FileReader("data/npcs.json")){
            // read who the enemy is
            JSONObject jObj = (JSONObject) jsonParser.parse(reader);
            JSONObject enemyJObj = (JSONObject) jObj.get(name);

            // set weapon and name of enemy
            this.setName(name);
            this.setEnemyName((String) enemyJObj.get("name"));
            this.setWeapon((String) enemyJObj.get("weapon"));
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