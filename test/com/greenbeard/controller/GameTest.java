package com.greenbeard.controller;

import com.greenbeard.controller.Game;
import com.greenbeard.model.Player;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

public class GameTest {
    //
    private Player testPlayer;
    private Game game = new Game();


    @Before
    public void setUp(){
        testPlayer = new Player();
        game.setPlayer(testPlayer);
    }

    @Test
    public void returnLostText_whenNoNavigator() {
        System.out.println(testPlayer.getCrewMates());
        game.finale();
    }

    @Test
    public void returnsSuccessText_whenRightCrewMates() {
        testPlayer.setWeapon("pistol");
        testPlayer.addCrewMate("mourner");
        testPlayer.addCrewMate("zombie");
        testPlayer.addCrewMate("stranger");
        game.setCurrentLocation("island");
        System.out.println(testPlayer.getWeapon());
        System.out.println(testPlayer.getCrewMates());
        game.finale();
    }
}