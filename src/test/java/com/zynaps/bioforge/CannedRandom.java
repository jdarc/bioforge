package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.RandomGenerator;

public class CannedRandom implements RandomGenerator {

    private int index;
    private final double[] canned;

    public CannedRandom(double... values) {
        index = 0;
        canned = values;
    }

    @Override
    public double nextDouble() {
        return canned[index++ % canned.length];
    }
}
