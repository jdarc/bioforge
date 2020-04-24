package com.zynaps.demo.rostering;

import org.joda.time.DateTime;
import org.joda.time.Interval;

class Absence {

    public final DateTime start;
    public final DateTime end;
    private final Interval interval;

    public Absence(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
        interval = new Interval(start, end);
    }

    public boolean overlaps(Interval other) {
        return interval.overlaps(other);
    }
}
