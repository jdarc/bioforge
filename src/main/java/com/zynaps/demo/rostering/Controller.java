package com.zynaps.demo.rostering;

import com.zynaps.bioforge.Island;

import java.util.List;

class Controller {

    private final Island population;
    private final Mapper mapper;
    private final Rules rules;
    private final int totalRequired;

    public Controller(Schedule schedule, Island population, Mapper mapper, Rules rules) {
        this.population = population;
        this.mapper = mapper;
        this.rules = rules;
        totalRequired = schedule.shifts.values().stream().map(s -> s.required).reduce(0, Integer::sum);
    }

    public List<Assignment> evolve(int seconds) {
        final long limit = System.currentTimeMillis() + seconds * 1000;
        double bestFitness = Double.NEGATIVE_INFINITY;
        boolean solutionFound = false;
        while (!solutionFound && System.currentTimeMillis() < limit) {
            population.evolve(creature -> rules.evaluate(mapper.decode(creature)));
            if (population.getChampion().getFitness() != bestFitness) {
                bestFitness = population.getChampion().getFitness();
                solutionFound = mapper.decode(population.getChampion()).size() == totalRequired;
                System.out.println(String.format("Mutation: %f, Crossover: %f, Generation: %d, Assignments: %d/%d, Score: %f",
                        population.getMutationRate(),
                        population.getCrossoverRate(),
                        population.getGeneration(),
                        mapper.decode(population.getChampion()).size(),
                        totalRequired,
                        population.getChampion().getFitness()));
            }
        }
        return mapper.decode(population.getChampion());
    }
}
