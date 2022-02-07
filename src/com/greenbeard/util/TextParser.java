package com.greenbeard.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class TextParser {
    private static JSONParser jsonParser = new JSONParser();
    private static final long WORD_DELAY = 250; //250;

    private TextParser() {
    }

    public static JSONObject readJsonFile(String file) {
        JSONObject jObj = null;
        try (Reader readerDialogue = new FileReader(file)) {
            jObj = (JSONObject) jsonParser.parse(readerDialogue);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return jObj;
    }

    public static String checkSynonym(String command) {
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

    public static void printFile(String file) {
        try {
            List<String> text = Files.readAllLines(Path.of(file));
            text.forEach(line -> System.out.println(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printWordByWord(String line) {
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

    public static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}