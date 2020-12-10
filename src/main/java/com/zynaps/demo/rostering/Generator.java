package com.zynaps.demo.rostering;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

class Generator {

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private Generator() {
    }

    public static Schedule makeRandom(int totalShifts, int totalEmployees, int restTime) {
        List<Shift> shifts = new ArrayList<>(totalShifts);
        List<Employee> employees = new ArrayList<>(totalEmployees);

        var startOfWeek = new LocalDate().withDayOfWeek(DateTimeConstants.MONDAY).toDateTimeAtStartOfDay();
        for (var i = 0; i < totalShifts; ++i) {
            var start = startOfWeek.plusDays((int) (Math.random() * 7)).plusHours((int) (Math.random() * 8));
            shifts.add(new Shift(i + 1, start, start.plusHours(8), (int) (1 + Math.random() * 3)));
        }

        for (var i = 0; i < totalEmployees; ++i) {
            var total = (int) (0 + Math.random() * 3);
            List<Absence> absences = new ArrayList<>(total);
            while (total-- > 0) {
                var hours = (int) (Math.random() * 8);
                var start = startOfWeek.plusDays((int) (Math.random() * 7)).plusHours(hours);
                absences.add(new Absence(start, start.plusHours(1 + (int) (Math.random() * (23 - hours)))));
            }
            employees.add(new Employee(i + 1, absences));
        }

        return new Schedule(shifts, employees, restTime);
    }

