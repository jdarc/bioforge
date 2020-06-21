package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.RandomNumberGenerator;

public class CannedRandomNumber implements RandomNumberGenerator {

    private int index;
    private final double[] canned;

    public CannedRandomNumber(double... values) {
        index = 0;
        canned = values;
    }

    @Override
    public double nextDouble() {
        return canned[index++ % canned.length];
    }
}
