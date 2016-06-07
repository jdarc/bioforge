package com.zynaps.bioforge.generators;

public class FastRandom implements RandomGenerator {

    private long state0 = (long)(Math.random() * Long.MAX_VALUE);
    private long state1 = (long)(Math.random() * Long.MAX_VALUE);

    @Override
    public int nextInt(int bound) {
        return bound <= 0 ? 0 : (int)(nextDouble() * bound);
    }

    @Override
    public double nextDouble() {
        return 0.5 + randomLong() * 0.5 / Long.MAX_VALUE;
    }

    private long randomLong() {
        long s1 = state0;
        long s0 = state1;
        state0 = s0;
        s1 ^= s1 << 23;
        state1 = s1 ^ s0 ^ s1 >>> 17 ^ s0 >>> 26;
        return state1 + s0;
    }
}
