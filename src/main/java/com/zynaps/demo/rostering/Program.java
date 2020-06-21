package com.zynaps.demo.rostering;

import com.zynaps.bioforge.Builder;
import com.zynaps.bioforge.schemes.RouletteSelection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.joda.time.format.DateTimeFormat;

class Program {

    public static void main(String[] args) {
        var schedule = Generator.makeArchtype(); // 64, 32, 11
        var rules = new Rules(schedule);
        var mapper = new Mapper(schedule);
        var island = new Builder().tribes(1).selectionScheme(new RouletteSelection(.75))
                                  .populationSize(100)
                                  .genomeSize(mapper.genomeLength)
                                  .crossoverRate(0.3)
                                  .mutationRate(0.0003)
                                  .nuke(true)
                                  .build();
        System.out.println(output(new Controller(schedule, island, mapper, rules).evolve(60)));
    }

    private static String output(List<Assignment> solution) {
        var sorted = solution.stream()
                             .collect(Collectors.groupingBy(assignment -> assignment.shift));

        var fullFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        var shortTimeFormat = DateTimeFormat.forPattern("HH:mm");

        var shifts = sorted.keySet()
                           .stream()
                           .sorted(Comparator.comparingLong(a -> a.start.getMillis()))
                           .collect(Collectors.toList());

        var sb = new StringBuilder();
        sb.append(System.lineSeparator());
        for (var shift : shifts) {
            sb.append(String.format("Shift: %d, Requires: %d, Start: %s, End: %s",
                                    shift.id,
                                    shift.required,
                                    shift.start.toString(fullFormat),
                                    shift.end.toString(fullFormat)));
            sb.append(System.lineSeparator());
            var assignments = sorted.get(shift)
                                    .stream()
                                    .sorted(Comparator.comparingInt(a -> a.employee.id))
                                    .collect(Collectors.toList());
            for (var assignment : assignments) {
                sb.append(String.format(" Employee: %d", assignment.employee.id));
                sb.append(System.lineSeparator());
                for (var a : assignment.employee.absences) {
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
