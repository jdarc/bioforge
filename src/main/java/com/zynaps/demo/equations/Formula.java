package com.zynaps.demo.equations;

class Formula {

    private static final char[] OPERANDS = {'+', '-', '*', '/'};

    private final byte a;
    private final byte op1;
    private final byte b;
    private final byte op2;
    private final byte c;
    private final byte op3;
    private final byte d;
    private final byte op4;
    private final byte e;
    private final byte op5;
    private final byte f;

    public Formula(long gene) {
        a = (byte)((0x7 & gene) % 6);
        op1 = (byte)(0x3 & gene >> 3);
        b = (byte)((0x7 & gene >> 5) % 6);
        op2 = (byte)(0x3 & gene >> 8);
        c = (byte)((0x7 & gene >> 10) % 6);
        op3 = (byte)(0x3 & gene >> 13);
        d = (byte)((0x7 & gene >> 15) % 6);
        op4 = (byte)(0x3 & gene >> 18);
        e = (byte)((0x7 & gene >> 20) % 6);
        op5 = (byte)(0x3 & gene >> 23);
        f = (byte)((0x7 & gene >> 25) % 6);
    }

    public boolean isValid() {
        byte[] mark = new byte[6];
        return (++mark[a] | ++mark[b] | ++mark[c] | ++mark[d] | ++mark[e] | ++mark[f]) < 2;
    }

    public String toFormula(int[] numbers) {
        return String.format("%d%s%d%s%d%s%d%s%d%s%d",
                             numbers[a], OPERANDS[op1],
                             numbers[b], OPERANDS[op2],
                             numbers[c], OPERANDS[op3],
                             numbers[d], OPERANDS[op4],
                             numbers[e], OPERANDS[op5],
                             numbers[f]);
    }
}
