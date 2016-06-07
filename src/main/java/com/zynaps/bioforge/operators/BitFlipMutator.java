package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomGenerator;

public class BitFlipMutator implements MutationOperator {

    @Override
    public void accept(Creature creature, RandomGenerator random, double rate) {
        for (int i = 0; i < creature.getGenomeSize(); ++i) {
            if (random.nextDouble() < rate) {
                creature.flip(i);
            }
        }
    }
}
