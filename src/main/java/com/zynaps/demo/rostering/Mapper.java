package com.zynaps.demo.rostering;

import com.zynaps.bioforge.Creature;

import java.util.ArrayList;
import java.util.List;

class Mapper {

    public final int genomeLength;
    private final Schedule schedule;
    private final int allocs;
    private final int shiftBits;
    private final int employeeBits;

    public Mapper(Schedule schedule) {
        this.schedule = schedule;
        allocs = schedule.shifts.values().stream().mapToInt(shift -> shift.required).sum();
        shiftBits = bitsNeeded(schedule.shifts.values().stream().max((a, b) -> Integer.compare(a.id, b.id)).get().id);
        employeeBits = bitsNeeded(schedule.employees.values().stream().max((a, b) -> Integer.compare(a.id, b.id)).get().id);
        genomeLength = allocs * (shiftBits + employeeBits);
    }

    private static int bitsNeeded(int a) {
        return (int) (Math.floor(Math.log(a) / Math.log(2.0)) + 1.0);
    }

    public List<Assignment> decode(Creature creature) {
        final int step = shiftBits + employeeBits;
        final int shiftMask = (1 << shiftBits) - 1;
        final int employeeMask = (1 << employeeBits) - 1;
        final ArrayList<Assignment> assignments = new ArrayList<>(allocs);
        for (int i = 0; i < allocs; ++i) {
            long strand = creature.extract(i * step, step);
            Shift shift = schedule.getShiftById((int) (strand >>> employeeBits & shiftMask));
            Employee employee = schedule.getEmployeeById((int) (strand & employeeMask));
            if (shift != Shift.NULL && employee != Employee.NULL) {
                assignments.add(new Assignment(shift, employee));
            }
        }
        return assignments;
    }
}
