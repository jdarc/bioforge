package com.zynaps.bioforge.generators;

import java.util.concurrent.ThreadLocalRandom;

public class JavaRandomNumbers implements RandomNumberGenerator {

    @Override
    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
}
