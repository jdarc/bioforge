package com.zynaps.demo.packman;

class Circle {

    public final double x;
    public final double y;
    public final double radius;

    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double overlap(Circle other) {
        var d = (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y);
        if (d > (radius + other.radius) * (radius + other.radius)) {
            return 0.0;
        }

        d = Math.sqrt(d);
        var overlaps = d <= Math.abs(other.radius - radius);
        if (overlaps && other.radius >= radius) {
            return Math.PI * radius * radius;
        } else if (overlaps && other.radius < radius) {
            return Math.PI * other.radius * other.radius;
        } else {
            var rr0 = other.radius * other.radius;
            var rr1 = radius * radius;
            var phi = Math.acos((rr0 + d * d - rr1) / (2.0 * other.radius * d)) * 2.0;
            var theta = Math.acos((rr1 + d * d - rr0) / (2.0 * radius * d)) * 2.0;
            return 0.5 * theta * rr1 - 0.5 * rr1 * Math.sin(theta) + 0.5 * phi * rr0 - 0.5 * rr0 * Math.sin(phi);
        }
    }
}
