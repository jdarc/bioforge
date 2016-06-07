package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.CannedRandom;
import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.CreatureBuilder;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SpliceCrossoverTest {

    @Test
    public void testAboveCrossoverRate() throws Exception {
        CreatureBuilder builder = new CreatureBuilder().withGenomeSize(9);
        Creature mum = builder.build();
        Creature kid = builder.build();

        new SpliceCrossover().accept(mum, null, kid, () -> 0.75, 0.5);

        assertThat(kid.describe(), is(mum.describe()));
    }

    @Test
    public void testBelowCrossoverRate() throws Exception {
        CreatureBuilder builder = new CreatureBuilder().withGenomeSize(9);
        Creature kid = builder.build();
        Creature dad = builder.build();
        Creature mum = builder.withFlip(true).build();

        new SpliceCrossover().accept(mum, dad, kid, new CannedRandom(0.1, 0.0, 0.3, 0.8), 0.5);

        assertThat(kid.describe(), is("111110001"));
    }
}
