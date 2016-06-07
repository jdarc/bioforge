package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.RandomGenerator;
import com.zynaps.bioforge.operators.CrossoverOperator;
import com.zynaps.bioforge.operators.MutationOperator;
import com.zynaps.bioforge.schemes.SelectionScheme;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Tribe {

    Creature champion;
    List<Creature> parents;
    List<Creature> children;

    Tribe(int populationSize, int genomeSize) {
        champion = new Creature(genomeSize);
        parents = Stream.generate(() -> new Creature(genomeSize)).limit(populationSize).collect(Collectors.toList());
        children = Stream.generate(() -> new Creature(genomeSize)).limit(populationSize).collect(Collectors.toList());
    }

    void compete(ToDoubleFunction<Creature> callback) {
        parents.stream().forEach(creature -> creature.fitness = callback.applyAsDouble(creature));
        champion.mimic(parents.stream().sorted().findFirst().get());
    }

    void breed(SelectionScheme scheme, CrossoverOperator crossoverOperator, RandomGenerator random, double rate) {
        List<Creature> breeders = scheme.apply(new ArrayList<>(parents), random);
        children.stream().forEach(child -> crossoverOperator.accept(breeders.get(random.nextInt(breeders.size())),
                                                                    breeders.get(random.nextInt(breeders.size())),
                                                                    child, random, rate));
    }

    void mutate(MutationOperator mutationOperator, RandomGenerator random, double rate) {
        children.stream().forEach(child -> mutationOperator.accept(child, random, rate));
    }

    void swap() {
        List<Creature> swap = parents;
        parents = children;
        children = swap;
    }
}
