package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomNumberGenerator;

import java.util.stream.IntStream;

public class BitFlipMutator implements MutationOperator {

    @Override
    public void accept(Creature creature, RandomNumberGenerator random, double rate) {
        IntStream.range(0, creature.getGenomeSize())
                 .filter(i -> random.nextDouble() < rate)
                 .forEach(creature::flip);
    }
}
