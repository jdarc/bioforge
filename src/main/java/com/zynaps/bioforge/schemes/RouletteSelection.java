package com.zynaps.bioforge.schemes;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouletteSelection implements SelectionScheme {

    private final double elitism;

    public RouletteSelection(double elitism) {
        this.elitism = 1.0 - Math.max(0.0, Math.min(1.0, elitism));
    }

    @Override
    public List<Creature> apply(List<Creature> creatures, RandomNumberGenerator random) {
        Collections.sort(creatures);

        var minimumFitness = Double.POSITIVE_INFINITY;
        for (var creature : creatures) {
            minimumFitness = Math.min(minimumFitness, creature.getFitness()) - 1.0;
        }

        var totalFitness = 0.0;
        for (var creature : creatures) {
            totalFitness += creature.getFitness() - minimumFitness;
        }

        var total = creatures.size() * 2;
        List<Creature> breeders = new ArrayList<>(total);
        for (var i = 0; i < total; ++i) {
            var accum = 0.0;
            var slice = random.nextDouble() * totalFitness * elitism;
            Creature selected = null;
            for (var j = 0; j < creatures.size() && selected == null; ++j) {
                var creature = creatures.get(j);
                accum += creature.getFitness() - minimumFitness;
                if (accum > slice) {
                    selected = creature;
                }
            }
            breeders.add(selected != null ? selected : creatures.get(0));
        }

        return breeders;
    }
}
