package com.zynaps.demo.circles;

import com.zynaps.bioforge.Creature;

class Circle {

    public final double x;
    public final double y;
    public final double radius;

    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Circle(Creature creature, double multiplier) {
        long dna = creature.extract(0, 63);
        x = multiplier * (0x1FFFFF & dna);
        y = multiplier * (0x1FFFFF & dna >> 21);
        radius = multiplier * (0x1FFFFF & dna >> 42);
    }

    private static double sqr(double x) {
        return x * x;
    }

    public boolean overlaps(Circle other) {
        return sqr(x - other.x) + sqr(y - other.y) < sqr(radius + other.radius);
    }
}
