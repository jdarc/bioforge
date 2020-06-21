package com.zynaps.bioforge.generators;

public interface RandomNumberGenerator {

    default int nextInt(int bound) {
        return bound <= 0 ? 0 : (int) (bound * nextDouble());
    }

    double nextDouble();
}
