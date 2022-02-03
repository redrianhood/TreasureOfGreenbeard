package com.greenbeard.model;

import com.greenbeard.util.TextParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GameMap {
    private static final String LOCATIONS_FILE = "data/locations/locations.json";
    private static final String NPC_FILE = "data/npc.json";
    private Map<String, Location> locations = new HashMap<>();

    public GameMap() {
        JSONObject locObj = TextParser.readJsonFile(LOCATIONS_FILE);
        locObj.forEach((k, v) -> {
            String key = (String) k;
            JSONObject value = (JSONObject) v;
            String name = (String) value.get("name");
            String description = (String) value.get("description");

            List<String> travel = new ArrayList<>();
            if (value.get("locations") != null) {
                JSONArray travelTo = (JSONArray) value.get("locations");
                travelTo.forEach(item -> travel.add((String) item));
            }
            locations.put(key, new Location(name, description, travel, key));
        });

        JSONObject npcObj = TextParser.readJsonFile(NPC_FILE);
        npcObj.forEach((k, v) -> {
            String key = (String) k;
            JSONObject value = (JSONObject) v;
            value.forEach((npcKey, npcVal) -> {
                JSONObject val = (JSONObject) npcVal;
                String name = (String) val.get("name");
                String greeting = (String) val.get("greeting");
                String ableToRecruit = (String) val.get("ableToRecruit");
                String recruitMessage = (String) val.get("recruitMessage");
                String image = (String) val.get("image");
                String weapon = (String) val.get("weapon");
                String intro = (String) val.get("intro");
                NPC npc = new NPC(name, greeting, Boolean.parseBoolean(ableToRecruit), recruitMessage, image);
                if(weapon != null && intro != null) {
                    npc.setWeapon(weapon);
                    npc.setIntro(intro);
                }
                Location curLoc = locations.get(key);
                curLoc.addNpc(npc);
            });
        });
    }

    public void showLocation(String location) {
        String pathFile = null;
        try {
            switch (location) {
                case "bar":
                case "cemetery":
                case "clinic":
                case "crypt":
                case "harbor":
                case "inn":
                case "island":
                case "town":
                    pathFile = "data/single-image-text/" + location + ".txt";
                    break;
                default:
                    System.out.println("Invalid Location you are trying to go" + location);
            }
            if (pathFile != null) {
                Files.lines(Path.of(pathFile)).forEach(System.out::println);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMap(String location) {
        String filePath = null;
        try {
            switch (location) {

                case "bar":
                case "cemetery":
                case "clinic":
                case "crypt":
                case "harbor":
                case "inn":
                case "island":
                case "town":
                    filePath = "data/map/ascii-map-" + location + ".txt";
                    break;
                default:
                    System.out.println("There is nothing to show, verify your input>");
            }
            if (filePath != null) {
                Files.lines(Path.of(filePath)).forEach(System.out::println);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void availableCommand(String location) {
        System.out.print("These are the available commands: ");
        switch (location) {
            case "harbor":
                System.out.println("\"" + ColorConsole.RED_BOLD + "show crew" + ColorConsole.RESET + "\", \"" + ColorConsole.RED_BOLD +  "show map" + ColorConsole.RESET + "\", " +
                        "\"" + ColorConsole.RED_BOLD + "leave" + ColorConsole.RESET + "\", \"" + ColorConsole.RED_BOLD + "set sail" + ColorConsole.RESET + "\"");
                break;
            case "town":
                System.out.println("\"" + ColorConsole.RED_BOLD + "show crew" + ColorConsole.RESET + "\", \"" + ColorConsole.RED_BOLD +  "show map" + ColorConsole.RESET + "\", " +
                        "\"" +  ColorConsole.RED_BOLD +  "go" + ColorConsole.RESET + "\"");
                break;
            case "bar":
            case "cemetery":
            case "crypt":
            case "inn":
            case "clinic":
            case "restaurant":
                System.out.println("\"" + ColorConsole.RED_BOLD + "show crew" + ColorConsole.RESET + "\", \"" + ColorConsole.RED_BOLD +  "show map" + ColorConsole.RESET + "\", " +
                        "\"" +  ColorConsole.RED_BOLD + "go" + ColorConsole.RESET + "\", " + "\"" + ColorConsole.RED_BOLD +  "leave" + ColorConsole.RESET + "\", " +
                        ColorConsole.RED_BOLD +  "talk" + ColorConsole.RESET + "\", " + "\"" + ColorConsole.RED_BOLD +  "recruit" + ColorConsole.RESET + "\"");
        }

    public Map<String, Location> getLocations() {
        return locations;

    }
}