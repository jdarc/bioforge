package com.zynaps.bioforge;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class TribeTest {

    @Test
    public void testCompete() throws Exception {
        double[] samples = new double[]{1, 2, 3, 6, 0, -1, 8, 0, 0, 1, 23, 10, 11, 33, 1, 2, 2, 3, -4, 1};

        Tribe tribe = new Tribe(20, 8);
        assertThat(tribe.champion.fitness, is(0.0));

        tribe.compete(new ToDoubleFunction<Creature>() {
            private int i = 0;

            @Override
            public double applyAsDouble(Creature value) {
                return samples[i++];
            }
        });

        for (int i = 0; i < samples.length; i++) {
            assertThat(tribe.parents.get(i).fitness, is(samples[i]));
        }
        assertThat(tribe.champion.fitness, is(33.0));
    }

    @Test
    public void testBreed() throws Exception {
        Tribe tribe = new Tribe(4, 8);
        tribe.parents.get(0).configure("11000011");
        tribe.parents.get(1).configure("11110000");
        tribe.parents.get(2).configure("00001111");
        tribe.parents.get(3).configure("00111100");

        tribe.breed((src, rnd) -> Arrays.asList(src.get(0), src.get(1), src.get(2), src.get(3), src.get(1), src.get(3), src.get(0), src.get(2)),
                    (mum, dad, kid, random, rate) -> {
                        kid.inherit(mum, 0, 0, 4);
                        kid.inherit(dad, 4, 4, 4);
                    }, new CannedRandom(0.0, 0.13, 0.25, 0.38, 0.5, 0.63, 0.75, 0.88, 0.99), 0.5);

        assertThat(tribe.children.get(0).describe(), is("11000000"));
        assertThat(tribe.children.get(1).describe(), is("00001100"));
        assertThat(tribe.children.get(2).describe(), is("11111100"));
        assertThat(tribe.children.get(3).describe(), is("11001111"));
    }

    @Test
    public void testMutate() throws Exception {
        Tribe tribe = new Tribe(4, 8);
        for (Creature child : tribe.children) {
            assertThat(child.describe(), is("00000000"));
        }

        tribe.mutate((creature, random, rate) -> {
            for (int i = 0; i < creature.getGenomeSize(); i++) {
                if (random.nextDouble() < rate) {
                    creature.flip(i);
                }
            }
        }, new CannedRandom(0.25, 0.75), 0.5);

        for (Creature child : tribe.children) {
            assertThat(child.describe(), is("10101010"));
        }
    }

    @Test
    public void testSwap() throws Exception {
        Tribe tribe = new Tribe(4, 8);
        List<Creature> source = tribe.parents;
        List<Creature> destination = tribe.children;

        assertThat(tribe.parents, is(not(destination)));
        assertThat(tribe.children, is(not(source)));

        tribe.swap();

        assertThat(tribe.parents, is(destination));
        assertThat(tribe.children, is(source));
    }
}
