package com.zynaps.demo.rostering;

import org.joda.time.Hours;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class Rules {

    private static final double HARD_SCORE = 1000.0;
    private final Collection<Shift> shifts;
    private final Collection<Employee> employees;
    private final int restTime;

    public Rules(Schedule schedule) {
        this.shifts = schedule.shifts.values();
        this.employees = schedule.employees.values();
        this.restTime = schedule.restTime;
    }

    public double evaluate(List<Assignment> assignments) {
        return processShiftRules(assignments) + processEmployeeRules(assignments);
    }

    private double processShiftRules(List<Assignment> assignments) {
        double fitness = 0.0;
        for (Shift shift : shifts) {
            List<Employee> allocated = assignments.stream()
                                                  .filter(a -> a.shift.id == shift.id)
                                                  .map(assignment -> assignment.employee)
                                                  .collect(Collectors.toList());
            if (allocated.size() > 0) {
                if (duplicates(allocated)) {
                    fitness -= HARD_SCORE;
                }

                for (Employee employee : allocated) {
                    if (employee.isAbsentDuring(shift)) {
                        fitness -= HARD_SCORE;
                    }
                }

                fitness += Math.min(allocated.size(), shift.required) - Math.max(0, allocated.size() - shift.required);
            } else {
                fitness -= shift.required;
            }
        }
        return fitness;
    }

    private double processEmployeeRules(List<Assignment> assignments) {
        double fitness = 0.0;
        for (Employee employee : employees) {
            List<Shift> shifts = assignments.stream()
                                            .filter(a -> a.employee.id == employee.id)
                                            .map(assignment -> assignment.shift)
                                            .collect(Collectors.toList());
            shifts.sort((a, b) -> a.start.compareTo(b.start));
            for (int i = 1; i < shifts.size(); ++i) {
                int timeBetween = Hours.hoursBetween(shifts.get(i - 1).end, shifts.get(i).start).getHours();
                if (timeBetween < restTime) {
                    fitness -= HARD_SCORE;
                }
            }
            fitness += shifts.size();
        }
        return fitness;
    }

    public boolean duplicates(List<Employee> items) {
        int size = items.size();
        for (int i = 0; i < size - 1; ++i) {
            Employee item = items.get(i);
            for (int j = i + 1; j < size; ++j) {
                if (item.id == items.get(j).id) {
                    return true;
                }
            }
        }
        return false;
    }
}
