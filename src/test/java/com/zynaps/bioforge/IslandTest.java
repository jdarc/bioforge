package com.zynaps.bioforge;

import com.zynaps.bioforge.generators.RandomNumberGenerator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class IslandTest {

    @Test
    public void testGetChampion() {
        Island island = new Island(8, 4, 8);
        Creature champion = island.tribes[0].champion;
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
    public void testGetGeneration() {
        Island island = new Island(1, 1, 1);
        assertThat(island.getGeneration(), is(0));
        for (int t = 0; t < ThreadLocalRandom.current().nextInt(1, 9); ) {
            island.evolve(creature -> 0.0);
            assertThat(island.getGeneration(), is(++t));
        }
    }

    @Test
    public void testGetMutationRate() {
        double expected = ThreadLocalRandom.current().nextDouble();
        Island island = new Island(1, 1, 1);
        assertThat(island.getMutationRate(), is(0.0));
        island.mutationRate = expected;
        assertThat(island.getMutationRate(), is(expected));
    }

    @Test
    public void testSetMutationRate() {
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
    public void testGetCrossoverRate() {
        double expected = ThreadLocalRandom.current().nextDouble();
        Island island = new Island(1, 1, 1);
        assertThat(island.getCrossoverRate(), is(0.0));
        island.crossoverRate = expected;
        assertThat(island.getCrossoverRate(), is(expected));
    }

    @Test
    public void testSetCrossoverRate() {
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
    public void testUseCrossoverOperator() {
        Island island = new Island(1, 1, 1);
        Creature[] test = {null};
        island.evolve(creature -> 0.0);
        assertThat(test[0], is(nullValue()));

        island.useCrossoverOperator((mum, dad, kid, random, rate) -> test[0] = kid);

        island.evolve(creature -> 0.0);
        assertThat(test[0], is(notNullValue()));
    }

    @Test
    public void testUseMutationOperator() {
        Island island = new Island(1, 1, 1);
        Creature[] test = {null};
        island.evolve(creature -> 0.0);
        assertThat(test[0], is(nullValue()));

        island.useMutationOperator((creature, random, rate) -> test[0] = creature);

        island.evolve(creature -> 0.0);
        assertThat(test[0], is(notNullValue()));
    }

    @Test
    public void testUseSelectionScheme() {
        Island island = new Island(1, 1, 1);
        Creature[] test = {null};
        island.evolve(creature -> 0.0);
        assertThat(test[0], is(nullValue()));

        island.useSelectionScheme((creatures, random) -> Collections.singletonList(test[0] = creatures.get(0)));

        island.evolve(creature -> 0.0);
        assertThat(test[0], is(notNullValue()));
    }

    @Test
    public void testUseRandomGenerator() {
        Island island = new Island(1, 1, 1);
        double[] test = {0.0};
        island.evolve(creature -> 0.0);
        assertThat(test[0], is(0.0));

        island.useRandomGenerator(() -> (test[0] = 0.8));

        island.evolve(creature -> 0.0);
        assertThat(test[0], is(0.8));
    }

    @Test
    public void testZero() {
        Island island = new Island(1, 1, 8);
        island.useRandomGenerator(() -> 1.0);
        island.nuke();
        assertThat(island.tribes[0].parents.get(0).describe(), is(not("00000000")));
        island.zero();
        assertThat(island.tribes[0].parents.get(0).describe(), is(not("11111111")));
    }

    @Test
    public void testNuke() {
        boolean[] truthy = new boolean[100];
        Arrays.fill(truthy, true);

        Island island = new Island(1, 1, 100);
        island.random = new RandomNumberGenerator() {
            @Override
            public int nextInt(int bound) {
                return bound;
            }

            @Override
            public double nextDouble() {
                return 1.0;
            }
        };
        Creature creature = island.tribes[0].parents.get(0);

        assertThat(Arrays.equals(creature.dna, truthy), is(false));
        island.nuke();
        assertThat(Arrays.equals(creature.dna, truthy), is(true));
    }

    @Test
    public void testSingleTribeEvolve() {
        Island island = new Island(1, 4, 8);
        assertThat(island.tribes[0].champion.getFitness(), is(0.0));
        island.evolve(value -> 10.0);
        assertThat(island.tribes[0].champion.getFitness(), is(10.0));
    }

    @Test
    public void testManyTribeEvolve() {
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
