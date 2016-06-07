package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.RandomGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class IslandTest {

    @Test
    public void testGetChampion() throws Exception {
        Island island = new Island(8, 4, 8);
        Creature champion = island.firstTribe.champion;
        double best = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < island.tribes.length; i++) {
            List<Creature> creatures = island.tribes[i].parents;
            for (Creature creature : creatures) {
                creature.fitness = 2000.0 * ThreadLocalRandom.current().nextDouble() - 1000.0;
                if (creature.fitness > best) {
                    best = creature.fitness;
                    champion = creature;
                }
            }
        }
        island.evolve(creature -> creature.fitness);
        assertThat(island.getChampion().getFitness(), is(champion.getFitness()));
    }

    @Test
    public void testGetGeneration() throws Exception {
        Island island = new Island(1, 1, 1);
        assertThat(island.getGeneration(), is(0));
        for (int t = 0; t < ThreadLocalRandom.current().nextInt(1, 9); ) {
            island.evolve(creature -> 0.0);
            assertThat(island.getGeneration(), is(++t));
        }
    }

    @Test
    public void testGetMutationRate() throws Exception {
        double expected = ThreadLocalRandom.current().nextDouble();
        Island island = new Island(1, 1, 1);
        assertThat(island.getMutationRate(), is(0.0));
        island.mutationRate = expected;
        assertThat(island.getMutationRate(), is(expected));
    }

    @Test
    public void testSetMutationRate() throws Exception {
        Island island = new Island(1, 1, 1);
        assertThat(island.mutationRate, is(0.0));

        island.setMutationRate(-0.1);
        assertThat(island.mutationRate, is(0.0));

        island.setMutationRate(1.1);
        assertThat(island.mutationRate, is(1.0));

        double expected = ThreadLocalRandom.current().nextDouble();
        island.setMutationRate(expected);
        assertThat(island.mutationRate, is(expected));
    }

    @Test
    public void testGetCrossoverRate() throws Exception {
        double expected = ThreadLocalRandom.current().nextDouble();
        Island island = new Island(1, 1, 1);
        assertThat(island.getCrossoverRate(), is(0.0));
        island.crossoverRate = expected;
        assertThat(island.getCrossoverRate(), is(expected));
    }

    @Test
    public void testSetCrossoverRate() throws Exception {
        Island island = new Island(1, 1, 1);
        assertThat(island.crossoverRate, is(0.0));

        island.setCrossoverRate(-0.1);
        assertThat(island.crossoverRate, is(0.0));

        island.setCrossoverRate(1.1);
        assertThat(island.crossoverRate, is(1.0));

        double expected = ThreadLocalRandom.current().nextDouble();
        island.setCrossoverRate(expected);
        assertThat(island.crossoverRate, is(expected));
    }

    @Test
    public void testUseCrossoverOperator() throws Exception {
        Island island = new Island(1, 1, 1);
        Creature[] test = {null};
        island.evolve(creature -> 0.0);
        assertThat(test[0], is(nullValue()));

        island.useCrossoverOperator((mum, dad, kid, random, rate) -> test[0] = kid);

        island.evolve(creature -> 0.0);
        assertThat(test[0], is(notNullValue()));
    }

    @Test
    public void testUseMutationOperator() throws Exception {
        Island island = new Island(1, 1, 1);
        Creature[] test = {null};
        island.evolve(creature -> 0.0);
        assertThat(test[0], is(nullValue()));

        island.useMutationOperator((creature, random, rate) -> test[0] = creature);

        island.evolve(creature -> 0.0);
        assertThat(test[0], is(notNullValue()));
    }

    @Test
    public void testUseSelectionScheme() throws Exception {
        Island island = new Island(1, 1, 1);
        Creature[] test = {null};
        island.evolve(creature -> 0.0);
        assertThat(test[0], is(nullValue()));

        island.useSelectionScheme((creatures, random) -> Collections.singletonList(test[0] = creatures.get(0)));

        island.evolve(creature -> 0.0);
        assertThat(test[0], is(notNullValue()));
    }

    @Test
    public void testUseRandomGenerator() throws Exception {
        Island island = new Island(1, 1, 1);
        double[] test = {0.0};
        island.evolve(creature -> 0.0);
        assertThat(test[0], is(0.0));

        island.useRandomGenerator(() -> (test[0] = 0.8));

        island.evolve(creature -> 0.0);
        assertThat(test[0], is(0.8));
    }

    @Test
    public void testZero() throws Exception {
        Island island = new Island(1, 1, 8);
        island.useRandomGenerator(() -> 1.0);
        island.nuke();
        assertThat(island.firstTribe.parents.get(0).describe(), is(not("00000000")));
        island.zero();
        assertThat(island.firstTribe.parents.get(0).describe(), is(not("11111111")));
    }

    @Test
    public void testNuke() throws Exception {
        boolean[] truthy = new boolean[100];
        Arrays.fill(truthy, true);

        Island island = new Island(1, 1, 100);
        island.random = new RandomGenerator() {
            @Override
            public int nextInt(int bound) {
                return bound;
            }

            @Override
            public double nextDouble() {
                return 1.0;
            }
        };
        Creature creature = island.firstTribe.parents.get(0);

        assertThat(Arrays.equals(creature.dna, truthy), is(false));
        island.nuke();
        assertThat(Arrays.equals(creature.dna, truthy), is(true));
    }

    @Test
    public void testSingleTribeEvolve() throws Exception {
        Island island = new Island(1, 4, 8);
        assertThat(island.firstTribe.champion.getFitness(), is(0.0));
        island.evolve(value -> 10.0);
        assertThat(island.firstTribe.champion.getFitness(), is(10.0));
    }

    @Test
    public void testManyTribeEvolve() throws Exception {
        Island island = new Island(3, 4, 8);
        for (int i = 0; i < island.tribes.length; i++) {
            assertThat(island.tribes[i].champion.getFitness(), is(0.0));
        }

        island.evolve(value -> Thread.currentThread().getId());

        for (int i = 0; i < island.tribes.length; i++) {
            assertThat(island.tribes[i].champion.getFitness(), greaterThan(0.0));
        }
    }
}
