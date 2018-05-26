package com.treader.demo.util;

import java.util.Random;

public class RandomUtil {

    private static final Random random = new Random();

    public static int getRandom(int max) {
        return random.nextInt(max);
    }
}
