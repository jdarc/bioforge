package com.zynaps.demo.equations;

import com.zynaps.bioforge.Creature;

class Formula {

    private static final char[] OPERANDS = {'+', '-', '*', '/'};

    private int a;
    private int op1;
    private int b;
    private int op2;
    private int c;
    private int op3;
    private int d;
    private int op4;
    private int e;
    private int op5;
    private int f;

    public Formula(Creature creature) {
        decodeGenes(creature.extract(0, 32));
    }

    private static boolean areIndicesDistinct(int... indices) {
        int[] accumulator = new int[indices.length];
        int mask = 0;
        for (int index : indices) {
            mask |= ++accumulator[index];
        }
        return mask == 1;
    }

    public boolean isValid() {
        return areIndicesDistinct(a, b, c, d, e, f);
    }

    public String toEquation(int[] numbers) {
        return String.format("%d%s%d%s%d%s%d%s%d%s%d",
                numbers[a], OPERANDS[op1],
                numbers[b], OPERANDS[op2],
                numbers[c], OPERANDS[op3],
                numbers[d], OPERANDS[op4],
                numbers[e], OPERANDS[op5],
                numbers[f]
        );
    }

    private void decodeGenes(long genes) {
        a = (int) ((0x7 & genes) % 6);
        op1 = (int) (0x3 & genes >> 3);
        b = (int) ((0x7 & genes >> 5) % 6);
        op2 = (int) (0x3 & genes >> 8);
        c = (int) ((0x7 & genes >> 10) % 6);
        op3 = (int) (0x3 & genes >> 13);
        d = (int) ((0x7 & genes >> 15) % 6);
        op4 = (int) (0x3 & genes >> 18);
        e = (int) ((0x7 & genes >> 20) % 6);
        op5 = (int) (0x3 & genes >> 23);
        f = (int) ((0x7 & genes >> 25) % 6);
    }
}
