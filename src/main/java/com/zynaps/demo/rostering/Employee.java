package com.zynaps.demo.rostering;

import org.joda.time.Interval;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        Interval interval = new Interval(shift.start, shift.end);
        return absences.stream().anyMatch(absence -> absence.overlaps(interval));
    }
}
