package com.greenbeard.model;

import java.util.HashSet;
import java.util.Set;

public class Player extends Character{
    private static Player player;
    Set<String> crewMates = new HashSet<>();
    private int gold = 100;
    private String shipName = "";

    private Player() {
        // singleton
    }

    public static Player getInstance(){
        if(player == null){
            player = new Player();
        }
        return player;
    }

    public Set<String> addCrewMate(String name){
        crewMates.add(name);
        return crewMates;
    }

    public Set<String> getCrewMates() {
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