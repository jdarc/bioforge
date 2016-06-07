package com.zynaps.demo.rostering;

class Assignment {

    public final Shift shift;
    public final Employee employee;

    public Assignment(Shift shift, Employee employee) {
        this.shift = shift;
        this.employee = employee;
    }
}
