package com.greenbeard.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadLine {
    public static void main(String[] args) throws IOException {
        File f = new File("data/welcome/welcome.txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        StringBuffer sb = new StringBuffer();
        String line;
        while((line=br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        fr.close();
        System.out.println(sb.toString());
        delay();
    }

    private static void delay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
