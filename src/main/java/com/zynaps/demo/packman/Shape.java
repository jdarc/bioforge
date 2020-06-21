package com.zynaps.demo.packman;

class Shape {

    public final Edge[] edges;

    public Shape(Vertex... vertices) {
        edges = new Edge[vertices.length];
        for (var i = 0; i < vertices.length; ) {
            edges[i] = new Edge(vertices[i], vertices[++i % vertices.length]);
        }
    }

    public boolean contains(Circle circle) {
        for (var edge : edges) {
            var dpx = (circle.x - edge.normal.x * circle.radius - edge.center.x) * edge.normal.x;
            var dpy = (circle.y - edge.normal.y * circle.radius - edge.center.y) * edge.normal.y;
            if (dpx + dpy < 0.0) {
                return false;
            }
        }
        return true;
    }
}
