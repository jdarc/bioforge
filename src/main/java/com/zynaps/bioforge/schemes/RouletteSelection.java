package com.zynaps.bioforge.schemes;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouletteSelection implements SelectionScheme {

    private final double elitism;

    public RouletteSelection(double elitism) {
        this.elitism = 1.0 - Math.max(0.0, Math.min(1.0, elitism));
    }

    @Override
    public List<Creature> apply(List<Creature> creatures, RandomGenerator random) {
        Collections.sort(creatures);

        double minimumFitness = Double.POSITIVE_INFINITY;
        for (Creature creature : creatures) {
            minimumFitness = Math.min(minimumFitness, creature.getFitness()) - 1.0;
        }

        double totalFitness = 0.0;
        for (Creature creature : creatures) {
            totalFitness += creature.getFitness() - minimumFitness;
        }

        int total = creatures.size() * 2;
        List<Creature> breeders = new ArrayList<>(total);
        for (int i = 0; i < total; ++i) {
            double accum = 0.0;
            double slice = random.nextDouble() * totalFitness * elitism;
            Creature selected = null;
            for (int j = 0; j < creatures.size() && selected == null; ++j) {
                Creature creature = creatures.get(j);
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
