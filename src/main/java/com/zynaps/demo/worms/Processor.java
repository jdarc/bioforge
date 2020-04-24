package com.zynaps.demo.worms;

import java.util.Arrays;

class Processor {

    public static final int PROGRAM_SIZE = 4096;
    public static final int INSTRUCTION_SIZE = 32;

    private final int[] instructions = new int[PROGRAM_SIZE];
    private final double[] registers = new double[32];

    public double peek(int offset) {
        return registers[offset];
    }

    public void poke(int offset, double value) {
        registers[offset] = value;
    }

    public void clear() {
        Arrays.fill(registers, 0.0);
    }

    public void load(int... code) {
        System.arraycopy(code, 0, instructions, 0, code.length);
        instructions[PROGRAM_SIZE - 1] = 0x0;
    }

    public void run(int cycles) {
        int pc = 0;
        int cycle = 0;
        while (cycle++ < cycles) {
            final int instruction = instructions[pc++];
            final int opc = (0x01F & instruction >> 27);
            final int dst = (0x01F & instruction >> 22);
            final int opa = (0x01F & instruction >> 17);
            final int opb = (0x01F & instruction >> 12);
            final int mem = (0xFFF & instruction);
            switch (opc) {
                case 0:
                    return;
                case 1:
                    pc = registers[opa] != 0.0 ? mem : pc;
                    break;
                case 2:
                    pc = registers[opa] > registers[opb] ? mem : pc;
                    break;
                case 3:
                    pc = registers[opa] <= registers[opb] ? mem : pc;
                    break;
                case 4:
                    break;
                case 5:
                    registers[dst] = 0.0;
                    break;
                case 6:
                    registers[dst] = ((0x3FFFFF & instruction) - 0x200000);
                    break;
                case 7:
                    registers[dst] = ((0x3FFFFF & instruction) - 0x200000) / 512.0;
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    registers[dst] = registers[opa] + registers[opb];
                    break;
                case 11:
                    registers[dst] = registers[opa] - registers[opb];
                    break;
                case 12:
                    registers[dst] = registers[opa] * registers[opb];
                    break;
                case 13:
                    registers[dst] = registers[opa] / registers[opb];
                    break;
                case 14:
                    registers[dst] = registers[opa] % registers[opb];
                    break;
                case 15:
                    break;
                case 16:
                    registers[dst] = registers[dst] + registers[opa] * registers[opb];
                    break;
                case 17:
                    break;
                case 18:
                    registers[dst] = Math.ceil(registers[opa]);
                    break;
                case 19:
                    registers[dst] = Math.floor(registers[opa]);
                    break;
                case 20:
                    registers[dst] = Math.sin(registers[opa]);
                    break;
                case 21:
                    registers[dst] = Math.cos(registers[opa]);
                    break;
                case 22:
                    registers[dst] = Math.tan(registers[opa]);
                    break;
                case 23:
                    registers[dst] = Math.asin(registers[opa]);
                    break;
                case 24:
                    registers[dst] = Math.acos(registers[opa]);
                    break;
                case 25:
                    registers[dst] = Math.atan(registers[opa]);
                    break;
                case 26:
                    break;
                case 27:
                    registers[dst] = Math.sqrt(Math.abs(registers[opa]));
                    break;
                case 28:
                    break;
                case 29:
                    registers[dst] = Math.min(registers[opa], registers[opb]);
                    break;
                case 30:
                    registers[dst] = Math.max(registers[opa], registers[opb]);
                    break;
                case 31:
                    registers[dst] = Math.hypot(registers[opa], registers[opb]);
                    break;
            }
            if (!Double.isFinite(registers[dst])) {
                registers[dst] = 0.0;
                return;
            }
        }
    }
}
