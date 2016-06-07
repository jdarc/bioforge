package com.zynaps.bioforge.generators;

import java.util.concurrent.ThreadLocalRandom;

public class JavaRandom implements RandomGenerator {

    @Override
    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
}
