package com.greenbeard.util;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class DieTest{
    private Die die;

    @Before
    public void setUp() {
        die = new Die();
    }

    @Test
    public void dmgRoll_returnsMinAndMaxInclusive() {
        Set<Integer> valueTest = new HashSet<>();
        for(int i = 0; i < 100; i++){
            valueTest.add(die.dmgRoll(10));
        }
        System.out.println(valueTest);
    }
}