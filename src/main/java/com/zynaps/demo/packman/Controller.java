package com.zynaps.demo.packman;

import com.zynaps.bioforge.Builder;
import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.Island;

import java.util.ArrayList;
import java.util.List;

class Controller {

    private static final int NUMBER_OF_CIRCLES = 10;

    private final Island population;
    private Shape shape;

    public Controller() {
        population = new Builder()
                .tribes(3)
                .populationSize(150)
                .genomeSize(NUMBER_OF_CIRCLES * 63)
                .crossoverRate(0.3)
                .mutationRate(0.002)
                .build();
        reset();
    }

    private static List<Circle> decode(Creature creature) {
        double multiplier = 12.0 / 2097152.0;
        List<Circle> circles = new ArrayList<>(NUMBER_OF_CIRCLES);
        for (int i = 0; i < NUMBER_OF_CIRCLES; ++i) {
            long dna = creature.extract(63 * i, 63);
            double x = multiplier * ((0x1FFFFF & dna) - 0x100000);
            double y = multiplier * ((0x1FFFFF & dna >> 21) - 0x100000);
            double r = multiplier * ((0x1FFFFF & dna >> 42));
            circles.add(new Circle(x, y, r));
        }
        return circles;
    }

    private static Shape generateShape(int count) {
        Vertex[] vertices = new Vertex[count];
        int rightMost = 0;
        double highestXCoord = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < count; ++i) {
            Vertex v = vertices[i] = new Vertex(12.0 * Math.random() - 6.0, 12.0 * Math.random() - 6.0);
            if (v.x > highestXCoord || v.x == highestXCoord && v.y < vertices[rightMost].y) {
                highestXCoord = v.x;
                rightMost = i;
            }
        }

        int[] hull = new int[count];
        int indexHull = rightMost;
        int outCount = 0;
        do {
            hull[outCount] = indexHull;

            int nextHullIndex = 0;
            Vertex poc = vertices[hull[outCount]];
            for (int i1 = 0; i1 < vertices.length; ++i1) {
                if (nextHullIndex != indexHull) {
                    double e1x = vertices[nextHullIndex].x - poc.x;
                    double e1y = vertices[nextHullIndex].y - poc.y;
                    double e2x = vertices[i1].x - poc.x;
                    double e2y = vertices[i1].y - poc.y;
                    double c = e1x * e2y - e1y * e2x;
                    if (c < 0.0 || c == 0.0 && e2x * e2x + e2y * e2y > e1x * e1x + e1y * e1y) {
                        nextHullIndex = i1;
                    }
                } else {
                    nextHullIndex = i1;
                }
            }

            ++outCount;
            indexHull = nextHullIndex;
        } while (indexHull != rightMost);

        Vertex[] result = new Vertex[outCount];
        for (int i = 0; i < result.length; ++i) {
            result[i] = vertices[hull[i]];
        }

        return new Shape(result);
    }

    public Shape getShape() {
        return shape;
    }

    public List<Circle> getChampion() {
        return decode(population.getChampion());
    }

    public int getGeneration() {
        return population.getGeneration();
    }

    public double getFitness() {
        return population.getChampion().getFitness();
    }

    public void reset() {
        shape = generateShape(16);
        population.zero();
    }

    public void evolve() {
        population.evolve(creature -> {
            List<Circle> circles = decode(creature);
            double fitness = 0.0;
            for (Circle circle : circles) {
                if (shape.contains(circle)) {
                    fitness += circle.radius;
                    for (Circle other : circles) {
                        if (other != circle) {
                            fitness -= other.overlap(circle);
                        }
                    }
                } else {
                    fitness -= circle.radius;
                }
            }
            return Math.max(0, fitness);
        });
    }
}
