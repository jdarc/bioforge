package com.zynaps.demo.rostering;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.joda.time.Hours;

class Rules {

    private static final double HARD_SCORE = 1000.0;
    private final Collection<Shift> shifts;
    private final Collection<Employee> employees;
    private final int restTime;

    public Rules(Schedule schedule) {
        shifts = schedule.shifts.values();
        employees = schedule.employees.values();
        restTime = schedule.restTime;
    }

    public double evaluate(List<Assignment> assignments) {
        return processShiftRules(assignments) + processEmployeeRules(assignments);
    }

    private double processShiftRules(List<Assignment> assignments) {
        var fitness = 0.0;
        for (var shift : shifts) {
            var allocated = assignments.stream()
                                       .filter(a -> a.shift.id == shift.id)
                                       .map(assignment -> assignment.employee)
                                       .collect(Collectors.toList());
            if (allocated.size() > 0) {
                if (duplicates(allocated)) {
                    fitness -= HARD_SCORE;
                }

                for (var employee : allocated) {
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
        var fitness = 0.0;
        for (var employee : employees) {
            var shifts = assignments.stream().filter(a -> a.employee.id == employee.id)
                                    .map(assignment -> assignment.shift)
                                    .sorted(Comparator.comparing(a -> a.start))
                                    .collect(Collectors.toList());
            for (var i = 1; i < shifts.size(); ++i) {
                var timeBetween = Hours.hoursBetween(shifts.get(i - 1).end, shifts.get(i).start).getHours();
                if (timeBetween < restTime) {
                    fitness -= HARD_SCORE;
                }
            }
            fitness += shifts.size();
        }
        return fitness;
    }

    public boolean duplicates(List<Employee> items) {
        var size = items.size();
        for (var i = 0; i < size - 1; ++i) {
            var item = items.get(i);
            for (var j = i + 1; j < size; ++j) {
                if (item.id == items.get(j).id) {
                    return true;
                }
            }
        }
        return false;
    }
}
