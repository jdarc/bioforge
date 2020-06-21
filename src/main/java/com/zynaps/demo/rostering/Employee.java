package com.zynaps.demo.rostering;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.joda.time.Interval;

class Employee {

    public static final Employee NULL = new Employee(0);

    public final int id;
    public final List<Absence> absences;

    public Employee(int id, Absence... absences) {
        this(id, Arrays.asList(absences));
    }

    public Employee(int id, List<Absence> absences) {
        this.id = id;
        this.absences = Collections.unmodifiableList(absences);
    }

    public boolean isAbsentDuring(Shift shift) {
        var interval = new Interval(shift.start, shift.end);
        return absences.stream().anyMatch(absence -> absence.overlaps(interval));
    }
}
