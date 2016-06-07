package com.zynaps.bioforge.schemes;

import com.zynaps.bioforge.CannedRandom;
import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.CreatureBuilder;
import com.zynaps.bioforge.generators.RandomGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RouletteSelectionTest {

    @Test
    public void testFromZeroFitness() throws Exception {
        CreatureBuilder builder = new CreatureBuilder();
        List<Creature> creatures = new ArrayList<>();
        creatures.add(builder.withFitness(20.0).build());
        creatures.add(builder.withFitness(0.0).build());
        creatures.add(builder.withFitness(30.0).build());
        creatures.add(builder.withFitness(10.0).build());
        RandomGenerator random = new CannedRandom(0.0, 0.0, 0.49, 0.49, 0.83, 0.83, 0.99, 0.99);
        check(new RouletteSelection(0.0).apply(creatures, random), creatures);
    }

    @Test
    public void testAboveZeroFitness() throws Exception {
        CreatureBuilder builder = new CreatureBuilder();
        List<Creature> creatures = new ArrayList<>();
        creatures.add(builder.withFitness(15.0).build());
        creatures.add(builder.withFitness(5.0).build());
        creatures.add(builder.withFitness(20.0).build());
        creatures.add(builder.withFitness(10.0).build());
        RandomGenerator random = new CannedRandom(0.0, 0.0, 0.48, 0.48, 0.80, 0.80, 0.99, 0.99);
        check(new RouletteSelection(0.0).apply(creatures, random), creatures);
    }

    @Test
    public void testFullRangeFitness() throws Exception {
        CreatureBuilder builder = new CreatureBuilder();
        List<Creature> creatures = new ArrayList<>();
        creatures.add(builder.withFitness(50.0).build());
        creatures.add(builder.withFitness(-10.0).build());
        creatures.add(builder.withFitness(100.0).build());
        creatures.add(builder.withFitness(0.0).build());
        RandomGenerator random = new CannedRandom(0.0, 0.0, 0.61, 0.61, 0.95, 0.95, 0.995, 0.995);

        List<Creature> result = new RouletteSelection(0.0).apply(creatures, random);

        check(result, creatures);
    }

    @Test
    public void testUpToZeroFitness() throws Exception {
        CreatureBuilder builder = new CreatureBuilder();
        List<Creature> creatures = new ArrayList<>();
        creatures.add(builder.withFitness(-50.0).build());
        creatures.add(builder.withFitness(-10.0).build());
        creatures.add(builder.withFitness(-30.0).build());
        creatures.add(builder.withFitness(0.0).build());
        RandomGenerator random = new CannedRandom(0.0, 0.0, 0.48, 0.48, 0.81, 0.81, 0.995, 0.995);

        List<Creature> result = new RouletteSelection(0.0).apply(creatures, random);

        check(result, creatures);
    }

    @Test
    public void testBelowZeroFitness() throws Exception {
        CreatureBuilder builder = new CreatureBuilder();
        List<Creature> creatures = new ArrayList<>();
        creatures.add(builder.withFitness(-50.0).build());
        creatures.add(builder.withFitness(-10.0).build());
        creatures.add(builder.withFitness(-30.0).build());
        creatures.add(builder.withFitness(-5.0).build());
        RandomGenerator random = new CannedRandom(0.0, 0.0, 0.48, 0.48, 0.81, 0.81, 0.995, 0.995);

        List<Creature> result = new RouletteSelection(0.0).apply(creatures, random);

        check(result, creatures);
    }

    @Test
    public void testElitism() throws Exception {
        CreatureBuilder builder = new CreatureBuilder();
        List<Creature> creatures = new ArrayList<>();
        creatures.add(builder.withFitness(5.0).build());
        creatures.add(builder.withFitness(5.1).build());
        creatures.add(builder.withFitness(5.2).build());
        creatures.add(builder.withFitness(5.3).build());
        RandomGenerator random = new CannedRandom(0.0, 0.0, 0.25, 0.25, 0.5, 0.5, 0.75, 0.75);

        List<Creature> result = new RouletteSelection(0.25).apply(creatures, random);

        assertThat(result.get(0).getFitness(), is(creatures.get(0).getFitness()));
        assertThat(result.get(1).getFitness(), is(creatures.get(0).getFitness()));
        assertThat(result.get(2).getFitness(), is(creatures.get(0).getFitness()));
        assertThat(result.get(3).getFitness(), is(creatures.get(0).getFitness()));
        assertThat(result.get(4).getFitness(), is(creatures.get(1).getFitness()));
        assertThat(result.get(5).getFitness(), is(creatures.get(1).getFitness()));
        assertThat(result.get(6).getFitness(), is(creatures.get(2).getFitness()));
        assertThat(result.get(7).getFitness(), is(creatures.get(2).getFitness()));
    }

    private void check(List<Creature> result, List<Creature> creatures) {
        Collections.sort(creatures);
        for (int i = 0, j = 0; i < creatures.size(); i++) {
            assertThat(result.get(j++).getFitness(), is(creatures.get(i).getFitness()));
            assertThat(result.get(j++).getFitness(), is(creatures.get(i).getFitness()));
        }
    }
}
