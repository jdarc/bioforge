package com.zynaps.demo.worms;

import com.zynaps.bioforge.Builder;
import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.Island;

class Controller {

    private static final int GRID_CHANGE_MILLIS = 1000 * 60 * 5; // change every 5 minutes

    private final int[] assembly;
    private final Grid grid;
    private final Processor processor;
    private final Island population;
    private final GridView view;
    private final Worm worm;
    private boolean active;
    private boolean interactive;
    public boolean replayChampion;

    public Controller(GridView view) {
        this.view = view;
        this.grid = new Grid();
        this.worm = new Worm();
        this.replayChampion = true;
        this.processor = new Processor();
        this.assembly = new int[Processor.PROGRAM_SIZE];
        this.population = new Builder().tribes(1)
                                       .populationSize(100)
                                       .genomeSize(Processor.PROGRAM_SIZE * Processor.INSTRUCTION_SIZE)
                                       .crossoverRate(0.5)
                                       .mutationRate(0.0025)
                                       .nuke(true)
                                       .build();
    }

    public void stop() {
        active = false;
    }

    public void start() {
        active = true;
        new Thread(() -> {
            long t = System.currentTimeMillis();
            while (active) {
                population.evolve(this::evaluate);
                replayChampion();
                t = gridGenerate(t);
            }
        }).start();
    }

    private void replayChampion() {
        if (replayChampion) {
            interactive = true;
            evaluate(population.getChampion());
            interactive = false;
        }
    }

    private long gridGenerate(long t) {
        if (System.currentTimeMillis() - t > GRID_CHANGE_MILLIS) {
            t = System.currentTimeMillis();
            grid.generate();
        }
        return t;
    }

    private double evaluate(Creature creature) {
        for (int i = 0; i < assembly.length; ++i) {
            assembly[i] = (int)creature.extract(i * Processor.INSTRUCTION_SIZE, Processor.INSTRUCTION_SIZE);
        }
        processor.load(assembly);

        grid.reset();
        worm.reset();
        worm.moveTo(128, 128);

        int step = 0;
        double fitness = 0.0;
        while (active && worm.isAlive()) {
            grid.set(worm.getX(), worm.getY());

            if (interactive) {
                view.update(population.getGeneration(), population.getChampion().getFitness(), grid::drawToBitmap);
            }

            worm.sense(grid);

            processor.clear();
            processor.poke(0, ++step);
            processor.poke(1, worm.getX());
            processor.poke(2, worm.getY());
            processor.poke(3, worm.getEnergy());
            processor.poke(4, worm.getOrientation().ordinal());
            processor.poke(5, worm.getWallAntennae().getSw());
            processor.poke(6, worm.getWallAntennae().getW());
            processor.poke(7, worm.getWallAntennae().getNw());
            processor.poke(8, worm.getWallAntennae().getN());
            processor.poke(9, worm.getWallAntennae().getNe());
            processor.poke(10, worm.getWallAntennae().getE());
            processor.poke(11, worm.getWallAntennae().getSe());
            processor.poke(12, worm.getFoodAntennae().getSw());
            processor.poke(13, worm.getFoodAntennae().getW());
            processor.poke(14, worm.getFoodAntennae().getNw());
            processor.poke(15, worm.getFoodAntennae().getN());
            processor.poke(16, worm.getFoodAntennae().getNe());
            processor.poke(17, worm.getFoodAntennae().getE());
            processor.poke(18, worm.getFoodAntennae().getSe());

            int cycles = processor.run(262144);
            double turnPeek = processor.peek(30);
            double movePeek = processor.peek(31);

            if (turnPeek < -1.0) {
                worm.turnRight();
            } else if (turnPeek > 1.0) {
                worm.turnLeft();
            }

            if (movePeek > 0.0) {
                worm.moveForward();
            } else {
                fitness -= 50.0;
            }

            if (grid.isObstacle(worm.getX(), worm.getY())) {
                worm.kill();
            } else if (grid.isFood(worm.getX(), worm.getY())) {
                worm.addEnergy(3);
                fitness += 10.0;
            } else if (grid.isFree(worm.getX(), worm.getY())) {
                worm.useEnergy(1);
                fitness += 2.0;
            }
        }
        return fitness;
    }
}
