package com.greenbeard.model;

import com.greenbeard.util.TextParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GameMap {
    private static final GameMap GAME_MAP = new GameMap();
    private static final String LOCATIONS_FILE = "data/locations/locations.json";
    private static final String NPC_FILE = "data/npc.json";
    private Map<String, Location> locations = new HashMap<>();

    public static GameMap getInstance() {
        return GAME_MAP;
    }

    private GameMap() {
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
                String basicName = (String) npcKey;
                JSONObject val = (JSONObject) npcVal;
                String name = (String) val.get("name");
                String image = (String) val.get("image");
                String ableToRecruit = (String) val.get("ableToRecruit");
                String recruitMessage = (String) val.get("recruitMessage");
                String enemy = (String) val.get("enemy");
                Character npc;

                if(Boolean.parseBoolean(enemy)) {
                    String weapon = (String) val.get("weapon");
                    String intro = (String) val.get("intro");
                    String victory = (String) val.get("victory");
                    String defeat = (String) val.get("defeat");

                    npc = new Enemy(basicName, intro, victory, defeat, image, recruitMessage, Boolean.parseBoolean(ableToRecruit));

                } else {
                    String greeting = (String) val.get("greeting");
                    String realName = (String) val.get("realName");
                    npc = new NPC(name, greeting, Boolean.parseBoolean(ableToRecruit), recruitMessage, image, realName);
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
                case "restaurant":
                    pathFile = "data/single-image-text/" + location + ".txt";
                    break;
                default:
                    System.out.println("Invalid Location you are trying to go " + location);
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
                case "restaurant":
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

    public void availableCommand(Location location) {
        System.out.print("Available commands: ");
        switch (location.getBasicName()) {
            case "harbor":
                System.out.println("\"" + ColorConsole.RED_BOLD + "show crew" + ColorConsole.RESET + "\", \"" + ColorConsole.RED_BOLD + "show map" + ColorConsole.RESET + "\", " +
                        "\"" + ColorConsole.RED_BOLD + "quit" + ColorConsole.RESET + "\", " + ColorConsole.RED_BOLD + "leave" + ColorConsole.RESET + "\", \"" + ColorConsole.RED_BOLD + "set sail" + ColorConsole.RESET + "\"");
                break;
            case "town":
                System.out.println("\"" + ColorConsole.RED_BOLD + "show crew" + ColorConsole.RESET + "\", \"" + ColorConsole.RED_BOLD + "show map" + ColorConsole.RESET + "\", " +
                        "\"" + ColorConsole.RED_BOLD + "quit" + ColorConsole.RESET + "\", " + "\"" + ColorConsole.RED_BOLD + "go" + ColorConsole.RESET + "\"");
                break;
            case "bar":
            case "cemetery":
            case "crypt":
            case "inn":
            case "clinic":
            case "restaurant":
                System.out.println("\"" + ColorConsole.RED_BOLD + "show crew" + ColorConsole.RESET + "\", \"" + ColorConsole.RED_BOLD + "show map" + ColorConsole.RESET + "\", " +
                        "\"" + ColorConsole.RED_BOLD + "go" + ColorConsole.RESET + "\", " + "\"" + ColorConsole.RED_BOLD + "leave" + ColorConsole.RESET + "\", " +
                        ColorConsole.RED_BOLD + "talk" + ColorConsole.RESET + "\", " +
                         ColorConsole.RED_BOLD + "quit" + ColorConsole.RESET + "\", " +"\"" + ColorConsole.RED_BOLD + "recruit" + ColorConsole.RESET + "\"");
        }

    }

    public Map<String, Location> getLocations() {
        return locations;
    }
}