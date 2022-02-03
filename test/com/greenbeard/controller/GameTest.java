package com.greenbeard.controller;

import com.greenbeard.model.GameMap;
import com.greenbeard.model.Location;
import com.greenbeard.model.Player;
import org.junit.Before;
import org.junit.Test;

public class GameTest {
    //
    private Player testPlayer;
    private Game game = new Game();
    private GameMap map = GameMap.getInstance();


    @Before
    public void setUp(){
        testPlayer = Player.getInstance();
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
        game.setCurrentLocation(map.getLocations().get("island"));
        System.out.println(testPlayer.getWeapon());
        System.out.println(testPlayer.getCrewMates());
        game.finale();
    }

    @Test
    public void successfullyFightZombie() {
        testPlayer.setWeapon("pistol");
        game.setCurrentLocation(map.getLocations().get("crypt"));
        game.fight("zombie");
    }
}