package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomNumberGenerator;

public interface CrossoverOperator {
    void accept(Creature mum, Creature dad, Creature kid, RandomNumberGenerator random, double rate);
}
