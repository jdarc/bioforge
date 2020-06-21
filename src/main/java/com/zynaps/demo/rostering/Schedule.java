package com.zynaps.demo.rostering;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Schedule {

    public final Map<Integer, Shift> shifts;
    public final Map<Integer, Employee> employees;
    public final int restTime;

    public Schedule(List<Shift> shifts, List<Employee> employees, int restTime) {
        this.shifts = shifts.stream().collect(Collectors.toMap(key -> key.id, item -> item));
        this.employees = employees.stream().collect(Collectors.toMap(key -> key.id, item -> item));
        this.restTime = Math.max(0, restTime);
    }

    public Shift getShiftById(int id) {
        return shifts.getOrDefault(id, Shift.NULL);
    }

    public Employee getEmployeeById(int id) {
        return employees.getOrDefault(id, Employee.NULL);
    }
}
