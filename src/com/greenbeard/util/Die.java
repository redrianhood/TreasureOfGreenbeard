package com.greenbeard.util;

import java.util.Random;

public class Die {
    private static Random rand = new Random();

    public Die() {
    }

    public int dmgRoll(int value){
        return rand.nextInt(value + 1);
    }
}