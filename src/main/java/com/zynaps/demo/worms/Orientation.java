package com.zynaps.demo.worms;

enum Orientation {
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),
    NORTH(0, -1);

    public final int dx;
    public final int dy;

    Orientation(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Orientation left() {
        return values()[0x03 & ordinal() - 1];
    }

    public Orientation right() {
        return values()[0x03 & ordinal() + 1];
    }
}
