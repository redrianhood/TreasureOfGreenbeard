package com.greenbeard.controller;

import com.apps.util.Prompter;

import com.greenbeard.model.Enemy;
import com.greenbeard.model.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Game {
    private boolean gameOver;
    private Player player = new Player();
    private Prompter prompter = new Prompter(new Scanner(System.in));
    private String currentLocation = "town";
    private JSONParser jsonParser = new JSONParser();
    private Scanner scanner = new Scanner(System.in);
    private Clip clip;
    private static long BANNER_DELAY = 1500;
    private static final long WORD_DELAY = 250;


    public void execute() {
        gameOver = false;
        welcome();
        while (!gameOver) {
            if (player.getHealth() <= 0) {
                gameOver = true;
                break;
            }
            start();
        }
        gameOver();
    }

    private void audio(String fileName) {
        try {
            File audioFile = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-10.0f);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
//    private float getCurrentVolume(Clip clip){
//        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//        return (float) Math.pow(10f, volumeControl.getValue() / 20f);
//    }

    private void welcome() {
        audio("data/audio/gamemusic.wav");
        System.out.println("\n\n");
        try {
            //Files.lines(Path.of("data/welcome/banner.txt")).forEach(System.out::println);
            Files.lines(Path.of("data/welcome/banner1.txt")).forEach(System.out::println);
            delay(BANNER_DELAY);
            Files.lines(Path.of("data/welcome/banner2.txt")).forEach(System.out::println);
            delay(BANNER_DELAY);
            Files.lines(Path.of("data/welcome/banner3.txt")).forEach(System.out::println);
            delay(BANNER_DELAY);

            List<String> welcome = Files.readAllLines(Path.of("data/welcome/welcome.txt"));
            List<String> intro = Files.readAllLines(Path.of("data/welcome/intro.txt"));
            welcome.forEach((line) -> {
                printWordByWord(line);
            });
            player.setName(prompter.prompt("\nWhat is your name Captain? -> "));
            player.setShipName(prompter.prompt("What is the name of your Ship? -> "));
            String weapon = prompter.prompt("What kind of weapon do you carry?\n" +
                    "Options are: sword, or pistol\n --> ", "sword|pistol", "Invalid selection");
            player.setWeapon(weapon);
            System.out.printf("\n\nYou are the Great Captain %s, Captain of the %s.\n" +
                            "With your trusty %s by your side, you set off to town.%n",
                    player.getName(), player.getShipName(), player.getWeapon());
            intro.forEach((line) -> {
                printWordByWord(line);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printWordByWord(String line) {
        if (!line.trim().isEmpty()) {
            String[] words = line.split(" ");
            if ((words == null) || (words.length < 1)) {
                return;
            }
            List<String> wordsList = Arrays.asList(words);
            wordsList.forEach((word) -> {
                System.out.print(word + " ");
                delay(WORD_DELAY);
            });
        }
        System.out.println();
        delay(WORD_DELAY);
    }

    private void clearConsole() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    private void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        String input = prompter.prompt("What would you like to do?\n -> ").toLowerCase();
        if (!this.currentLocation.equals("town")) {
            System.out.println("Interact with the environment with verb commands, such as 'go', 'talk', 'recruit', followed by a noun.");
        }
        textParser(input);
    }

    private void textParser(String input) {
        List<String> commands = Arrays.asList(input.split(" "));

        if (commands.size() != 2) {
            System.out.println("Invalid Command");
            return;
        }

        String verb = checkSynonym(commands.get(0));
        String noun = commands.get(1);

        try (Reader reader = new FileReader("data/locations/locations.json")) {
            JSONObject jObj = (JSONObject) jsonParser.parse(reader);
            JSONObject location = (JSONObject) jObj.get(this.currentLocation);

            if ("go".equals(verb)) {
                //check if valid route based on json locations for the current location
                if (!validateRoute(noun)) {
                    //invalid route.
                    System.out.println("Can not go to " + noun + " from " + this.currentLocation);
                    return;
                }

                //Valid route.

                //Get the JSON object for the target destinationS
                location = (JSONObject) jObj.get(noun);
                //Check if JSON object found for the target destination
                if (location != null) {
                    //JSON object found for the target destination
                    String description = (String) location.get("description");
                    System.out.println(description);
                    this.currentLocation = noun;
                } else {
                    //JSON object NOT found for the target destination
                    System.out.println("No JSON entry for: " + noun);
                    return;
                }
            }
            if (!currentLocation.equals("town") && "recruit".equals(verb)) {
                JSONObject npcs = (JSONObject) location.get("npcs");
                JSONObject npc = (JSONObject) npcs.get(noun);

                if (npc != null) {
                    String name = (String) npc.get("name");
                    String ableToRecruit = (String) npc.get("ableToRecruit");
                    if (ableToRecruit.equals("true")) {
                        player.addCrewMate(name);
                    }
                }
            }
            if ("set".equals(verb) && "sail".equals(noun)) {
                this.currentLocation = "island";
                location = (JSONObject) jObj.get(this.currentLocation);
                String description = (String) location.get("description");
                System.out.println(description);
                finale();
            }

            if (!currentLocation.equals("town") && "talk".equals(verb)) {
                JSONObject npcs = (JSONObject) location.get("npcs");
                JSONObject npc = (JSONObject) npcs.get(noun);

                if (npc != null) {
                    String greet = (String) npc.get("greeting");
                    System.out.println(greet);
                } else {
                    System.out.println("Sorry you cannot speak to " + noun + ".");
                }
            }

            System.out.println(player.getCrewMates());

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean validateRoute(String destination) {
        try (Reader reader = new FileReader("data/locations/locations.json")) {
            //Get the JSON Data for the current location
            JSONObject jObj = (JSONObject) jsonParser.parse(reader);
            JSONObject currentLocationJObj = (JSONObject) jObj.get(this.currentLocation);
//            Get the possible destinations from the current location
            JSONArray locationsArray = (JSONArray) currentLocationJObj.get("locations");

            //check if the target destination is found in the permitted destinations
            for (Object locElement : locationsArray) {
                String locationName = (String) locElement;

                if (locationName.equals(destination)) {
                    //valid destination
                    return true;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        //destination not found
        return false;
    }

    private String checkSynonym(String command) {

        try (Reader readerSynonym = new FileReader("data/synonyms.json")) {
            JSONObject jObj = (JSONObject) jsonParser.parse(readerSynonym);
            if (jObj.containsKey(command)) {
                return command;
            } else {
                final String[] keyStr = {""};
                jObj.forEach((key, val) -> {
                    JSONArray arr = (JSONArray) val;
                    if (arr.contains(command)) {
                        keyStr[0] = String.valueOf(key);
                    }
                });
                return keyStr[0];
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return "Required return statement here";
    }

    private void gameOver() {
        System.out.println("GAME OVER!");
        String playAgain = prompter.prompt("Play again? yes or no?\n -> ", "yes|no", "Invalid Choice");
        if ("yes".equals(playAgain)) {
            resetGame();
            execute();
        } else if ("no".equals(playAgain)) {
            System.out.println("Thanks for playing! See you again!");
            System.exit(0);
        }
    }

    private void resetGame() {
        player.clearCrewMates();
    }

    void finale() {
        if (player.getCrewMates().contains("mourner")) {
            System.out.println("You land on Yarginory Island, look around, and see a treasure chest just" +
                    "sitting on the beach! You approach it cautiously...");
            fight("greenbeard");
        } else {
            System.out.println("You didn't have a navigator and got lost at sea. Sorry :(\n" +
                    "GAME OVER");
        }
        gameOver = true;
    }
}