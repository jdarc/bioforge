package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomGenerator;

public interface MutationOperator {

    void accept(Creature creature, RandomGenerator random, double rate);
}
