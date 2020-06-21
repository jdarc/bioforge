package com.zynaps.demo.rostering;

import com.zynaps.bioforge.Creature;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparingInt;

class Mapper {

    public final int genomeLength;
    private final Schedule schedule;
    private final int allocs;
    private final int shiftBits;
    private final int employeeBits;

    public Mapper(Schedule schedule) {
        this.schedule = schedule;
        var shiftStream = schedule.shifts.values().stream();
        var employeeStream = schedule.employees.values().stream();
        allocs = shiftStream.mapToInt(shift -> shift.required).sum();
        shiftBits = bitsNeeded(shiftStream.max(comparingInt(a -> a.id)).orElseThrow().id);
        employeeBits = bitsNeeded(employeeStream.max(comparingInt(a -> a.id)).orElseThrow().id);
        genomeLength = allocs * (shiftBits + employeeBits);
    }

    private static int bitsNeeded(int a) {
        return (int) (Math.floor(Math.log(a) / Math.log(2.0)) + 1.0);
    }

    public List<Assignment> decode(Creature creature) {
        final var step = shiftBits + employeeBits;
        final var shiftMask = (1 << shiftBits) - 1;
        final var employeeMask = (1 << employeeBits) - 1;
        final var assignments = new ArrayList<Assignment>(allocs);
        for (var i = 0; i < allocs; ++i) {
            var strand = creature.extract(i * step, step);
            var shift = schedule.getShiftById((int) (strand >>> employeeBits & shiftMask));
            var employee = schedule.getEmployeeById((int) (strand & employeeMask));
            if (shift != Shift.NULL && employee != Employee.NULL) {
                assignments.add(new Assignment(shift, employee));
            }
        }
        return assignments;
    }
}
