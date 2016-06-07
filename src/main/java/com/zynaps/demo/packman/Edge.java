package com.zynaps.demo.packman;

class Edge {

    public final Vertex p1;
    public final Vertex p2;
    public final Vertex center;
    public final Vertex normal;

    public Edge(Vertex p1, Vertex p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.center = new Vertex((p1.x + p2.x) * 0.5, (p1.y + p2.y) * 0.5);
        double x = p2.x - p1.x;
        double y = p2.y - p1.y;
        double inv = 1.0 / Math.sqrt(x * x + y * y);
        this.normal = new Vertex(-y * inv, x * inv);
    }
}
