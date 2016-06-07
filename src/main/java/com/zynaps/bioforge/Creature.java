package com.zynaps.bioforge;

import java.util.Arrays;

public class Creature implements Comparable<Creature> {

    final boolean[] dna;
    double fitness;

    public Creature(int genomeSize) {
        dna = new boolean[genomeSize];
    }

    public int getGenomeSize() {
        return dna.length;
    }

    public double getFitness() {
        return fitness;
    }

    public boolean isSet(int index) {
        return dna[index];
    }

    public void flip(int index) {
        dna[index] = !dna[index];
    }

    public void zero() {
        Arrays.fill(dna, false);
    }

    public void mimic(Creature other) {
        fitness = other.fitness;
        System.arraycopy(other.dna, 0, dna, 0, other.dna.length);
    }

    public void inherit(Creature other, int srcpos, int dstpos, int length) {
        System.arraycopy(other.dna, srcpos, dna, dstpos, length);
    }

    public long extract(int offset, int length) {
        long result = 0;
        for (int i = 0; i < length; ++i) {
            result = (result << 1) | (dna[offset + i] ? 1 : 0);
        }
        return result;
    }

    public void splice(int offset, int length, long sequence) {
        int index = offset + length - 1;
        for (int i = 0; i < length; ++i) {
            dna[index - i] = (0x1 & sequence >> i) != 0;
        }
    }

    public void configure(String sequence) {
        configure(sequence, 0);
    }

    public void configure(String sequence, int offset) {
        for (int i = 0; i < sequence.length(); i++) {
            dna[offset + i] = sequence.charAt(i) != '0';
        }
    }

    public String describe() {
        return describe(0, dna.length);
    }

    public String describe(int offset, int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = isSet(offset + i) ? '1' : '0';
        }
        return new String(chars);
    }

    @Override
    public int compareTo(Creature other) {
        return Double.compare(other.fitness, fitness);
    }
}
