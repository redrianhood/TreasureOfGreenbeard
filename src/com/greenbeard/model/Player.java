package com.greenbeard.model;

import java.util.ArrayList;

public class Player extends Character{
    ArrayList<String> crewMates = new ArrayList<>();
    private int gold = 100;
    private String shipName = "";

    public Player() {

    }

    public ArrayList<String> addCrewMate(String name){
        crewMates.add(name);
        // JSON Crewmates npc.json
        return crewMates;
    }

    public ArrayList<String> getCrewMates() {
        return crewMates;
    }

    public void clearCrewMates() {
        crewMates.clear();
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + getName() + '\'' +
                ", health=" + getHealth() +
                ", weapon='" + getWeapon() + '\'' +
                "crewMates=" + getCrewMates() +
                ", gold=" + getGold() +
                '}';
    }
}