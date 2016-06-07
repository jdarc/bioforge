package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomGenerator;

public class SpliceCrossover implements CrossoverOperator {

    @Override
    public void accept(Creature mum, Creature dad, Creature kid, RandomGenerator random, double rate) {
        kid.mimic(mum);
        if (random.nextDouble() < rate) {
            int srcpos = random.nextInt(kid.getGenomeSize());
            int length = random.nextInt(kid.getGenomeSize() - srcpos + 1);
            int dstpos = random.nextInt(kid.getGenomeSize() - length + 1);
            kid.inherit(dad, srcpos, dstpos, length);
        }
    }
}
