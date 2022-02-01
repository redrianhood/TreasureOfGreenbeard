package com.greenbeard.controller;

import com.apps.util.Prompter;

import com.greenbeard.model.Enemy;
import com.greenbeard.model.Player;
import com.greenbeard.util.Die;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
;
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
    private Die die = new Die();
    private Prompter prompter = new Prompter(new Scanner(System.in));
    private String currentLocation = "town";
    private JSONParser jsonParser = new JSONParser();
    private boolean dialogue = false;

    private Scanner scanner = new Scanner(System.in);
    private Clip clip;
    private static long BANNER_DELAY = 0; //1500;
    private static final long WORD_DELAY = 0; //250;


    public void execute() {
        gameOver = false;
//        welcome();
        while (!gameOver) {
            if (player.getHealth() <= 0) {
                gameOver = true;
                break;
            }
            start();
        }
        gameOver();
    }

    private void audio(String fileName, int count) {
        try {
            // Stop previous audio clip, if any.


            if(clip != null) {
                clip.stop();
            }

            File audioFile = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
//            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//            volumeControl.setValue(-10.0f);
            clip.start();
            clip.loop(count);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void audioPreference() {
        if(clip==null) {
            return;
        }
        String response = "";
        System.out.println("Choose your audio preferences: ");
        while(response.trim().isEmpty()) {
            System.out.print("Do you want an audio on your background? (Y / N): ");
            response = scanner.next().trim().toUpperCase();

            if(response.length() >1) {
                response = response.charAt(0) + "";
            }


            switch (response) {
                case ("Y"):
                    this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                    break;
                case ("N"):
                    this.clip.stop();
                    break;
                default:
                    System.out.println("Not a Valid Response.");
                    audioPreference();
            }
        }
    }

    private void help() {
        System.out.println("\n\n");
        System.out.println("Your present location is: " + this.currentLocation);
        //showLocation();
        System.out.println("\n");
        System.out.println("You can chose to go to:\n" + getDestinations(this.currentLocation));

        System.out.println("\n\n");
        audioPreference();
    }

    private void welcome() {
        audio("data/audio/gamemusic.wav", Clip.LOOP_CONTINUOUSLY);
        System.out.println("\n\n");
        try {
            //Files.lines(Path.of("data/welcome/banner.txt")).forEach(System.out::println);
            Files.lines(Path.of("data/welcome/banner1.txt")).forEach(System.out::println);
            delay(BANNER_DELAY);
            Files.lines(Path.of("data/welcome/banner2.txt")).forEach(System.out::println);
            delay(BANNER_DELAY);
            Files.lines(Path.of("data/welcome/banner3.txt")).forEach(System.out::println);
            delay(BANNER_DELAY);

            audioPreference();
            System.out.println("\n\n");
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

    private void showLocation() {
        String pathFile = null;
        try {
            switch (this.currentLocation) {
                case "bar":
                case "cemetery":
                case "clinic":
                case "crypt":
                case "harbor":
                case "inn":
                case "island":
                case "town":
                    pathFile = "data/single-image-text/" + this.currentLocation + ".txt";
                    break;
                default:
                    System.out.println("Invalid Location you are trying to go" + this.currentLocation);
            }
            if(pathFile != null) {
                Files.lines(Path.of(pathFile)).forEach(System.out::println);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMap() {
        String filePath = null;
        try {
            switch (this.currentLocation) {

                case "bar":
                case "cemetery":
                case "clinic":
                case "crypt":
                case "harbor":
                case "inn":
                case "island":
                case "town":
                    filePath = "data/map/ascii-map-" + this.currentLocation + ".txt";
                    break;
                default:
                    System.out.println("There is nothing to show, verify your input>");
            }
            if(filePath!= null) {
                Files.lines(Path.of(filePath)).forEach(System.out::println);
            }
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

    //method delaying
    private void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        System.out.println("\n");
        //showLocation();

        System.out.println("\n");
        String input = prompter.prompt("What would you like to do?\n -> ").toLowerCase();
        showMap();
        List<String> commands = Arrays.asList(input.split(" "));

        if (commands.size() == 1) {
            if("help".equals(commands.get(0))) {
                help();
                return;
            }
        } else if (commands.size() !=2 ) {
            System.out.println("Invalid Command");
            return;
        }

        String verb = checkSynonym(commands.get(0));
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
            audio("data/audio/finalbattle.wav", Clip.LOOP_CONTINUOUSLY);
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
            }
        }
    }

    // Handles traveling between different locations in the map.
    private void travel(String noun) {
        JSONObject jObj = readJsonFile("data/locations/locations.json");
        //check if valid route based on json locations for the current location
        if (!validateRoute(noun)) {
            //invalid route.
            System.out.println("Can not go to " + noun + " from " + this.currentLocation);
            return;
        }

        if (noun.equals("sail")) {
            sailToIsland(jObj);
            return;
        }
        //Valid route.
        this.currentLocation = noun;
        //Get the JSON object for the target destinationS
        JSONObject location = (JSONObject) jObj.get(noun);
        if (location != null) {
            //JSON object found for the target destination
            String description = (String) location.get("description");
            System.out.println(description);
        } else {
            //JSON object NOT found for the target destination
            System.out.println("No JSON entry for: " + noun);
        }

    }


    private boolean validateRoute(String destination) {
        //Get the JSON Data for the current location
        JSONObject jObj = readJsonFile("data/locations/locations.json");
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
        JSONObject jObj = readJsonFile("data/npc.json");
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
        this.currentLocation = "island";
        JSONObject location = (JSONObject) jObj.get(this.currentLocation);
        String description = (String) location.get("description");
        System.out.println(description);
        finale();
    }


    private String checkSynonym(String command) {
        JSONObject jObj = readJsonFile("data/synonyms.json");
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
    }

    private void startDialogue(String noun) {
        JSONObject jObj = readJsonFile("data/npc.json");
        JSONObject npcs = (JSONObject) jObj.get(this.currentLocation); // grab npcs in current location
        JSONObject npc = (JSONObject) npcs.get(noun); // grab specific npc based on text input


        if (npc != null) {
            this.dialogue = true;
            String greet = (String) npc.get("greeting");
            String ascii = (String) npc.get("image");
            System.out.println(greet +"\n");

//            printFile("data/"+ascii);

            JSONObject jsonObject = readJsonFile("data/dialogue.json");
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
        if (player.getCrewMates().contains("mourner")) {
            System.out.println("\nYou land on Yarginory Island, look around, and see a treasure chest just" +
                    "sitting on the beach! You approach it cautiously...\n\n");
            fight("greenbeard");
        } else {
            System.out.println("You didn't have a navigator and got lost at sea. Sorry :(\n" +
                    "GAME OVER");
        }
        gameOver = true;
    }

    void fight(String name) {
        Enemy enemy = new Enemy(currentLocation, name);
        boolean fighting = true;

        // fight intro description -> pulled from enemy
        System.out.println(enemy.getIntro());

        // player attack, enemy attack loop
        while (fighting){
            // once one has 0 health print victory or defeat
            if (player.getHealth() <= 0){
                System.out.println("I, THE MIGHTY GREENBEARD HAVE KILLED YOU!!!");
                fighting = false;
            } else if (enemy.getHealth() <= 0){
                System.out.println("OH NO, i have been defeated. And so i die  X_X");
                fighting = false;
                gameOver = true;
            }
            // player attack
            if (player.getHealth() >= 0 && enemy.getHealth() >= 0){
                int playerDmg = player.getBaseDmg() + die.dmgRoll(player.getVariableDmg());
                enemy.setHealth(enemy.getHealth() - playerDmg);
                System.out.printf("Player does %d damage; Enemy health at %d\n", playerDmg, enemy.getHealth());
            }
            // enemy attack
            if (player.getHealth() >= 0 && enemy.getHealth() >= 0){
                int enemyDmg = enemy.getBaseDmg() + die.dmgRoll(enemy.getVariableDmg());
                player.setHealth(player.getHealth() - enemyDmg);
                System.out.printf("Enemy does %d damage; Player health at %d\n", enemyDmg, player.getHealth());
            }
        }
    }

    private JSONObject readJsonFile(String file) {
        JSONObject jObj = null;
        try (Reader readerDialogue = new FileReader(file)) {
            jObj = (JSONObject) jsonParser.parse(readerDialogue);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return jObj;
    }
    private void printFile(String file) {

        try {
            List<String> text = Files.readAllLines(Path.of(file));
            text.forEach(line -> System.out.println(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}