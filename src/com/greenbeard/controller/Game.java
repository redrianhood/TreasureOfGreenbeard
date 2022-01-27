package com.greenbeard.controller;

import com.apps.util.Prompter;

import com.greenbeard.model.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Game {
    private boolean gameOver;
    private Player player;
    private Prompter prompter = new Prompter(new Scanner(System.in));
    private String currentLocation;
    private JSONParser jsonParser = new JSONParser();

    public void execute() {
        welcome();
        start();
        gameOver();
    }

    private void welcome() {
        System.out.println("\n\n");
        try {
            Files.lines(Path.of("data/welcome/banner.txt")).forEach(System.out::println);
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
        String input = prompter.prompt("Where would you like to go?\n -> ").toLowerCase();
        System.out.println(input);
        textParser(input);

    }

    private void textParser(String input) {
        String verb = checkSynonym(input);
        System.out.println("***return from synonym is: " + verb);


        List<String> commands = Arrays.asList(input.split(" "));

        if (commands.contains("go")) {
            try (Reader reader = new FileReader("data/bar/bar.json")) {
                JSONObject jObj = (JSONObject) jsonParser.parse(reader);
                System.out.println(jObj);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private String checkSynonym(String command) {

        try (Reader readerSynonym = new FileReader("data/synonyms.json")) {
            JSONObject jObj = (JSONObject) jsonParser.parse(readerSynonym);
            if (jObj.containsKey(command)) {
                System.out.println("INPUT IS A KEY");
                return command;
            } else {
                final String[] keyStr = {""};
                jObj.forEach((key, val) -> {
                    JSONArray arr = (JSONArray) val;
                    if (arr.contains(command)) {
                        System.out.println("INPUT IS A VALUE OF" + key);
                        keyStr[0] = key.toString();
                    }
                });
                return keyStr[0];
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } return "Required return statement here";
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