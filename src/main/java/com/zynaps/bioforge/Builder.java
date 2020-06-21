package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.JavaRandomNumbers;
import com.zynaps.bioforge.generators.RandomNumberGenerator;
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
    private RandomNumberGenerator random = new JavaRandomNumbers();
    private int tribes = 1;
    private int populationSize = 50;
    private int genomeSize = 64;
    private double crossoverRate = 0.5;
    private double mutationRate = 0.005;
    private boolean nuke = false;

    public Builder crossoverOperator(CrossoverOperator operator) {
        crossoverOperator = operator;
        return this;
    }

    public Builder mutationOperator(MutationOperator operator) {
        mutationOperator = operator;
        return this;
    }

    public Builder selectionScheme(SelectionScheme scheme) {
        this.scheme = scheme;
        return this;
    }

    public Builder randomGenerator(RandomNumberGenerator random) {
        this.random = random;
        return this;
    }

    public Builder tribes(int total) {
        tribes = Math.max(1, total);
        return this;
    }

    public Builder populationSize(int size) {
        populationSize = Math.max(2, size);
        return this;
    }

    public Builder genomeSize(int size) {
        genomeSize = Math.max(8, size);
        return this;
    }

    public Builder crossoverRate(double rate) {
        crossoverRate = Math.max(0.0, Math.min(1.0, rate));
        return this;
    }

    public Builder mutationRate(double rate) {
        mutationRate = Math.max(0.0, Math.min(1.0, rate));
        return this;
    }

    public Builder nuke(boolean nuke) {
        this.nuke = nuke;
        return this;
    }

    public Island build() {
        var island = new Island(tribes, populationSize, genomeSize);
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
