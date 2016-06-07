package com.zynaps.bioforge;

public class CreatureBuilder {

    private int genomizeSize = 100;
    private double fitness = 0.0;
    private boolean flip = false;

    public CreatureBuilder withGenomeSize(int value) {
        genomizeSize = value;
        return this;
    }

    public CreatureBuilder withFitness(double value) {
        fitness = value;
        return this;
    }

    public CreatureBuilder withFlip(boolean value) {
        flip = value;
        return this;
    }

    public Creature build() {
        Creature creature = new Creature(genomizeSize);
        creature.fitness = fitness;
        if (flip) {
            for (int i = 0; i < creature.dna.length; ++i) {
                creature.dna[i] = !creature.dna[i];
            }
        }
        return creature;
    }
}
