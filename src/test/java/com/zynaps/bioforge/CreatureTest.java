package com.zynaps.bioforge;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class CreatureTest {

    @Test
    public void testGetGenomeSize() {
        int size = ThreadLocalRandom.current().nextInt(100);
        assertThat(new Creature(size).getGenomeSize(), is(size));
    }

    @Test
    public void testGetFitness() {
        double fitness = 2000.0 * ThreadLocalRandom.current().nextDouble() - 1000.0;
        Creature creature = new Creature(0);
        creature.fitness = fitness;
        assertThat(creature.getFitness(), is(fitness));
    }

    @Test
    public void testIsSet() {
        Creature creature = new Creature(100);
        int index = ThreadLocalRandom.current().nextInt(creature.getGenomeSize());
        assertThat(creature.isSet(index), is(false));

        creature.dna[index] = true;

        assertThat(creature.isSet(index), is(true));
    }

    @Test
    public void testFlip() {
        Creature creature = new Creature(100);
        int index = ThreadLocalRandom.current().nextInt(creature.getGenomeSize());
        assertThat(creature.dna[index], is(false));

        creature.flip(index);

        assertThat(creature.dna[index], is(true));
    }

    @Test
    public void testZero() {
        int size = ThreadLocalRandom.current().nextInt(100);
        Creature creature = new Creature(size);
        Arrays.fill(creature.dna, true);
        IntStream.range(0, size).forEach(i -> assertThat(creature.dna[i], is(true)));

        creature.zero();

        IntStream.range(0, size).forEach(i -> assertThat(creature.dna[i], is(false)));
    }

    @Test
    public void testMimic() throws Exception {
        double fitness = 20.0 * ThreadLocalRandom.current().nextDouble() - 10.0;

        Creature creature = new Creature(8);
        creature.dna[2] = true;
        creature.fitness = fitness;

        Creature theThing = new Creature(8);
        creature.dna[5] = true;
        theThing.fitness = 0.5;

        assertThat(theThing.fitness, is(not(fitness)));
        assertThat(Arrays.equals(theThing.dna, creature.dna), is(false));

        theThing.mimic(creature);

        assertThat(theThing.fitness, is(fitness));
        assertThat(Arrays.equals(theThing.dna, creature.dna), is(true));
    }

    @Test
    public void testInherit() {
        Creature creature1 = new Creature(15);
        Creature creature2 = new Creature(15);
        creature2.flip(5);
        creature2.flip(6);
        creature2.flip(7);
        creature2.flip(8);

        assertThat(creature1.describe(10, 4), is(not("1111")));
        creature1.inherit(creature2, 5, 10, 4);
        assertThat(creature1.describe(10, 4), is("1111"));
    }

    @Test
    public void testExtract() {
        Creature creature = new Creature(64);
        IntStream.range(0, creature.dna.length).forEach(i -> creature.dna[i] = ThreadLocalRandom.current().nextDouble() > 0.5);
        int offset = ThreadLocalRandom.current().nextInt(creature.dna.length - 10);
        int length = ThreadLocalRandom.current().nextInt(creature.dna.length - offset) + 1;

        String sequence = Long.toBinaryString(ThreadLocalRandom.current().nextLong()).substring(0, length);
        IntStream.range(0, length).forEach(i -> creature.dna[offset + i] = sequence.charAt(i) != '0');

        assertThat(creature.extract(offset, length), is(Long.valueOf(sequence, 2)));
    }

    @Test
    public void testSplice() {
        Creature creature = new Creature(32);
        int offset = ThreadLocalRandom.current().nextInt(creature.dna.length - 10);
        int length = ThreadLocalRandom.current().nextInt(creature.dna.length - offset) + 1;
        long sequence = ThreadLocalRandom.current().nextLong() & (1 << length) - 1;

        creature.splice(offset, length, sequence);

        String bits = IntStream.range(0, length).mapToObj(i -> creature.dna[offset + i] ? "1" : "0").collect(Collectors.joining(""));
        assertThat(Long.valueOf(bits, 2), is(sequence));
    }

    @Test
    public void testConfigure() {
        Creature creature = new Creature(11);

        creature.configure("1011101", 2);

        assertThat(creature.isSet(0), is(false));
        assertThat(creature.isSet(1), is(false));
        assertThat(creature.isSet(2), is(true));
        assertThat(creature.isSet(3), is(false));
        assertThat(creature.isSet(4), is(true));
        assertThat(creature.isSet(5), is(true));
        assertThat(creature.isSet(6), is(true));
        assertThat(creature.isSet(7), is(false));
        assertThat(creature.isSet(8), is(true));
        assertThat(creature.isSet(9), is(false));
        assertThat(creature.isSet(10), is(false));
    }

    @Test
    public void testDescribe() {
        Creature creature = new Creature(11);
        creature.flip(2);
        creature.flip(3);
        creature.flip(5);
        creature.flip(8);

        assertThat(creature.describe(2, 7), is("1101001"));
    }

    @Test
    public void testCompareTo() {
        List<Creature> creatures = Stream.generate(() -> new Creature(0)).limit(3).collect(Collectors.toList());
        creatures.get(0).fitness = 1.0;
        creatures.get(1).fitness = 2.0;
        creatures.get(2).fitness = 5.0;

        assertThat(creatures.get(0).getFitness(), is(1.0));
        assertThat(creatures.get(1).getFitness(), is(2.0));
        assertThat(creatures.get(2).getFitness(), is(5.0));

        Collections.sort(creatures);

        assertThat(creatures.get(0).getFitness(), is(5.0));
        assertThat(creatures.get(1).getFitness(), is(2.0));
        assertThat(creatures.get(2).getFitness(), is(1.0));
    }
}
