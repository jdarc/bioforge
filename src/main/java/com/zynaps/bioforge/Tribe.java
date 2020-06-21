package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.RandomNumberGenerator;
import com.zynaps.bioforge.operators.CrossoverOperator;
import com.zynaps.bioforge.operators.MutationOperator;
import com.zynaps.bioforge.schemes.SelectionScheme;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Tribe {

    final Creature champion;
    List<Creature> parents;
    List<Creature> children;

    Tribe(int populationSize, int genomeSize) {
        champion = new Creature(genomeSize);
        parents = Stream.generate(() -> new Creature(genomeSize)).limit(populationSize).collect(Collectors.toList());
        children = Stream.generate(() -> new Creature(genomeSize)).limit(populationSize).collect(Collectors.toList());
    }

    void compete(ToDoubleFunction<Creature> callback) {
        parents.forEach(creature -> creature.fitness = callback.applyAsDouble(creature));
        champion.mimic(parents.stream().sorted().findFirst().orElse(champion));
    }

    void breed(SelectionScheme scheme, CrossoverOperator crossoverOperator, RandomNumberGenerator random, double rate) {
        var breeders = scheme.apply(new ArrayList<>(parents), random);
        children.forEach(child -> crossoverOperator.accept(breeders.get(random.nextInt(breeders.size())),
                                                           breeders.get(random.nextInt(breeders.size())),
                                                           child, random, rate));
    }

    void mutate(MutationOperator mutationOperator, RandomNumberGenerator random, double rate) {
        children.forEach(child -> mutationOperator.accept(child, random, rate));
    }

    void swap() {
        var swap = parents;
        parents = children;
        children = swap;
    }
}
