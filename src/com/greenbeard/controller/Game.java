package com.greenbeard.controller;

import com.apps.util.Prompter;
import com.apps.util.Console;
import com.greenbeard.model.Location;
import com.greenbeard.model.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Game {
    private boolean gameOver;
    private Player player;
    private Prompter prompter = new Prompter(new Scanner(System.in));
    private Location location;

    public void execute() {
        welcome();
        start();
        gameOver();
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

    private void start() {
        String input = prompter.prompt("Where would you like to go?\n -> ", "bar|cemetery|crypt|harbor", "Sorry not valid location, try again.");
        
        System.out.println(input);
    }

    private void gameOver() {
        System.out.println("GAME OVER!");
        String playAgain = prompter.prompt("Play again? yes or no?\n -> ", "yes|no", "Invalid Choice");
        if ("yes".equals(playAgain)) {
            execute();
        } else if ("no".equals(playAgain)) {
            System.out.println("Thanks for playing! See you again!");
            System.exit(0);
        }
    }
}