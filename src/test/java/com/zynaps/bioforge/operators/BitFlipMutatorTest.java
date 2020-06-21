package com.zynaps.bioforge.operators;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.CreatureBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BitFlipMutatorTest {

    @Test
    public void testBelowMutationRate() {
        Creature creature = new CreatureBuilder().withGenomeSize(9).build();
        assertThat(creature.describe(), is("000000000"));

        new BitFlipMutator().accept(creature, () -> 0.2, 0.5);
        assertThat(creature.describe(), is("111111111"));
    }

    @Test
    public void testAboveMutationRate() {
        Creature creature = new CreatureBuilder().withGenomeSize(9).build();
        assertThat(creature.describe(), is("000000000"));

        new BitFlipMutator().accept(creature, () -> 0.7, 0.5);
        assertThat(creature.describe(), is("000000000"));
    }
}
