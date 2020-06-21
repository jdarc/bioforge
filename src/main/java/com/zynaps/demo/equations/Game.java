package com.zynaps.demo.equations;

import com.zynaps.bioforge.Builder;
import java.util.Arrays;
import java.util.Random;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static java.lang.Double.parseDouble;

class Game {
    private static final Random RANDOM = new Random(System.nanoTime());
    private static final int SHUFFLE_COUNT = 64;
    private static final int TOTAL_NUMBERS = 6;
    private final int[] numbers;
    private final int target;
    private ScriptEngine engine;

    public Game(int totalLarge) {
        target = RANDOM.nextInt(998) + 1;
        numbers = new int[TOTAL_NUMBERS];

        selectLargeNumbers(clamp(totalLarge, 1, 4));
        selectSmallNumbers(clamp(totalLarge, 1, 4));
    }

    private static int[] shuffle(int... numbers) {
        for (var times = 0; times < SHUFFLE_COUNT; ++times) {
            swap(numbers, RANDOM.nextInt(numbers.length), RANDOM.nextInt(numbers.length));
        }
        return numbers;
    }

    private static void swap(int[] numbers, int i, int j) {
        var temp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = temp;
    }

    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) {
            return min;
        }
        if (val.compareTo(max) > 0) {
            return max;
        }
        return val;
    }

    public String describe() {
        return "Numbers: " + Arrays.toString(numbers) +
               System.lineSeparator() +
               "Target: " + target +
               System.lineSeparator();
    }

    public void run() {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");

        var population = new Builder().tribes(4)
                                      .populationSize(50)
                                      .genomeSize(32)
                                      .crossoverRate(0.5)
                                      .mutationRate(0.005)
                                      .build();

        var lastResult = 0.0;
        do {
            population.evolve(creature -> {
                var formula = new Formula(creature);
                if (formula.isValid()) {
                    return target - Math.abs(target - evaluate(formula.toEquation(numbers)));
                }
                return 0.0;
            });

            var formula = new Formula(population.getChampion()).toEquation(numbers);
            var result = evaluate(formula);
            if (lastResult != result) {
                lastResult = result;
                System.out.println(String.format("Generation [%d]: %s = %s", population.getGeneration(), formula, result));
            }
        } while (evaluate(new Formula(population.getChampion()).toEquation(numbers)) != target);

        var formula = new Formula(population.getChampion()).toEquation(numbers);
        System.out.println(String.format("Solution: %s = %s", formula, evaluate(formula)));
    }

    private double evaluate(String equation) {
        try {
            return parseDouble(engine.eval(equation).toString());
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectLargeNumbers(int totalLarge) {
        System.arraycopy(shuffle(25, 50, 75, 100), 0, numbers, 0, totalLarge);
    }

    private void selectSmallNumbers(int totalLarge) {
        System.arraycopy(shuffle(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 0, numbers, totalLarge, TOTAL_NUMBERS - totalLarge);
    }
}
