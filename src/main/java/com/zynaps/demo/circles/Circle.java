package com.zynaps.demo.circles;

class Circle {

    public final double x;
    public final double y;
    public final double radius;

    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean overlaps(Circle other) {
        return sqr(x - other.x) + sqr(y - other.y) < sqr(radius + other.radius);
    }

    private static double sqr(double x) {
        return x * x;
    }
}
