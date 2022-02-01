package com.greenbeard.client;

import com.greenbeard.controller.Game;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();
        game.execute();
    }
}