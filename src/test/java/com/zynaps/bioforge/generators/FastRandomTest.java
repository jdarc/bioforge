package com.zynaps.bioforge.generators;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class FastRandomTest {

    @Test
    public void testNextIntWithPositiveNonZeroBoundary() throws Exception {
        RandomGenerator random = new FastRandom();
        for (int t = 0; t < 1000; t++) {
            int bound = ThreadLocalRandom.current().nextInt(100) + 1;
            int actual = random.nextInt(bound);
            assertThat(actual, lessThan(bound));
            assertThat(actual, greaterThanOrEqualTo(0));
        }
    }

    @Test
    public void testNextIntWithZeroAndNegativeBoundary() throws Exception {
        RandomGenerator random = new FastRandom();
        assertThat(random.nextInt(0), equalTo(0));
        for (int t = 0; t < 1000; t++) {
            assertThat(random.nextInt(0 - ThreadLocalRandom.current().nextInt(100)), equalTo(0));
        }
    }

    @Test
    public void testNextDouble() throws Exception {
        RandomGenerator random = new FastRandom();
        for (int t = 0; t < 1000; t++) {
            assertThat(random.nextDouble(), lessThan(1.0));
            assertThat(random.nextDouble(), greaterThanOrEqualTo(0.0));
        }
    }
}
