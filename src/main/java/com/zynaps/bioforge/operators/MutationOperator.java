package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomNumberGenerator;

public interface MutationOperator {
    void accept(Creature creature, RandomNumberGenerator random, double rate);
}

