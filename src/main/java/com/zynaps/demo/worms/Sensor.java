package com.zynaps.demo.worms;

@FunctionalInterface
interface Sensor {

    boolean accept(Grid grid, int x, int y);
}
