//package com.greenbeard.util;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.Reader;
//import java.util.Arrays;
//import java.util.List;
//
//public final class TextParser {
//    private static JSONParser jsonParser = new JSONParser();
//
//    private TextParser() {
//    }
//
//    public static void parse(String input) {
//        List<String> commands = Arrays.asList(input.split(" "));
//
//        if(commands.size() != 2) {
//            System.out.println("Invalid Command");
//            return;
//        }
//
//        String verb = checkSynonym(commands.get(0));
//        String noun = commands.get(1);
//
//        try (Reader reader = new FileReader("data/locations/locations.json")) {
//            JSONObject jObj = (JSONObject) jsonParser.parse(reader);
//            JSONObject location = (JSONObject) jObj.get(this.currentLocation);
//
//            if ("go".equals(verb)) {
//                this.currentLocation = noun;
//                location = (JSONObject) jObj.get(this.currentLocation);
//                String description = (String) location.get("description");
//                System.out.println(description);
//            }
//            if (!currentLocation.equals("town") && "recruit".equals(verb)) {
//                JSONArray npcs = (JSONArray) location.get("npcs");
//
//                npcs.forEach(item -> {
//                    JSONObject npc = (JSONObject) item;
//                    String name = (String) npc.get("name");
//                    String ableToRecruit = (String) npc.get("ableToRecruit");
//                    if(name.equals(noun) && ableToRecruit.equals("true")) {
//                        player.addCrewMate(name);
//                    }
//                });
//            }
//            if ("set".equals(verb) && "sail".equals(noun)) {
//                this.currentLocation = "island";
//                location = (JSONObject) jObj.get(this.currentLocation);
//                String description = (String) location.get("description");
//                System.out.println(description);
//                ending();
//            }
//
//            System.out.println(player.getCrewMates());
//
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String checkSynonym(String command) {
//
//        try (Reader readerSynonym = new FileReader("data/synonyms.json")) {
//            JSONObject jObj = (JSONObject) jsonParser.parse(readerSynonym);
//            if (jObj.containsKey(command)) {
//                return command;
//            } else {
//                final String[] keyStr = {""};
//                jObj.forEach((key, val) -> {
//                    JSONArray arr = (JSONArray) val;
//                    if (arr.contains(command)) {
//                        keyStr[0] = String.valueOf(key);
//                    }
//                });
//                return keyStr[0];
//            }
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//        return "Required return statement here";
//    }
//
//}