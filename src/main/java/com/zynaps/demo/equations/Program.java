package com.zynaps.demo.equations;

import com.zynaps.bioforge.Builder;
import com.zynaps.bioforge.Island;

class Program {

    public static void main(String[] args) {
        new Program().run();
    }

    private void run() {
        Game game = new Game(6, 2, 1, 999);
        System.out.println("Genetic Algorithm - Equations");
        System.out.println("");
        System.out.println(game.getDescription());
        System.out.println("");

        double lastResult = 0.0;
        boolean[] solutionFound = {false};
        Island population = new Builder().tribes(4)
                                         .populationSize(50)
                                         .genomeSize(32)
                                         .crossoverRate(0.5)
                                         .mutationRate(0.005)
                                         .build();

        while (!solutionFound[0]) {
            population.evolve(creature -> {
                Formula formula = new Formula(creature.extract(0, 32));
                if (formula.isValid()) {
                    double result = Calculator.evaluate(game.mixFormula(formula));
                    if (result == game.getTarget()) {
                        solutionFound[0] = true;
                    }
                    return game.getTarget() - Math.abs(game.getTarget() - result);
                }
                return 0.0;
            });

            String formula = game.mixFormula(new Formula(population.getChampion().extract(0, 32)));
            double result = Calculator.evaluate(formula);
            if (lastResult != result) {
                lastResult = result;
                System.out.println(String.format("Generation: %d\t Best: %s\t Result: %s", population.getGeneration(), formula, result));
            }
        }

        String formula = game.mixFormula(new Formula(population.getChampion().extract(0, 32)));
        System.out.println(String.format("Solution: %s = %s", formula, Calculator.evaluate(formula)));
    }
}
