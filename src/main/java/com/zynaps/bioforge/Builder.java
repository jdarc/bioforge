package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.FastRandom;
import com.zynaps.bioforge.generators.RandomGenerator;
import com.zynaps.bioforge.operators.BitFlipMutator;
import com.zynaps.bioforge.operators.CrossoverOperator;
import com.zynaps.bioforge.operators.MutationOperator;
import com.zynaps.bioforge.operators.SpliceCrossover;
import com.zynaps.bioforge.schemes.SelectionScheme;
import com.zynaps.bioforge.schemes.TournamentSelection;

public class Builder {

    private CrossoverOperator crossoverOperator = new SpliceCrossover();
    private MutationOperator mutationOperator = new BitFlipMutator();
    private SelectionScheme scheme = new TournamentSelection(3);
    private RandomGenerator random = new FastRandom();
    private int tribes = 1;
    private int populationSize = 50;
    private int genomeSize = 64;
    private double crossoverRate = 0.5;
    private double mutationRate = 0.005;
    private boolean nuke = false;

    public Builder crossoverOperator(CrossoverOperator operator) {
        this.crossoverOperator = operator;
        return this;
    }

    public Builder mutationOperator(MutationOperator operator) {
        this.mutationOperator = operator;
        return this;
    }

    public Builder selectionScheme(SelectionScheme scheme) {
        this.scheme = scheme;
        return this;
    }

    public Builder randomGenerator(RandomGenerator random) {
        this.random = random;
        return this;
    }

    public Builder tribes(int total) {
        this.tribes = Math.max(1, total);
        return this;
    }

    public Builder populationSize(int size) {
        this.populationSize = Math.max(2, size);
        return this;
    }

    public Builder genomeSize(int size) {
        this.genomeSize = Math.max(8, size);
        return this;
    }

    public Builder crossoverRate(double rate) {
        this.crossoverRate = Math.max(0.0, Math.min(1.0, rate));
        return this;
    }

    public Builder mutationRate(double rate) {
        this.mutationRate = Math.max(0.0, Math.min(1.0, rate));
        return this;
    }

    public Builder nuke(boolean nuke) {
        this.nuke = nuke;
        return this;
    }

    public Island build() {
        Island island = new Island(tribes, populationSize, genomeSize);
        island.useCrossoverOperator(crossoverOperator);
        island.useMutationOperator(mutationOperator);
        island.useSelectionScheme(scheme);
        island.useRandomGenerator(random);
        island.setCrossoverRate(crossoverRate);
        island.setMutationRate(mutationRate);
        if (nuke) {
            island.nuke();
        }
        return island;
    }
}
