package com.zynaps.demo.worms;

import com.zynaps.bioforge.Builder;
import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.Island;

class Controller {

    private static final int GRID_CHANGE_MILLIS = 1000 * 60 * 5;

    private final int[] assembly;
    private final Grid grid;
    private final Processor processor;
    private final Island population;
    private final GridView view;
    private final Worm worm;
    public boolean replayChampion;
    private boolean active;
    private boolean interactive;

    public Controller(GridView view) {
        this.view = view;
        grid = new Grid();
        worm = new Worm();
        replayChampion = true;
        processor = new Processor();
        assembly = new int[Processor.PROGRAM_SIZE];
        population = new Builder()
            .tribes(1)
            .populationSize(300)
            .genomeSize(Processor.PROGRAM_SIZE * Processor.INSTRUCTION_SIZE)
            .crossoverRate(0.5)
            .mutationRate(0.003)
            .nuke(true)
            .build();
    }

    public void stop() {
        active = false;
    }

    public void start() {
        active = true;
        new Thread(() -> {
            var t = System.currentTimeMillis();
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
        for (var i = 0; i < assembly.length; ++i) {
            assembly[i] = (int) creature.extract(i * Processor.INSTRUCTION_SIZE, Processor.INSTRUCTION_SIZE);
        }
        processor.load(assembly);

        grid.reset();
        worm.reset();
        worm.moveTo(128, 128);

        var step = 0;
        var fitness = 0.0;
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

            processor.run(262144);
            var turnPeek = processor.peek(0);

            if (turnPeek < -512.0) {
                worm.turnLeft();
                worm.useEnergy(1);
            } else if (turnPeek > 512.0) {
                worm.turnRight();
                worm.useEnergy(1);
            }

            worm.moveForward();
            worm.useEnergy(1);
            fitness += 1.0;

            if (worm.getEnergy() <= 0 || grid.isObstacle(worm.getX(), worm.getY())) {
                worm.kill();
            } else if (grid.isFood(worm.getX(), worm.getY())) {
                worm.addEnergy(5);
            }
        }
        return fitness;
    }
}
