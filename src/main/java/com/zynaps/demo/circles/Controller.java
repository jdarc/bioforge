package com.zynaps.demo.circles;

import com.zynaps.bioforge.Builder;
import com.zynaps.bioforge.Island;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

class Controller {

    private static final int MINIMUM_RADIUS = 5;
    private static final int MAXIMUM_RADIUS = 13;
    private static final int NUMBER_STATIC_CIRCLES = 50;
    private static final double SCALER = 1.0 / 2097152.0;

    private final List<Circle> staticCircles;
    private Island island;
    private int width;
    private int height;

    public Controller() {
        staticCircles = new ArrayList<>(NUMBER_STATIC_CIRCLES);
    }

    public Circle getChampionCircle() {
        return new Circle(getIsland().getChampion(), width * SCALER);
    }

    public Collection<Circle> getStaticCircles() {
        return staticCircles;
    }

    public int getGeneration() {
        return getIsland().getGeneration();
    }

    public double getFitness() {
        return getIsland().getChampion().getFitness();
    }

    public void reset(int width, int height) {
        this.width = width;
        this.height = height;
        island = null;
        generateStaticCircles(width, height);
    }

    public void evolve() {
        getIsland().evolve(creature -> {
            Circle circle = new Circle(creature, width * SCALER);
            if (isOutsideViewport(circle) || getStaticCircles().stream().anyMatch(circle::overlaps)) {
                return 0.0;
            }
            return circle.radius;
        });
    }

    private void generateStaticCircles(int width, int height) {
        staticCircles.clear();
        Random random = new Random();
        for (int i = 0; i < NUMBER_STATIC_CIRCLES; ++i) {
            int iterations = 10;
            double radius = MINIMUM_RADIUS + random.nextDouble() * (MAXIMUM_RADIUS - MINIMUM_RADIUS);
            while (--iterations > 0) {
                double x = radius + random.nextDouble() * (width - 2.0 * radius);
                double y = radius + random.nextDouble() * (height - 2.0 * radius);
                Circle circle = new Circle(x, y, radius);
                if (staticCircles.stream().noneMatch(circle::overlaps)) {
                    staticCircles.add(circle);
                    iterations = 0;
                }
            }
        }
    }

    private boolean isOutsideViewport(Circle circle) {
        return (circle.x + circle.radius > width) ||
                (circle.x - circle.radius < 0.0) ||
                (circle.y + circle.radius > height) ||
                (circle.y - circle.radius < 0.0);
    }

    private Island getIsland() {
        if (island == null) {
            island = new Builder().populationSize(100).genomeSize(63).build();
        }
        return island;
    }
}
