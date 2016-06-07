package com.zynaps.demo.circles;

import com.zynaps.bioforge.Builder;
import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.Island;

import java.util.*;

class Controller {

    private static final int MINIMUM_RADIUS = 5;
    private static final int MAXIMUM_RADIUS = 13;
    private static final int NUMBER_STATIC_CIRCLES = 100;
    private static final double SCALER = 1.0 / 2097152.0;

    private List<Circle> circles;
    private Island island;
    private int width;
    private int height;

    public Controller() {
        circles = new ArrayList<>();
        island = new Builder().populationSize(100).genomeSize(63).build();
    }

    public Circle getChampionCircle() {
        return decodeToCircle(island.getChampion(), width * SCALER);
    }

    public Collection<Circle> getStaticCircles() {
        return Collections.unmodifiableCollection(circles);
    }

    public int getGeneration() {
        return island.getGeneration();
    }

    public double getFitness() {
        return island.getChampion().getFitness();
    }

    public void reset(int width, int height) {
        this.width = width;
        this.height = height;
        circles.clear();
        Random random = new Random();
        for (int i = 0; i < NUMBER_STATIC_CIRCLES; ++i) {
            int iterations = 10;
            double radius = MINIMUM_RADIUS + random.nextDouble() * (MAXIMUM_RADIUS - MINIMUM_RADIUS);
            while (--iterations > 0) {
                double x = radius + random.nextDouble() * (width - 2.0 * radius);
                double y = radius + random.nextDouble() * (height - 2.0 * radius);
                Circle circle = new Circle(x, y, radius);
                if (!circles.stream().anyMatch(circle::overlaps)) {
                    circles.add(circle);
                    iterations = 0;
                }
            }
        }
    }

    public void evolve() {
        island.evolve(creature -> {
            Circle circle = decodeToCircle(creature, width * SCALER);
            double fitness = circle.radius;
            for (Circle other : circles) {
                if (circle.overlaps(other) ||
                    (circle.x + circle.radius > width) ||
                    (circle.x - circle.radius < 0.0) ||
                    (circle.y + circle.radius > height) ||
                    (circle.y - circle.radius < 0.0)) {
                    fitness = 0.0;
                    break;
                }
            }
            return fitness;
        });
    }

    private static Circle decodeToCircle(Creature creature, double multiplier) {
        long dna = creature.extract(0, 63);
        double x = multiplier * (0x1FFFFF & dna);
        double y = multiplier * (0x1FFFFF & dna >> 21);
        double r = multiplier * (0x1FFFFF & dna >> 42);
        return new Circle(x, y, r);
    }
}
