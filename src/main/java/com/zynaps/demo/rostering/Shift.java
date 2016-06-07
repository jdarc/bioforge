package com.zynaps.demo.rostering;

import org.joda.time.DateTime;

class Shift {

    public static final Shift NULL = new Shift(0, new DateTime(0), new DateTime(0), 0);

    public final int id;
    public final DateTime start;
    public final DateTime end;
    public final int required;

    public Shift(int id, DateTime start, DateTime end, int required) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.required = required;
    }
}
