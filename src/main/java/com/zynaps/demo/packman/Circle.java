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
        double d = (this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y);
        if (d > (this.radius + other.radius) * (this.radius + other.radius)) {
            return 0.0;
        }

        d = Math.sqrt(d);
        if (d <= Math.abs(other.radius - this.radius) && other.radius >= this.radius) {
            return Math.PI * this.radius * this.radius;
        } else if (d <= Math.abs(other.radius - this.radius) && other.radius < this.radius) {
            return Math.PI * other.radius * other.radius;
        } else {
            double rr0 = other.radius * other.radius;
            double rr1 = this.radius * this.radius;
            double phi = Math.acos((rr0 + d * d - rr1) / (2.0 * other.radius * d)) * 2.0;
            double theta = Math.acos((rr1 + d * d - rr0) / (2.0 * this.radius * d)) * 2.0;
            return 0.5 * theta * rr1 - 0.5 * rr1 * Math.sin(theta) + 0.5 * phi * rr0 - 0.5 * rr0 * Math.sin(phi);
        }
    }
}
