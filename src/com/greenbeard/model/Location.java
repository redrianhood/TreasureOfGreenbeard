package com.greenbeard.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {
    private String name;
    private String basicName;
    private List<String> canTravelTo;
    private String description;
    private Map<String, NPC> npcs = new HashMap<>();

    public Location(String name, String description) {
        setName(name);
        setDescription(description);
    }

    public Location(String name, String description, List<String> canTravelTo, String basicName) {
        this(name, description);
        setBasicName(basicName);
        setCanTravelTo(canTravelTo);
    }

    public Location(String name,String description,  List<String> canTravelTo, String basicName, Map<String, NPC> npcs) {
        this(name, description, canTravelTo, basicName);
        setNpcs(npcs);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCanTravelTo() {
        return canTravelTo;
    }

    public void setCanTravelTo(List<String> canTravelTo) {
        this.canTravelTo = canTravelTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, NPC> getNpcs() {
        return npcs;
    }

    public void setNpcs(Map<String,NPC> npcs) {
        this.npcs = npcs;
    }

    public void addNpc(NPC npc) {
        this.npcs.put(npc.getName(), npc);
    }

    public String getBasicName() {
        return basicName;
    }

    public void setBasicName(String basicName) {
        this.basicName = basicName;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", canTravelTo=" + canTravelTo +
                ", description='" + description + '\'' +
                ", npcs=" + npcs +
                '}';
    }
}