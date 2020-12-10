package com.zynaps.demo.worms;

class Antennae {

    private final Sensor op;
    private int sw;
    private int w;
    private int nw;
    private int n;
    private int ne;
    private int e;
    private int se;

    public Antennae(Sensor op) {
        this.op = op;
    }

    public int getSw() {
        return sw;
    }

    public int getW() {
        return w;
    }

    public int getNw() {
        return nw;
    }

    public int getN() {
        return n;
    }

    public int getNe() {
        return ne;
    }

    public int getE() {
        return e;
    }

    public int getSe() {
        return se;
    }

    public void sense(Grid grid, int x, int y, Orientation orientation) {
        int tx, ty;
        var sdist = 0;
        var swdist = 0;
        var wdist = 0;
        var nwdist = 0;
        var ndist = 0;
        var nedist = 0;
        var edist = 0;
        var sedist = 0;

        ty = y;
        while (op.accept(grid, x, --ty)) {
            ++ndist;
        }

        ty = y;
        while (op.accept(grid, x, ++ty)) {
            ++sdist;
        }

        tx = x;
        while (op.accept(grid, ++tx, y)) {
            ++edist;
        }

        tx = x;
        while (op.accept(grid, --tx, y)) {
            ++wdist;
        }

        tx = x;
        ty = y;
        while (op.accept(grid, --tx, --ty)) {
            ++nwdist;
        }

        tx = x;
        ty = y;
        while (op.accept(grid, ++tx, --ty)) {
            ++nedist;
        }

        tx = x;
        ty = y;
        while (op.accept(grid, --tx, ++ty)) {
            ++swdist;
        }

        tx = x;
        ty = y;
        while (op.accept(grid, ++tx, ++ty)) {
            ++sedist;
        }

        switch (orientation) {
            case EAST: {
                sw = nwdist;
                w = ndist;
                nw = nedist;
                n = edist;
                ne = sedist;
                e = sdist;
                se = swdist;
            }
            case SOUTH: {
                sw = nedist;
                w = edist;
                nw = sedist;
                n = sdist;
                ne = swdist;
                e = wdist;
                se = swdist;
            }
            case WEST: {
                sw = sedist;
                w = sdist;
                nw = swdist;
                n = wdist;
                ne = nwdist;
                e = ndist;
                se = nedist;
            }
            case NORTH: {
                sw = swdist;
                w = wdist;
                nw = nwdist;
                n = ndist;
                ne = nedist;
                e = edist;
                se = sedist;
            }
        }
    }
}
