package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.CreatureBuilder;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BitFlipMutatorTest {

    @Test
    public void testBelowMutationRate() throws Exception {
        Creature creature = new CreatureBuilder().withGenomeSize(9).build();
        assertThat(creature.describe(), is("000000000"));

        new BitFlipMutator().accept(creature, () -> 0.2, 0.5);
        assertThat(creature.describe(), is("111111111"));
    }

    @Test
    public void testAboveMutationRate() throws Exception {
        Creature creature = new CreatureBuilder().withGenomeSize(9).build();
        assertThat(creature.describe(), is("000000000"));

        new BitFlipMutator().accept(creature, () -> 0.7, 0.5);
        assertThat(creature.describe(), is("000000000"));
    }
}
