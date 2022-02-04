package com.greenbeard.controller;

import com.apps.util.Console;
import com.apps.util.Prompter;


import com.greenbeard.model.*;
import com.greenbeard.model.Character;
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
    private GameMap map = GameMap.getInstance();
    private boolean cryptFight = true;
    private Player player = Player.getInstance();
    private Die die = new Die();
    private Audio audio = new Audio();
    private Prompter prompter = new Prompter(new Scanner(System.in));

    private Location currentLocation = map.getLocations().get("town");
    private Scanner scanner = new Scanner(System.in);
    private static long BANNER_DELAY = 0; //1500;
    private static final String DIALOGUE_FILE = "data/dialogue.json";

    public void execute() {
        gameOver = false;
        welcome();
        map.showLocation(this.currentLocation.getBasicName());
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
        System.out.println("\n");
        printCurrentLocation();
        map.availableCommand(this.currentLocation);
//        System.out.println("\n");
        showCharacters(this.currentLocation);
        System.out.println("========================\n");
        //audio.audioPreference();
    }

    private void printCurrentLocation() {
        System.out.println("Your current location is: " + ColorConsole.RED_BOLD + (this.currentLocation.getBasicName())+ ": " + this.currentLocation.getName() + ColorConsole.RESET);
        System.out.println();
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
            player.setShipName(prompter.prompt("What is the name of your Ship? -> "));
            String weapon = prompter.prompt("What kind of weapon do you carry?\n" +
                    "Options are: " + ColorConsole.CYAN_BOLD + "sword, or pistol" + ColorConsole.RESET + "\n  --> ", "sword|pistol", "Invalid selection");
            player.setWeapon(weapon);
            // Console.clear here (?)
            System.out.printf("\n\nYou are the Great Captain %s, Captain of the %s.\n" +
                            "With your trusty %s by your side, you set off to town.%n",
                    player.getName(), player.getShipName(), player.getWeaponName());
            intro.forEach((line) -> {
                TextParser.printWordByWord(line);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        String input = prompter.prompt("\nWhat would you like to do?\n -> ").toLowerCase();
        List<String> commands = Arrays.asList(input.split(" "));

        // Help and Quit commands
        if (commands.size() == 1) {
            if ("help".equals(commands.get(0))) {
                help();
                return;
            }
            else if ("quit".equals(commands.get(0))){
                String response = prompter.prompt("\nAre you sure you want to quit? (y/n) ", "y|n", "Invalid Selection");
                if ("y".equals(response)) {
                    gameOver = true;
                }
                return;
            }
        }

        if (commands.size() != 2) {
            System.out.println("Invalid Command");
            return;
        }

        String verb = TextParser.checkSynonym(commands.get(0));
        String noun = commands.get(1);

        if ("go".equals(verb)) {
            travel(noun);
        }

        // recruit an npc for your crew
        else if (!currentLocation.equals("town") && "recruit".equals(verb)) {
            recruitCrewMember(noun);
        }
        // set sail for island when ready for final boss
        else if ("set".equals(verb) && "sail".equals(noun) && "harbor".equals(this.currentLocation.getBasicName())) {
            if (audio.isMusicOn()) {
                audio.play("data/audio/finalbattle.wav", Clip.LOOP_CONTINUOUSLY);
                audio.setVolumeLevel(audio.getVolumePreference());
            }
            travel(noun);
        }
        // talking to someone
        else if (!currentLocation.equals("town") && "talk".equals(verb)) {
            startDialogue(noun);
        }

        //show current crew members
        else if ("look".equals(verb)) {
            if ("crew".equals(noun)) {
                System.out.println(ColorConsole.PURPLE_BOLD + player.getCrewMates() + ColorConsole.RESET);
            } else if ("map".equals(noun)) {
                map.showMap(this.currentLocation.getBasicName());
            }
        } else {
            System.out.println("Sorry the command you typed is not recognized");
        }
    }

//    private void actionOptions() {
//        System.out.println("Current possible actions: ");
////        StringBuilder builder = new StringBuilder();
////        currentLocation.getActionOptions().forEach(option -> builder.append(option).append(" "));
////        String actions = builder.toString();
////        actions.replace(" ", ", ");
////        System.out.println(actions);
//        System.out.println(String.join(", ", currentLocation.getActionOptions()));
//        System.out.println("==============");
//    }

    private void showCharacters(Location location) {
        List<String> characterList = new ArrayList<>();
        List<String> recruitList = new ArrayList<>();

        if (location == null) {
            //location not found
            return;
        }
        //ge the list of people found in the location
        //Iterate through JSONObject keys:
        location.getNpcs().forEach((key, value) -> {
            //add each character name to list
            Character npc =  value;
            characterList.add( npc.getName());

            //check if character can be recruited
            if (npc.isAbleToRecruit()) {
                //add character to recruit list
                recruitList.add(value.getName());
            }
        });
        System.out.println("You can talk to: " + ColorConsole.BLUE_ITALIC + characterList + ColorConsole.RESET);
        System.out.println("You can recruit: " + ColorConsole.BLUE_ITALIC + recruitList + ColorConsole.RESET);
    }

    // Handles traveling between different locations in the map.
    private void travel(String noun) {
        Console.clear();
      
        // First check and send you off to the island if you're sailing to the Island
        if (noun.equals("sail")) {
            sailToIsland();
        }

        // check if valid route based on json locations for the current location
        if (!validateRoute(noun)) {
            //invalid route.
            System.out.println("Can not go to " + noun + " from " + this.currentLocation.getBasicName());
            return;
        }
        //Valid route.
        setCurrentLocation(map.getLocations().get(noun));
        // Initiate combat if player enter the crypt for the first time
        if (cryptFight && "crypt".equals(noun)) {
            fight("zombie");
            cryptFight = false;
        }

        //Get the JSON object for the target destinationS
        if (currentLocation != null) {
            //JSON object found for the target destination
            map.showLocation(this.currentLocation.getBasicName());
            printCurrentLocation();
            String description = this.currentLocation.getDescription();
            System.out.println(description + "\n");

        } else {
            //JSON object NOT found for the target destination
            System.out.println("No JSON entry for: " + noun);
        }

    }

    private boolean validateRoute(String destination) {
        return map.getLocations().get(destination) != null;
    }


    private void recruitCrewMember(String member) {
        Character npc = this.currentLocation.getNpcs().get(member);

        if (npc != null) {
            String name = npc.getName();
            boolean ableToRecruit = npc.isAbleToRecruit();
            if (ableToRecruit) {
                    player.addCrewMate(name);
            }
            String recruitMsg = npc.getRecruitMessage();
            System.out.println(recruitMsg); // print message out when you try to recruit them.
            System.out.println();
        } else {
            System.out.println("You cannot recruit " + member + ".");
        }
    }

    private void sailToIsland() {
        if (player.getCrewMates().size() < 3) {
            System.out.printf("You don't have enough crew members to sail my friend!\n" +
                    "Continue searching for at least 3 members to \"Set Sail\" on ", ColorConsole.BLACK_BOLD + player.getShipName());
            return;
        } else {
            setCurrentLocation(map.getLocations().get("island"));
            String description = this.currentLocation.getDescription();
            System.out.println(description);
            finale();
            gameOver();
        }
    }

    private void startDialogue(String noun) {
        Character npc = this.currentLocation.getNpcs().get(noun);

        if (npc != null) {
            this.dialogue = true;
            String greet = npc.getGreeting();
            String ascii = npc.getImage();
            System.out.println(greet + "\n");

//            printFile("data/"+ascii);

            JSONObject jsonObject = TextParser.readJsonFile(DIALOGUE_FILE);
            JSONObject area = (JSONObject) jsonObject.get(this.currentLocation.getBasicName());
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
                    System.out.println("->" + responses.get(response - 1));
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
        cryptFight = true;
        currentLocation = map.getLocations().get("town");
        execute();
    }

    void finale() {
        // if crew doesn't have a navigator
        if (!player.getCrewMates().contains("mourner")) {
            TextParser.delay(300);
            System.out.println("You didn't have a navigator and got lost at sea. Sorry :(\n" +
                    "GAME OVER");
            gameOver = true;
        } // if crew doesn't have a shipwright
        else if (!player.getCrewMates().contains("zombie")) {
            TextParser.delay(300);
            System.out.println("As you were out at sea, you started sinking! \n" +
                    "You didn't have a shipwright and you sank to the bottom. Sorry :(\n" +
                    "GAME OVER");
            gameOver = true;
        } // if crew doesn't have a firstmate
        else if (!player.getCrewMates().contains("stranger")) {
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
        Enemy enemy = (Enemy) this.currentLocation.getNpcs().get(name);
        System.out.println(enemy);
        // reset health before each fight
        player.setHealth(100);


        // fight intro description -> pulled from enemy
        System.out.println(enemy.getIntro());

        boolean fighting = true;
        // player attack, enemy attack loop
        while (fighting) {
            // display:
            // "3 Attacks available: \n Strong, Guarded, Normal
            // prompt for valid input
            // calculate appropriate dmg
            // (is someone dead?)
            // calculate enemy dmg
            // (is someone dead?)
            // loop

            // Ask for what kind of attack and calculate damage
            boolean validInput = false;
            // reset guard for round
            boolean guarded = false;
            int playerDmg = 0;

            System.out.println("Pick an attack type:\nStrong, Guarded, Normal  {s/g/n}");
            while (!validInput) {
                String response = scanner.next().trim().toLowerCase();
                switch (response) {
                    // double the damage roll
                    case ("strong"):
                    case ("s"):
                        playerDmg = player.getBaseDmg() * 2 + die.dmgRoll(player.getVariableDmg());
                        validInput = true;
                        break;
                    case ("guarded"):
                    case ("g"):
                        guarded = true;
                        playerDmg = player.getBaseDmg() + die.dmgRoll(player.getVariableDmg());
                        validInput = true;
                        break;
                    case ("normal"):
                    case ("n"):
                        playerDmg = player.getBaseDmg() + die.dmgRoll(player.getVariableDmg());
                        validInput = true;
                        break;
                }
            }

            // player attack
            if (player.getHealth() >= 0 && enemy.getHealth() >= 0) {
                enemy.setHealth(enemy.getHealth() - playerDmg);
                System.out.printf("Player does %d damage; Enemy health at %d\n", playerDmg, enemy.getHealth());
                TextParser.delay(200);
            }
            // if enemy is defeated, end fight and continue game
            if (enemy.getHealth() <= 0) {
                System.out.println(enemy.getVictory());
                fighting = false;
            }
            // enemy attack
            if (player.getHealth() >= 0 && enemy.getHealth() >= 0) {
                int enemyDmg = enemy.getBaseDmg() + die.dmgRoll(enemy.getVariableDmg());
                if (guarded & enemyDmg >= 8) {
                    enemyDmg -= 8;
                } else if (guarded) {
                    enemyDmg = 0;
                }
                player.setHealth(player.getHealth() - enemyDmg);
                System.out.printf("Enemy does %d damage; Player health at %d\n", enemyDmg, player.getHealth());
                TextParser.delay(300);
            }
            // if defeated, call gameOver
            if (player.getHealth() <= 0) {
                System.out.println(enemy.getDefeat());
                fighting = false;
                gameOver();
            }
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}