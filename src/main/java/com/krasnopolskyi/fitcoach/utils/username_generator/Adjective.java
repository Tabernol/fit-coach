package com.krasnopolskyi.fitcoach.utils.username_generator;

import java.util.Random;

public enum Adjective {
    AMAZING,
    BRAVE,
    CLEVER,
    DILIGENT,
    ENERGETIC,
    FRIENDLY,
    GRACEFUL,
    HAPPY,
    INVENTIVE,
    JOYFUL,
    KIND,
    LIVELY,
    MIGHTY,
    NOBLE,
    OPTIMISTIC;

    public static Adjective getRandomAdjective() {
        Random random = new Random();
        Adjective[] adjectives = Adjective.values();
        return adjectives[random.nextInt(adjectives.length)];
    }
}
