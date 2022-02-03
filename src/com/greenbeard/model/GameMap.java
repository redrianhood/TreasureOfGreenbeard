package com.greenbeard.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameMap {
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
    }
}