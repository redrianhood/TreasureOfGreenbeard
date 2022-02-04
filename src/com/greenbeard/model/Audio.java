package com.greenbeard.model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Audio {
    public static final double DEFAULT_VOLUME = .1d *1;
    private Clip clip;
    private FloatControl gainControl;
    private Scanner scanner = new Scanner(System.in);
    private boolean musicOn;
    private double volumePreference;

    public void play(String fileName, int count) {
        try {
            // Stop previous audio clip, if any.
            if(clip != null) {
                clip.stop();
            }
            File audioFile = new File(fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            //set the volume with master gain control
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            //start playing clip
            clip.start();
            clip.loop(count);
            //check if volume is not zero
            if(volumePreference !=0) {
                setVolumeLevel(volumePreference);
            } else {
                setVolumeLevel(DEFAULT_VOLUME);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void audioPreference() {
        if (clip == null) {
            return;
        }
        String response = "";
        System.out.println("Choose your audio preferences: ");
        while (response.trim().isEmpty()) {
            System.out.print("Do you want an audio on your background? (Y / N): ");
            response = scanner.next().trim().toUpperCase();

            if (response.length() > 1) {
                response = response.charAt(0) + "";
            }
            switch (response) {
                case ("Y"):
                    this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                    musicOn = true;
                    break;
                case ("N"):
                    this.clip.stop();
                    musicOn = false;
                    break;
                default:
                    System.out.println("Not a Valid Response.");
                    audioPreference();
            }
        }
        if ("Y".equals(response)) {
            setVolumeLevelPreference();
        }
    }

    public void setVolumeLevelPreference() {
        if (clip == null) {
            return;
        }
        String response = "";
        while (response.trim().isEmpty()) {
            System.out.print("Choose your volume preferences level (1 to 10): ");
            response = scanner.next().trim();

            //must be either single or double digit ( 1 to 2 characters)

            if (response.length() < 1 || response.length() > 2) {
                System.out.println("Not a Valid Response.");
                setVolumeLevelPreference();
            }

            try {
                int volChoice = Integer.parseInt(response);
                if (volChoice < 1 || volChoice > 10) {
                    System.out.println("Not a Valid Response. Enter between 1 to 10");
                }

                System.out.println();
                volumePreference = volChoice * 0.1d;

                setVolumeLevel(volumePreference);
                break;

            } catch (Exception e) {
                System.out.println("Not a valid response. Enter between 1 to 10");
                setVolumeLevelPreference();
            }
        }
    }

    public void setVolumeLevel(double vol) {
        //System.out.println("Setting volume to: " +  vol*10);
        if (clip == null || !clip.isActive() || !clip.isOpen()) {
            return;
        }
        double gain = vol;
        float db = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(db);
    }

    public double getVolumePreference() {
        return volumePreference;
    }

    public boolean isMusicOn() {
        return musicOn;
    }
}