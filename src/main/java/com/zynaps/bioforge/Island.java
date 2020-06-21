package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.RandomNumberGenerator;
import com.zynaps.bioforge.operators.CrossoverOperator;
import com.zynaps.bioforge.operators.MutationOperator;
import com.zynaps.bioforge.operators.NOPCrossoverOperator;
import com.zynaps.bioforge.operators.NOPMutationOperator;
import com.zynaps.bioforge.schemes.SelectionScheme;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Island {

    final Tribe[] tribes;
    final Creature champion;
    CrossoverOperator crossoverOperator;
    MutationOperator mutationOperator;
    SelectionScheme scheme;
    RandomNumberGenerator random;
    double crossoverRate;
    double mutationRate;
    int generation;

    Island(int tribes, int populationSize, int genomeSize) {
        this.tribes = new Tribe[tribes];
        IntStream.range(0, tribes).forEach(i -> this.tribes[i] = new Tribe(populationSize, genomeSize));
        champion = new Creature(genomeSize);
        crossoverOperator = new NOPCrossoverOperator();
        mutationOperator = new NOPMutationOperator();
        scheme = (creatures, rnd) -> new ArrayList<>(creatures);
        random = () -> 0.0;
    }

    public Creature getChampion() {
        return champion;
    }

    public int getGeneration() {
        return generation;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double rate) {
        mutationRate = Math.max(0.0, Math.min(1.0, rate));
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double rate) {
        crossoverRate = Math.max(0.0, Math.min(1.0, rate));
    }

    public void useCrossoverOperator(CrossoverOperator operator) {
        crossoverOperator = operator;
    }

    public void useMutationOperator(MutationOperator operator) {
        mutationOperator = operator;
    }

    public void useSelectionScheme(SelectionScheme scheme) {
        this.scheme = scheme;
    }

    public void useRandomGenerator(RandomNumberGenerator random) {
        this.random = random;
    }

    public void zero() {
        Arrays.stream(tribes).forEach(tribe -> tribe.parents.forEach(Creature::zero));
    }

    public void nuke() {
        Arrays.stream(tribes).forEach(tribe -> tribe.parents.forEach(this::nukeCreature));
    }

    public void evolve(ToDoubleFunction<Creature> callback) {
        try {
            if (tribes.length == 1) {
                evolveTribe(callback);
            } else {
                evolveTribes(callback);
            }
            ++generation;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void nukeCreature(Creature creature) {
        IntStream.range(0, creature.dna.length).forEach(i -> creature.dna[i] = random.nextDouble() > 0.5);
    }

    private void evolveTribe(ToDoubleFunction<Creature> callback) {
        evolve(tribes[0], callback);
        champion.mimic(tribes[0].champion);
    }

    private void evolveTribes(ToDoubleFunction<Creature> callback) {
        List<Callable<Object>> todo = Arrays.stream(tribes)
                                            .map(tribe -> Executors.callable(() -> evolve(tribe, callback)))
                                            .collect(Collectors.toCollection(() -> new ArrayList<>(tribes.length)));
        ForkJoinPool.commonPool().invokeAll(todo);

        var best = Double.NEGATIVE_INFINITY;
        for (var tribe : tribes) {
            var creature = tribe.champion;
            if (creature.fitness >= best) {
                best = creature.fitness;
                champion.mimic(creature);
            }
        }

        if (random.nextDouble() > 0.5) {
            var tribeTo = tribes[random.nextInt(tribes.length)];
            var tribeFrom = tribes[random.nextInt(tribes.length)];
            for (var creature : tribeTo.parents) {
                creature.mimic(tribeTo.champion);
                if (random.nextDouble() < 0.5) {
                    var offset = random.nextInt(creature.getGenomeSize());
                    var length = random.nextInt(creature.getGenomeSize() - offset + 1);
                    System.arraycopy(tribeFrom.champion.dna, offset, creature.dna, offset, length);
                }
            }
        }
    }

    private void evolve(Tribe tribe, ToDoubleFunction<Creature> callback) {
        tribe.compete(callback);
        tribe.breed(scheme, crossoverOperator, random, crossoverRate);
        tribe.mutate(mutationOperator, random, mutationRate);
        tribe.swap();
    }
}
