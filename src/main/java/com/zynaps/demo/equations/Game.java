package com.zynaps.demo.equations;

import java.util.Arrays;
import java.util.Random;

class Game {

    private static final Random RANDOM = new Random(System.nanoTime());
    private static final int SHUFFLE_COUNT = 64;

    public final int target;
    private int[] numbers;

    public Game(int totalNumbers, int totalLarge, int minimumTarget, int maximumTarget) {
        target = minimumTarget + RANDOM.nextInt(maximumTarget - minimumTarget);
        numbers = new int[totalNumbers];
        System.arraycopy(shuffle(25, 50, 75, 100), 0, numbers, 0, totalLarge);
        System.arraycopy(shuffle(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), totalLarge, numbers, totalLarge, totalNumbers - totalLarge);
    }

    public int getTarget() {
        return target;
    }

    public String getDescription() {
        return "Numbers: " + Arrays.toString(numbers) + System.lineSeparator() + "Target: " + target;
    }

    public String mixFormula(Formula formula) {
        return formula.toFormula(numbers);
    }

    private static int[] shuffle(int... numbers) {
        for (int t = 0; t < SHUFFLE_COUNT; ++t) {
            int i = RANDOM.nextInt(numbers.length);
            int j = RANDOM.nextInt(numbers.length);
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
        return numbers;
    }
}
