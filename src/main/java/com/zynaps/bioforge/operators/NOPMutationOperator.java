package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomNumberGenerator;

public class NOPMutationOperator implements MutationOperator {

    @Override
    public void accept(Creature creature, RandomNumberGenerator random, double rate) {
    }
}

