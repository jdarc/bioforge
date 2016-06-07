package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.RandomGenerator;
import com.zynaps.bioforge.operators.CrossoverOperator;
import com.zynaps.bioforge.operators.MutationOperator;
import com.zynaps.bioforge.schemes.SelectionScheme;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.ToDoubleFunction;

public class Island {

    final Tribe[] tribes;
    final Tribe firstTribe;
    final Creature champion;
    CrossoverOperator crossoverOperator;
    MutationOperator mutationOperator;
    SelectionScheme scheme;
    RandomGenerator random;
    ExecutorService service;
    double crossoverRate;
    double mutationRate;
    int generation;

    Island(int tribes, int populationSize, int genomeSize) {
        this.tribes = new Tribe[tribes];
        for (int i = 0; i < tribes; ++i) {
            this.tribes[i] = new Tribe(populationSize, genomeSize);
        }
        firstTribe = this.tribes[0];
        champion = new Creature(genomeSize);
        crossoverOperator = (mum, dad, kid, rnd, rate) -> {
        };
        mutationOperator = (creature, rnd, rate) -> {
        };
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

    public void useRandomGenerator(RandomGenerator random) {
        this.random = random;
    }

    public void zero() {
        for (Tribe tribe : tribes) {
            tribe.parents.forEach(Creature::zero);
        }
    }

    public void nuke() {
        for (Tribe tribe : tribes) {
            tribe.parents.forEach(creature -> {
                for (int i = 0; i < creature.dna.length; ++i) {
                    creature.dna[i] = random.nextDouble() > 0.5;
                }
            });
        }
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
            e.printStackTrace();
        }
    }

    private void evolveTribe(ToDoubleFunction<Creature> callback) {
        evolve(firstTribe, callback);
        champion.mimic(firstTribe.champion);
    }

    private void evolveTribes(ToDoubleFunction<Creature> callback) throws InterruptedException {
        List<Callable<Object>> todo = new ArrayList<>(tribes.length);
        for (Tribe tribe : tribes) {
            todo.add(Executors.callable(() -> {
                evolve(tribe, callback);
            }));
        }
        (service = service == null ? new ForkJoinPool() : service).invokeAll(todo);

        double best = Double.NEGATIVE_INFINITY;
        for (Tribe tribe : tribes) {
            Creature creature = tribe.champion;
            if (creature.fitness >= best) {
                best = creature.fitness;
                champion.mimic(creature);
            }
        }

        if (random.nextDouble() > 0.5) {
            Tribe tribeTo = tribes[random.nextInt(tribes.length)];
            Tribe tribeFrom = tribes[random.nextInt(tribes.length)];
            for (Creature creature : tribeTo.parents) {
                creature.mimic(tribeTo.champion);
                if (random.nextDouble() < 0.5) {
                    int offset = random.nextInt(creature.getGenomeSize());
                    int length = random.nextInt(creature.getGenomeSize() - offset + 1);
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
