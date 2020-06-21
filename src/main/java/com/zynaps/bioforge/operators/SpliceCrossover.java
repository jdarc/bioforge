package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomNumberGenerator;

public class SpliceCrossover implements CrossoverOperator {

    @Override
    public void accept(Creature mum, Creature dad, Creature kid, RandomNumberGenerator random, double rate) {
        kid.mimic(mum);
        if (random.nextDouble() < rate) {
            var srcpos = random.nextInt(kid.getGenomeSize());
            var length = random.nextInt(kid.getGenomeSize() - srcpos + 1);
            var dstpos = random.nextInt(kid.getGenomeSize() - length + 1);
            kid.inherit(dad, srcpos, dstpos, length);
        }
    }

}
