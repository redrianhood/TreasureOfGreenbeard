package com.greenbeard.model;

import java.util.ArrayList;

public class Player extends Character{
    ArrayList<String> crewMates = new ArrayList<>();
    private int gold = 100;

    public Player() {

    }

    public ArrayList<String> addCrewMate(String name){
        crewMates.add(name);
        return crewMates;
    }

    public ArrayList<String> getCrewMates() {
        return crewMates;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
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