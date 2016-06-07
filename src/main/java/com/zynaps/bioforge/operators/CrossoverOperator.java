package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomGenerator;

public interface CrossoverOperator {

    void accept(Creature mum, Creature dad, Creature kid, RandomGenerator random, double rate);
}
