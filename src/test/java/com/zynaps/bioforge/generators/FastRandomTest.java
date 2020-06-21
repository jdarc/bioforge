package com.zynaps.bioforge.generators;

import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

public class FastRandomTest {

    @Test
    public void testNextIntWithPositiveNonZeroBoundary() {
        RandomNumberGenerator random = new FastRandomNumbers();
        for (int t = 0; t < 1000; t++) {
            int bound = ThreadLocalRandom.current().nextInt(100) + 1;
            int actual = random.nextInt(bound);
            assertThat(actual, lessThan(bound));
            assertThat(actual, greaterThanOrEqualTo(0));
        }
    }

    @Test
    public void testNextIntWithZeroAndNegativeBoundary() {
        RandomNumberGenerator random = new FastRandomNumbers();
        assertThat(random.nextInt(0), equalTo(0));
        for (int t = 0; t < 1000; t++) {
            assertThat(random.nextInt(0 - ThreadLocalRandom.current().nextInt(100)), equalTo(0));
        }
    }

    @Test
    public void testNextDouble() {
        RandomNumberGenerator random = new FastRandomNumbers();
        for (int t = 0; t < 1000; t++) {
            assertThat(random.nextDouble(), lessThan(1.0));
            assertThat(random.nextDouble(), greaterThanOrEqualTo(0.0));
        }
    }
}
