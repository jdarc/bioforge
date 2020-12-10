package com.zynaps.bioforge.schemes;

import com.zynaps.bioforge.CannedRandomNumber;
import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.CreatureBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TournamentSelectionTest {

    @Test
    public void testArityOne() {
        testArity(1);
    }

    @Test
    public void testArityTwo() {
        testArity(2);
    }

    @Test
    public void testArityThree() {
        testArity(3);
    }

    @Test
    public void testArityFour() {
        testArity(4);
    }

    @Test
    public void testArityFive() {
        testArity(5);
    }

    private void testArity(int arity) {
        List<Creature> creatures = new ArrayList<>();
        for (int t = 0; t < ThreadLocalRandom.current().nextInt(1, 9); t++) {
            creatures.add(new CreatureBuilder().withFitness(2000.0 * ThreadLocalRandom.current().nextDouble() - 1000.0).build());
        }

        double[] randoms =
            IntStream.range(0, 2 * creatures.size() * arity).mapToDouble(i -> ThreadLocalRandom.current().nextDouble()).toArray();
        double[] largest = IntStream.range(0, randoms.length / arity)
                                    .mapToObj(i -> Arrays.stream(randoms, i * arity, i * arity + arity))
                                    .mapToDouble(
                                        a -> a.map(x -> creatures.get((int) (x * creatures.size())).getFitness()).max().getAsDouble())
                                    .toArray();

        List<Creature> result = new TournamentSelection(arity).apply(creatures, new CannedRandomNumber(randoms));

        assertThat(result.size(), is(2 * creatures.size()));
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i).getFitness(), is(largest[i]));
        }
    }
}
