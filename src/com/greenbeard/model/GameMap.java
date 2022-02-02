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


}