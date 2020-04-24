package com.zynaps.demo.rostering;

import com.zynaps.bioforge.Builder;
import com.zynaps.bioforge.Island;
import com.zynaps.bioforge.schemes.RouletteSelection;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Program {

    public static void main(String[] args) {
        Schedule schedule = Generator.makeArchtype(); // 64, 32, 11
        Rules rules = new Rules(schedule);
        Mapper mapper = new Mapper(schedule);
        Island island = new Builder().tribes(1).selectionScheme(new RouletteSelection(.75))
                .populationSize(100)
                .genomeSize(mapper.genomeLength)
                .crossoverRate(0.3)
                .mutationRate(0.0003)
                .nuke(true)
                .build();
        System.out.println(output(new Controller(schedule, island, mapper, rules).evolve(60)));
    }

    private static String output(List<Assignment> solution) {
        Map<Shift, List<Assignment>> sorted = solution.stream()
                .collect(Collectors.groupingBy(assignment -> assignment.shift));

        DateTimeFormatter fullFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter shortTimeFormat = DateTimeFormat.forPattern("HH:mm");

        List<Shift> shifts = sorted.keySet()
                .stream()
                .sorted(Comparator.comparingLong(a -> a.start.getMillis()))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        for (Shift shift : shifts) {
            sb.append(String.format("Shift: %d, Requires: %d, Start: %s, End: %s",
                    shift.id,
                    shift.required,
                    shift.start.toString(fullFormat),
                    shift.end.toString(fullFormat)));
            sb.append(System.lineSeparator());
            List<Assignment> assignments = sorted.get(shift)
                    .stream()
                    .sorted((a, b) -> Integer.compare(a.employee.id, b.employee.id))
                    .collect(Collectors.toList());
            for (Assignment assignment : assignments) {
                sb.append(String.format(" Employee: %d", assignment.employee.id));
                sb.append(System.lineSeparator());
                for (Absence a : assignment.employee.absences) {
                    sb.append(String.format("  Absent: %s to %s",
                            a.start.toString(fullFormat),
                            a.end.toString(shortTimeFormat)));
                    sb.append(System.lineSeparator());
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
