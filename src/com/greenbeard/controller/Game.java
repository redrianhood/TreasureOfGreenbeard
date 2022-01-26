package com.greenbeard.controller;

import com.apps.util.Prompter;
import com.greenbeard.model.Location;
import com.greenbeard.model.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Player player;
    private Prompter prompter = new Prompter(new Scanner(System.in));
    private Location location;

    public void execute() {
        welcome();
    }

    private void welcome() {
        try {
            List<String> welcome = Files.readAllLines(Path.of("data/welcome/welcome.txt"));
            List<String> intro = Files.readAllLines(Path.of("data/welcome/intro.txt"));
            welcome.forEach(
                    line -> System.out.println(line)
            );
            intro.forEach(line -> System.out.println(line));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}