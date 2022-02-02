package com.greenbeard.controller;

import com.apps.util.Prompter;

import com.greenbeard.model.Audio;
import com.greenbeard.model.Enemy;
import com.greenbeard.model.GameMap;
import com.greenbeard.model.Player;
import com.greenbeard.util.Die;
import com.greenbeard.util.TextParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Game {
    private boolean gameOver;
    private boolean dialogue;
    private String currentLocation = "town";
    private Player player = new Player();
    private Die die = new Die();
    private Audio audio = new Audio();
    private GameMap map = new GameMap();
    private Prompter prompter = new Prompter(new Scanner(System.in));
    private static long BANNER_DELAY = 0; //1500;
    private static final String LOCATIONS_FILE = "data/locations/locations.json";
    private static final String NPC_FILE = "data/npc.json";
    private static final String DIALOGUE_FILE = "data/dialogue.json";


    public void execute() {
        gameOver = false;
        welcome();
        map.showLocation(this.currentLocation);
        printCurrentLocation();
        while (!gameOver) {
            if (player.getHealth() <= 0) {
                gameOver = true;
                break;
            }
            start();
        }
        gameOver();
    }

    private void help() {
        System.out.println("\n\n");
        printCurrentLocation();
        //showLocation();
        System.out.println("\n");
//        System.out.println("You can chose to go to:\n" + getDestinations(this.currentLocation));

        System.out.println("\n\n");
        audio.audioPreference();
    }

    private void printCurrentLocation() {
        System.out.println("Your current location is: " + this.currentLocation);
    }

    private void welcome() {
        audio.play("data/audio/gamemusic.wav", Clip.LOOP_CONTINUOUSLY);
        System.out.println("\n\n");
        try {
            //Files.lines(Path.of("data/welcome/banner.txt")).forEach(System.out::println);
            Files.lines(Path.of("data/welcome/banner1.txt")).forEach(System.out::println);
            TextParser.delay(BANNER_DELAY);
            Files.lines(Path.of("data/welcome/banner2.txt")).forEach(System.out::println);
            TextParser.delay(BANNER_DELAY);
            Files.lines(Path.of("data/welcome/banner3.txt")).forEach(System.out::println);
            TextParser.delay(BANNER_DELAY);

            audio.audioPreference();
            System.out.println("\n\n");
            List<String> welcome = Files.readAllLines(Path.of("data/welcome/welcome.txt"));
            List<String> intro = Files.readAllLines(Path.of("data/welcome/intro.txt"));
            welcome.forEach((line) -> {
                TextParser.printWordByWord(line);
            });
            player.setName(prompter.prompt("\nWhat is your name Captain? -> "));
            player.setShipName(prompter.prompt("What is the name of your Ship? [please include 'The' or not] -> "));
            String weapon = prompter.prompt("What kind of weapon do you carry?\n" +
                    "Options are: sword, or pistol\n --> ", "sword|pistol", "Invalid selection");
            player.setWeapon(weapon);
            System.out.printf("\n\nYou are the Great Captain %s, Captain of the %s.\n" +
                            "With your trusty %s by your side, you set off to town.%n",
                    player.getName(), player.getShipName(), player.getWeapon());
            intro.forEach((line) -> {
                TextParser.printWordByWord(line);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearConsole() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    private void start() {
        System.out.println("\n");
        //showLocation();

        System.out.println("\n");
        String input = prompter.prompt("What would you like to do?\n -> ").toLowerCase();
        List<String> commands = Arrays.asList(input.split(" "));

        if (commands.size() == 1) {
            if ("help".equals(commands.get(0))) {
                help();
                return;
            }
        } else if (commands.size() != 2) {
            System.out.println("Invalid Command");
            return;
        }

        String verb = TextParser.checkSynonym(commands.get(0));
        String noun = commands.get(1);

        if ("go".equals(verb)) {
            travel(noun);
        }

        // recruit an npc for your crew
        if (!currentLocation.equals("town") && "recruit".equals(verb)) {
            recruitCrewMember(noun);
        }
        // set sail for island when ready for final boss
        if ("set".equals(verb) && "sail".equals(noun)) {
            audio.play("data/audio/finalbattle.wav", Clip.LOOP_CONTINUOUSLY);
            travel(noun);
        }
        // talking to someone
        if (!currentLocation.equals("town") && "talk".equals(verb)) {
            startDialogue(noun);
        }

        //show current crew members
        if ("look".equals(verb)) {
            if ("crew".equals(noun)) {
                System.out.println(player.getCrewMates());
            } else if ("map".equals(noun)) {
                map.showMap(this.currentLocation);
            }
        }
    }

    // Handles traveling between different locations in the map.
    private void travel(String noun) {
        JSONObject jObj = TextParser.readJsonFile(LOCATIONS_FILE);

        // First check and send you off to the island if you're sailing to the Island
        if (noun.equals("sail")) {
            if (player.getCrewMates().size() < 3) {
                System.out.printf("You don't have enough crew members to sail my friend!\n" +
                        "Continue searching for at least 3 members to \"Set Sail\" on ", player.getShipName());
                return;
            } else {
                sailToIsland(jObj);
            }
        }

        // check if valid route based on json locations for the current location
        if (!validateRoute(noun)) {
            //invalid route.
            System.out.println("Can not go to " + noun + " from " + this.currentLocation);
            return;
        }
        //Valid route.
        this.currentLocation = noun;
        //Get the JSON object for the target destinationS
        JSONObject location = (JSONObject) jObj.get(noun);
        if (location != null) {
            //JSON object found for the target destination
            map.showLocation(this.currentLocation);
            printCurrentLocation();
            String description = (String) location.get("description");
            System.out.println(description);
        } else {
            //JSON object NOT found for the target destination
            System.out.println("No JSON entry for: " + noun);
        }

    }


    private boolean validateRoute(String destination) {
        //Get the JSON Data for the current location
        JSONObject jObj = TextParser.readJsonFile(LOCATIONS_FILE);
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
        return false;
    }

    private void recruitCrewMember(String member) {
        JSONObject jObj = TextParser.readJsonFile(NPC_FILE);
        JSONObject npcs = (JSONObject) jObj.get(this.currentLocation);
        JSONObject npc = (JSONObject) npcs.get(member);

        if (npc != null) {
            String name = (String) npc.get("name");
            String ableToRecruit = (String) npc.get("ableToRecruit");
            if (ableToRecruit.equals("true")) {
                player.addCrewMate(name);
            }
            String recruitMsg = (String) npc.get("recruitMessage");
            System.out.println(recruitMsg); // print message out when you try to recruit them.
            System.out.println();
        } else {
            System.out.println("You cannot recruit " + member + ".");
        }
    }

    private void sailToIsland(JSONObject jObj) {
        if (player.getCrewMates().size() < 3) {
            System.out.printf("You don't have enough crew members to sail my friend!\n" +
                    "Continue searching for at least 3 members to \"Set Sail\" on ", player.getShipName());
            return;
        } else {
            this.currentLocation = "island";
            JSONObject location = (JSONObject) jObj.get(this.currentLocation);
            String description = (String) location.get("description");
            System.out.println(description);
            finale();
        }
    }


    private void startDialogue(String noun) {
        JSONObject jObj = TextParser.readJsonFile(NPC_FILE);
        JSONObject npcs = (JSONObject) jObj.get(this.currentLocation); // grab npcs in current location
        JSONObject npc = (JSONObject) npcs.get(noun); // grab specific npc based on text input


        if (npc != null) {
            this.dialogue = true;
            String greet = (String) npc.get("greeting");
            String ascii = (String) npc.get("image");
            System.out.println(greet + "\n");

//            printFile("data/"+ascii);

            JSONObject jsonObject = TextParser.readJsonFile(DIALOGUE_FILE);
            JSONObject area = (JSONObject) jsonObject.get(this.currentLocation);
            JSONObject person = (JSONObject) area.get(noun);
            JSONArray options = (JSONArray) person.get("options");
            JSONArray responses = (JSONArray) person.get("responses");

            while (dialogue) {
                JSONArray finalOptions = options;
                options.forEach((item) -> System.out.println((finalOptions.indexOf(item) + 1) + ". " + item.toString()));
                String input = (prompter.prompt("-> "));
                Integer response = null;
                if (input.matches("\\d+")) {
                    response = Integer.valueOf(input);
                } else if (input.equals("leave")) {
                    dialogue = false;
                    break;
                }
                if (response != null && response <= responses.size()) {
                    System.out.println(responses.get(response - 1));
                } else {
                    System.out.println("Sorry the option " + input + " is not a valid response. Please choose the numerical number next to the dialogue option.");
                }

            }

        } else {
            System.out.println("Sorry you cannot speak to " + noun + ".");
        }
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

    //method to clear the crew-mates after game over
    private void resetGame() {
        player.clearCrewMates();
    }

    void finale() {
        if (!player.getCrewMates().contains("mourner")) {
            TextParser.delay(300);
            System.out.println("You didn't have a navigator and got lost at sea. Sorry :(\n" +
                    "GAME OVER");
            gameOver = true;
        } else if (!player.getCrewMates().contains("zombie")) {
            TextParser.delay(300);
            System.out.println("As you were out at sea, you started sinking! \n" +
                    "You didn't have a shipwright and you sank to the bottom. Sorry :(\n" +
                    "GAME OVER");
            gameOver = true;
        } else if (!player.getCrewMates().contains("stranger")) {
            TextParser.delay(300);
            System.out.println("MUTINY! \n" +
                    "You sailed to the island and got the treasure all right.\n" +
                    "Then your crew left you on the deserted island :(\n" +
                    "Shoulda had a FirstMate\n\n" +
                    "GAME OVER");
            gameOver = true;
        } else {
            System.out.println("\nYou land on Yarginory Island, look around, and see a treasure chest just" +
                    "sitting on the beach! You approach it cautiously...\n\n");
            TextParser.delay(300);
            fight("greenbeard");
            gameOver = true;
        }
    }

    void fight(String name) {
        Enemy enemy = new Enemy(currentLocation, name);
        boolean fighting = true;

        // fight intro description -> pulled from enemy
        System.out.println(enemy.getIntro());

        // player attack, enemy attack loop
        while (fighting) {
            // if defeated, call gameOver
            if (player.getHealth() <= 0) {
                System.out.println("I, THE MIGHTY GREENBEARD HAVE KILLED YOU!!!");
                fighting = false;
                gameOver();
            } // if enemy is defeated, continue game
            else if (enemy.getHealth() <= 0) {
                System.out.println("OH NO, i have been defeated. And so i die  X_X");
                fighting = false;
                // RVB - where to adjust for other fights
                gameOver();
            }
            // player attack
            if (player.getHealth() >= 0 && enemy.getHealth() >= 0) {
                int playerDmg = player.getBaseDmg() + die.dmgRoll(player.getVariableDmg());
                enemy.setHealth(enemy.getHealth() - playerDmg);
                System.out.printf("Player does %d damage; Enemy health at %d\n", playerDmg, enemy.getHealth());
                TextParser.delay(200);
            }
            // enemy attack
            if (player.getHealth() >= 0 && enemy.getHealth() >= 0) {
                int enemyDmg = enemy.getBaseDmg() + die.dmgRoll(enemy.getVariableDmg());
                player.setHealth(player.getHealth() - enemyDmg);
                System.out.printf("Enemy does %d damage; Player health at %d\n", enemyDmg, player.getHealth());
                TextParser.delay(300);
            }
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}