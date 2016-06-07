package com.zynaps.demo.worms;

class Worm {

    private int x;
    private int y;
    private int energy;
    private Orientation orientation;
    private Antennae wallAntennae;
    private Antennae foodAntennae;

    public Worm() {
        wallAntennae = new Antennae((grid, x, y) -> !grid.isObstacle(x, y));
        foodAntennae = new Antennae((grid, x, y) -> !grid.isObstacle(x, y) && !grid.isFood(x, y));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Antennae getWallAntennae() {
        return wallAntennae;
    }

    public Antennae getFoodAntennae() {
        return foodAntennae;
    }

    public void useEnergy(int amount) {
        energy = Math.max(0, energy - amount);
    }

    public void addEnergy(int amount) {
        energy = Math.min(100, energy + amount);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void turnLeft() {
        orientation = orientation.left();
    }

    public void turnRight() {
        orientation = orientation.right();
    }

    public void moveForward() {
        x += orientation.dx;
        y += orientation.dy;
    }

    public void reset() {
        energy = 100;
        orientation = Orientation.NORTH;
    }

    public void kill() {
        energy = 0;
    }

    public void sense(Grid grid) {
        wallAntennae.sense(grid, x, y, orientation);
        foodAntennae.sense(grid, x, y, orientation);
    }
}