    public static Schedule makeArchtype() {
        List<Shift> shifts = new ArrayList<>();
        shifts.add(new Shift(1, parse("2015-07-13 06:30:00"), parse("2015-07-13 14:00:00"), 1));
        shifts.add(new Shift(2, parse("2015-07-13 06:30:00"), parse("2015-07-13 14:00:00"), 1));
        shifts.add(new Shift(3, parse("2015-07-13 06:30:00"), parse("2015-07-13 14:00:00"), 1));
        shifts.add(new Shift(4, parse("2015-07-13 07:30:00"), parse("2015-07-13 14:00:00"), 1));
        shifts.add(new Shift(5, parse("2015-07-13 07:30:00"), parse("2015-07-13 14:00:00"), 1));
        shifts.add(new Shift(6, parse("2015-07-13 07:30:00"), parse("2015-07-13 14:00:00"), 1));
        shifts.add(new Shift(7, parse("2015-07-13 09:00:00"), parse("2015-07-13 14:00:00"), 1));
        shifts.add(new Shift(8, parse("2015-07-13 09:00:00"), parse("2015-07-13 14:00:00"), 1));
        shifts.add(new Shift(9, parse("2015-07-13 09:00:00"), parse("2015-07-13 14:00:00"), 1));
        shifts.add(new Shift(10, parse("2015-07-13 14:00:00"), parse("2015-07-13 19:00:00"), 1));
        shifts.add(new Shift(11, parse("2015-07-13 14:00:00"), parse("2015-07-13 19:00:00"), 1));
        shifts.add(new Shift(12, parse("2015-07-13 14:00:00"), parse("2015-07-13 19:00:00"), 1));
        shifts.add(new Shift(13, parse("2015-07-13 16:45:00"), parse("2015-07-13 22:00:00"), 1));
        shifts.add(new Shift(14, parse("2015-07-13 16:45:00"), parse("2015-07-13 22:00:00"), 1));
        shifts.add(new Shift(15, parse("2015-07-13 16:45:00"), parse("2015-07-13 22:00:00"), 1));
        shifts.add(new Shift(16, parse("2015-07-14 06:00:00"), parse("2015-07-14 14:00:00"), 1));
        shifts.add(new Shift(17, parse("2015-07-14 06:00:00"), parse("2015-07-14 14:00:00"), 1));
        shifts.add(new Shift(18, parse("2015-07-14 06:00:00"), parse("2015-07-14 14:00:00"), 1));
        shifts.add(new Shift(19, parse("2015-07-14 07:30:00"), parse("2015-07-14 14:00:00"), 1));
        shifts.add(new Shift(20, parse("2015-07-14 07:30:00"), parse("2015-07-14 14:00:00"), 1));
        shifts.add(new Shift(21, parse("2015-07-14 07:30:00"), parse("2015-07-14 14:00:00"), 1));
        shifts.add(new Shift(22, parse("2015-07-14 09:00:00"), parse("2015-07-14 14:00:00"), 1));
        shifts.add(new Shift(23, parse("2015-07-14 09:00:00"), parse("2015-07-14 14:00:00"), 1));
        shifts.add(new Shift(24, parse("2015-07-14 09:00:00"), parse("2015-07-14 14:00:00"), 1));
        shifts.add(new Shift(25, parse("2015-07-14 14:00:00"), parse("2015-07-14 19:00:00"), 1));
        shifts.add(new Shift(26, parse("2015-07-14 14:00:00"), parse("2015-07-14 19:00:00"), 1));
        shifts.add(new Shift(27, parse("2015-07-14 14:00:00"), parse("2015-07-14 19:00:00"), 1));
        shifts.add(new Shift(28, parse("2015-07-14 16:45:00"), parse("2015-07-14 22:00:00"), 1));
        shifts.add(new Shift(29, parse("2015-07-14 16:45:00"), parse("2015-07-14 22:00:00"), 1));
        shifts.add(new Shift(30, parse("2015-07-14 16:45:00"), parse("2015-07-14 22:00:00"), 1));
        shifts.add(new Shift(31, parse("2015-07-14 22:00:00"), parse("2015-07-15 06:30:00"), 2));
        shifts.add(new Shift(32, parse("2015-07-15 06:30:00"), parse("2015-07-15 14:00:00"), 1));
        shifts.add(new Shift(33, parse("2015-07-15 06:30:00"), parse("2015-07-15 14:00:00"), 2));
        shifts.add(new Shift(34, parse("2015-07-15 06:30:00"), parse("2015-07-15 14:00:00"), 1));
        shifts.add(new Shift(35, parse("2015-07-15 07:30:00"), parse("2015-07-15 14:00:00"), 1));
        shifts.add(new Shift(36, parse("2015-07-15 07:30:00"), parse("2015-07-15 14:00:00"), 1));
        shifts.add(new Shift(37, parse("2015-07-15 07:30:00"), parse("2015-07-15 14:00:00"), 1));
        shifts.add(new Shift(38, parse("2015-07-15 09:00:00"), parse("2015-07-15 14:00:00"), 1));
        shifts.add(new Shift(39, parse("2015-07-15 09:00:00"), parse("2015-07-15 14:00:00"), 1));
        shifts.add(new Shift(40, parse("2015-07-15 09:00:00"), parse("2015-07-15 14:00:00"), 1));
        shifts.add(new Shift(41, parse("2015-07-15 14:00:00"), parse("2015-07-15 19:00:00"), 1));
        shifts.add(new Shift(42, parse("2015-07-15 14:00:00"), parse("2015-07-15 19:00:00"), 1));
        shifts.add(new Shift(43, parse("2015-07-15 14:00:00"), parse("2015-07-15 19:00:00"), 1));
        shifts.add(new Shift(44, parse("2015-07-15 16:45:00"), parse("2015-07-15 22:00:00"), 1));
        shifts.add(new Shift(45, parse("2015-07-15 16:45:00"), parse("2015-07-15 22:00:00"), 1));
        shifts.add(new Shift(46, parse("2015-07-15 16:45:00"), parse("2015-07-15 22:00:00"), 1));
        shifts.add(new Shift(47, parse("2015-07-15 22:00:00"), parse("2015-07-16 06:30:00"), 2));
        shifts.add(new Shift(48, parse("2015-07-16 06:30:00"), parse("2015-07-16 14:00:00"), 1));
        shifts.add(new Shift(49, parse("2015-07-16 06:30:00"), parse("2015-07-16 14:00:00"), 2));
        shifts.add(new Shift(50, parse("2015-07-16 06:30:00"), parse("2015-07-16 14:00:00"), 1));
        shifts.add(new Shift(51, parse("2015-07-16 07:30:00"), parse("2015-07-16 14:00:00"), 1));
        shifts.add(new Shift(52, parse("2015-07-16 07:30:00"), parse("2015-07-16 14:00:00"), 1));
        shifts.add(new Shift(53, parse("2015-07-16 07:30:00"), parse("2015-07-16 14:00:00"), 1));
        shifts.add(new Shift(54, parse("2015-07-16 09:00:00"), parse("2015-07-16 14:00:00"), 1));
        shifts.add(new Shift(55, parse("2015-07-16 09:00:00"), parse("2015-07-16 14:00:00"), 1));
        shifts.add(new Shift(56, parse("2015-07-16 09:00:00"), parse("2015-07-16 14:00:00"), 1));
        shifts.add(new Shift(57, parse("2015-07-16 14:00:00"), parse("2015-07-16 19:00:00"), 1));
        shifts.add(new Shift(58, parse("2015-07-16 14:00:00"), parse("2015-07-16 19:00:00"), 1));
        shifts.add(new Shift(59, parse("2015-07-16 14:00:00"), parse("2015-07-16 19:00:00"), 1));
        shifts.add(new Shift(60, parse("2015-07-16 16:45:00"), parse("2015-07-16 22:00:00"), 1));
        shifts.add(new Shift(61, parse("2015-07-16 16:45:00"), parse("2015-07-16 22:00:00"), 1));
        shifts.add(new Shift(62, parse("2015-07-16 16:45:00"), parse("2015-07-16 22:00:00"), 1));
        shifts.add(new Shift(63, parse("2015-07-16 22:00:00"), parse("2015-07-17 06:30:00"), 2));
        shifts.add(new Shift(64, parse("2015-07-17 06:30:00"), parse("2015-07-17 14:00:00"), 1));
        shifts.add(new Shift(65, parse("2015-07-17 06:30:00"), parse("2015-07-17 14:00:00"), 1));
        shifts.add(new Shift(66, parse("2015-07-17 06:30:00"), parse("2015-07-17 14:00:00"), 1));
        shifts.add(new Shift(67, parse("2015-07-17 07:30:00"), parse("2015-07-17 14:00:00"), 1));
        shifts.add(new Shift(68, parse("2015-07-17 07:30:00"), parse("2015-07-17 14:00:00"), 1));
        shifts.add(new Shift(69, parse("2015-07-17 07:30:00"), parse("2015-07-17 14:00:00"), 1));
        shifts.add(new Shift(70, parse("2015-07-17 09:00:00"), parse("2015-07-17 14:00:00"), 1));
        shifts.add(new Shift(71, parse("2015-07-17 09:00:00"), parse("2015-07-17 14:00:00"), 1));
        shifts.add(new Shift(72, parse("2015-07-17 09:00:00"), parse("2015-07-17 14:00:00"), 1));
        shifts.add(new Shift(73, parse("2015-07-17 14:00:00"), parse("2015-07-17 19:00:00"), 1));
        shifts.add(new Shift(74, parse("2015-07-17 14:00:00"), parse("2015-07-17 19:00:00"), 1));
        shifts.add(new Shift(75, parse("2015-07-17 14:00:00"), parse("2015-07-17 19:00:00"), 1));
        shifts.add(new Shift(76, parse("2015-07-17 16:45:00"), parse("2015-07-17 22:00:00"), 1));
        shifts.add(new Shift(77, parse("2015-07-17 16:45:00"), parse("2015-07-17 22:00:00"), 1));
        shifts.add(new Shift(78, parse("2015-07-17 16:45:00"), parse("2015-07-17 22:00:00"), 1));
        shifts.add(new Shift(79, parse("2015-07-17 14:00:00"), parse("2015-07-17 22:30:00"), 2));
        shifts.add(new Shift(80, parse("2015-07-18 06:30:00"), parse("2015-07-18 14:00:00"), 1));
        shifts.add(new Shift(81, parse("2015-07-18 06:30:00"), parse("2015-07-18 14:00:00"), 1));
        shifts.add(new Shift(82, parse("2015-07-18 06:30:00"), parse("2015-07-18 14:00:00"), 1));
        shifts.add(new Shift(83, parse("2015-07-18 07:30:00"), parse("2015-07-18 14:00:00"), 1));
        shifts.add(new Shift(84, parse("2015-07-18 07:30:00"), parse("2015-07-18 14:00:00"), 1));
        shifts.add(new Shift(85, parse("2015-07-18 07:30:00"), parse("2015-07-18 14:00:00"), 1));
        shifts.add(new Shift(86, parse("2015-07-18 09:00:00"), parse("2015-07-18 15:00:00"), 1));
        shifts.add(new Shift(87, parse("2015-07-18 09:00:00"), parse("2015-07-18 15:00:00"), 1));
        shifts.add(new Shift(88, parse("2015-07-18 09:00:00"), parse("2015-07-18 15:00:00"), 1));
        shifts.add(new Shift(89, parse("2015-07-18 14:00:00"), parse("2015-07-18 19:00:00"), 1));
        shifts.add(new Shift(90, parse("2015-07-18 14:00:00"), parse("2015-07-18 19:00:00"), 1));
        shifts.add(new Shift(91, parse("2015-07-18 14:00:00"), parse("2015-07-18 19:00:00"), 1));
        shifts.add(new Shift(92, parse("2015-07-18 16:45:00"), parse("2015-07-18 22:00:00"), 1));
        shifts.add(new Shift(93, parse("2015-07-18 16:45:00"), parse("2015-07-18 22:00:00"), 1));
        shifts.add(new Shift(94, parse("2015-07-18 16:45:00"), parse("2015-07-18 22:00:00"), 1));
        shifts.add(new Shift(95, parse("2015-07-18 14:00:00"), parse("2015-07-18 22:30:00"), 2));
        shifts.add(new Shift(96, parse("2015-07-19 06:30:00"), parse("2015-07-19 14:00:00"), 1));
        shifts.add(new Shift(97, parse("2015-07-19 06:30:00"), parse("2015-07-19 14:00:00"), 1));
        shifts.add(new Shift(98, parse("2015-07-19 06:30:00"), parse("2015-07-19 14:00:00"), 1));
        shifts.add(new Shift(99, parse("2015-07-19 07:30:00"), parse("2015-07-19 14:00:00"), 1));
        shifts.add(new Shift(100, parse("2015-07-19 07:30:00"), parse("2015-07-19 14:00:00"), 1));
        shifts.add(new Shift(101, parse("2015-07-19 07:30:00"), parse("2015-07-19 14:00:00"), 1));
        shifts.add(new Shift(102, parse("2015-07-19 09:00:00"), parse("2015-07-19 14:00:00"), 1));
        shifts.add(new Shift(103, parse("2015-07-19 09:00:00"), parse("2015-07-19 14:00:00"), 1));
        shifts.add(new Shift(104, parse("2015-07-19 09:00:00"), parse("2015-07-19 14:00:00"), 1));
        shifts.add(new Shift(105, parse("2015-07-19 14:00:00"), parse("2015-07-19 19:00:00"), 1));
        shifts.add(new Shift(106, parse("2015-07-19 14:00:00"), parse("2015-07-19 19:00:00"), 1));
        shifts.add(new Shift(107, parse("2015-07-19 14:00:00"), parse("2015-07-19 19:00:00"), 1));
        shifts.add(new Shift(108, parse("2015-07-19 16:45:00"), parse("2015-07-19 22:00:00"), 1));
        shifts.add(new Shift(109, parse("2015-07-19 16:45:00"), parse("2015-07-19 22:00:00"), 1));
        shifts.add(new Shift(110, parse("2015-07-19 16:45:00"), parse("2015-07-19 22:00:00"), 1));
        shifts.add(new Shift(111, parse("2015-07-19 14:00:00"), parse("2015-07-19 22:30:00"), 2));

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, absence("2015-07-16 00:00:00", "2015-07-26 23:59:00"),
                                   absence("2015-07-16 06:00:00", "2015-07-24 23:00:00")));
        employees.add(new Employee(2, absence("2015-07-14 00:00:00", "2015-07-28 23:00:00")));
        employees.add(new Employee(3, absence("2015-07-16 01:45:00", "2015-07-16 18:45:00"),
                                   absence("2015-07-18 00:00:00", "2015-07-18 20:00:00")));
        employees.add(new Employee(4, absence("2015-07-13 06:15:00", "2015-07-13 10:15:00"),
                                   absence("2015-07-14 06:15:00", "2015-07-14 10:15:00"),
                                   absence("2015-07-16 06:15:00", "2015-07-16 10:15:00"),
                                   absence("2015-07-15 06:15:00", "2015-07-15 10:15:00"),
                                   absence("2015-07-17 06:15:00", "2015-07-17 10:15:00"),
                                   absence("2015-07-18 06:15:00", "2015-07-18 10:15:00"),
                                   absence("2015-07-19 06:15:00", "2015-07-19 10:15:00")));
        employees.add(new Employee(5));
        employees.add(new Employee(6, absence("2015-07-16 00:00:00", "2015-07-16 23:59:00")));
        employees.add(new Employee(7));
        employees.add(new Employee(8, absence("2015-07-18 00:00:00", "2015-07-26 23:59:00")));
        employees.add(new Employee(9));
        employees.add(new Employee(10, absence("2015-07-17 15:00:00", "2015-07-17 23:00:00"),
                                   absence("2015-07-18 15:00:00", "2015-07-18 23:00:00"),
                                   absence("2015-07-13 06:00:00", "2015-07-13 23:00:00"),
                                   absence("2015-07-19 13:00:00", "2015-07-19 23:00:00"),
                                   absence("2015-07-14 06:00:00", "2015-07-14 17:00:00"),
                                   absence("2015-07-15 06:00:00", "2015-07-15 17:00:00"),
                                   absence("2015-07-16 06:00:00", "2015-07-16 17:00:00")));
        employees.add(new Employee(11, absence("2015-07-11 00:00:00", "2015-07-13 23:59:00")));
        employees.add(new Employee(12));
        employees.add(new Employee(13, absence("2015-07-17 00:00:00", "2015-07-27 23:59:00")));
        employees.add(new Employee(14));
        employees.add(new Employee(15, absence("2015-07-18 00:00:00", "2015-07-18 23:59:00")));
        employees.add(new Employee(16, absence("2015-07-13 00:00:00", "2015-07-13 14:00:00")));
        employees.add(new Employee(17));
        employees.add(new Employee(18));
        employees.add(new Employee(19, absence("2015-07-14 18:00:00", "2015-07-14 22:00:00"),
                                   absence("2015-07-16 18:00:00", "2015-07-16 22:00:00"),
                                   absence("2015-07-15 00:00:00", "2015-07-15 23:59:00"),
                                   absence("2015-07-17 00:00:00", "2015-07-17 23:59:00")));
        employees.add(new Employee(20));
        employees.add(new Employee(21));
        employees.add(new Employee(22, absence("2015-07-13 06:45:00", "2015-07-13 14:00:00"),
                                   absence("2015-07-15 06:45:00", "2015-07-15 14:00:00"),
                                   absence("2015-07-16 16:00:00", "2015-07-16 23:00:00"),
                                   absence("2015-07-17 00:00:00", "2015-07-17 23:59:00"),
                                   absence("2015-07-18 00:00:00", "2015-07-18 23:59:00"),
                                   absence("2015-07-19 00:00:00", "2015-07-19 23:59:00")));
        employees.add(new Employee(23, absence("2015-07-13 22:00:00", "2015-07-14 02:00:00")));
        employees.add(new Employee(24));
        employees.add(new Employee(25, absence("2015-07-17 15:00:00", "2015-07-19 23:00:00")));
        employees.add(new Employee(26));
        employees.add(new Employee(27, absence("2015-06-23 07:00:00", "2015-09-06 23:00:00"),
                                   absence("2015-07-06 07:00:00", "2015-07-31 23:00:00"),
                                   absence("2015-07-13 07:00:00", "2015-07-19 23:00:00")));
        employees.add(new Employee(28));
        employees.add(new Employee(29));
        employees.add(new Employee(30, absence("2015-07-14 10:00:00", "2015-07-14 22:15:00"),
                                   absence("2015-07-17 10:00:00", "2015-07-17 14:00:00")));
        employees.add(new Employee(31));
        employees.add(new Employee(32));
        employees.add(new Employee(33, absence("2015-07-13 00:00:00", "2015-07-13 23:59:00")));
        employees.add(new Employee(34));
        employees.add(new Employee(35));
        employees.add(new Employee(36, absence("2015-07-01 00:00:00", "2015-07-31 23:59:00")));
        employees.add(new Employee(37));
        employees.add(new Employee(38));
        employees.add(new Employee(39, absence("2015-07-14 00:00:00", "2015-07-14 23:59:00")));
        employees.add(new Employee(40));

        return new Schedule(shifts, employees, 11);
    }

    private static Absence absence(String start, String end) {
        return new Absence(parse(start), parse(end));
    }

    private static DateTime parse(String s) {
        return DateTime.parse(s, formatter);
    }
}
