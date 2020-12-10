package com.zynaps.demo.packman;

import com.zynaps.bioforge.Builder;
import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.Island;

import java.util.ArrayList;
import java.util.List;

class Controller {

    private static final int NUMBER_OF_CIRCLES = 25;

    private final Island population;
    private Shape shape;

    public Controller() {
        population = new Builder()
            .tribes(5)
            .populationSize(80)
            .genomeSize(NUMBER_OF_CIRCLES * 126)
            .crossoverRate(0.1)
            .mutationRate(0.002)
            .build();
        reset();
    }

    private static List<Circle> decode(Creature creature) {
        var multiplier = 10.0 / 4398046511103.0;
        List<Circle> circles = new ArrayList<>(NUMBER_OF_CIRCLES);
        for (var i = 0; i < NUMBER_OF_CIRCLES; ++i) {
            int offset = 126 * i;
            var x = multiplier * ((0x3ffffffffffL & creature.extract(offset + 42 * 0, 42)) - 0x20000000000L);
            var y = multiplier * ((0x3ffffffffffL & creature.extract(offset + 42 * 1, 42)) - 0x20000000000L);
            var r = multiplier * ((0x7ffffffffffL & creature.extract(offset + 42 * 2, 42)));
            circles.add(new Circle(x, y, r));
        }
        return circles;
    }

    private static Shape generateShape(int count) {
        var vertices = new Vertex[count];
        var rightMost = 0;
        var highestXCoord = Double.NEGATIVE_INFINITY;
        for (var i = 0; i < count; ++i) {
            var v = vertices[i] = new Vertex(12.0 * Math.random() - 6.0, 12.0 * Math.random() - 6.0);
            if (v.x > highestXCoord || v.x == highestXCoord && v.y < vertices[rightMost].y) {
                highestXCoord = v.x;
                rightMost = i;
            }
        }

        var hull = new int[count];
        var indexHull = rightMost;
        var outCount = 0;
        do {
            hull[outCount] = indexHull;

            var nextHullIndex = 0;
            var poc = vertices[hull[outCount]];
            for (var i1 = 0; i1 < vertices.length; ++i1) {
                if (nextHullIndex != indexHull) {
                    var e1x = vertices[nextHullIndex].x - poc.x;
                    var e1y = vertices[nextHullIndex].y - poc.y;
                    var e2x = vertices[i1].x - poc.x;
                    var e2y = vertices[i1].y - poc.y;
                    var c = e1x * e2y - e1y * e2x;
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

        var result = new Vertex[outCount];
        for (var i = 0; i < result.length; ++i) {
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
            var circles = decode(creature);
            var fitness = 0.0;
            for (var circle : circles) {
                if (shape.contains(circle)) {
                    fitness += circle.radius;
                    for (var other : circles) {
                        if (other != circle) {
                            fitness -= other.overlap(circle);
                        }
                    }
                } else {
                    fitness -= circle.radius;
                }
            }
            return fitness;
        });
    }
}
