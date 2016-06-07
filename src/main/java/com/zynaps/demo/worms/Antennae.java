package com.zynaps.demo.worms;

class Antennae {

    private int sw;
    private int w;
    private int nw;
    private int n;
    private int ne;
    private int e;
    private int se;

    private final Sensor op;

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
        int sdist = 0;
        int swdist = 0;
        int wdist = 0;
        int nwdist = 0;
        int ndist = 0;
        int nedist = 0;
        int edist = 0;
        int sedist = 0;

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
            case EAST:
                this.sw = nwdist;
                this.w = ndist;
                this.nw = nedist;
                this.n = edist;
                this.ne = sedist;
                this.e = sdist;
                this.se = swdist;
                break;
            case SOUTH:
                this.sw = nedist;
                this.w = edist;
                this.nw = sedist;
                this.n = sdist;
                this.ne = swdist;
                this.e = wdist;
                this.se = swdist;
                break;
            case WEST:
                this.sw = sedist;
                this.w = sdist;
                this.nw = swdist;
                this.n = wdist;
                this.ne = nwdist;
                this.e = ndist;
                this.se = nedist;
                break;
            case NORTH:
                this.sw = swdist;
                this.w = wdist;
                this.nw = nwdist;
                this.n = ndist;
                this.ne = nedist;
                this.e = edist;
                this.se = sedist;
                break;
        }
    }
}
