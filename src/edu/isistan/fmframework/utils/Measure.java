package edu.isistan.fmframework.utils;

import java.util.ArrayList;

public class Measure {
    String tag;
    ArrayList<Long> times;
    TimeMeter.Precision precision;

    Measure(String tag, long time) {
        this.tag = tag;
        times = new ArrayList<>();
        times.add(time);
        precision = TimeMeter.DEFAULT_PRECISION;
    }

    public Measure precision(TimeMeter.Precision precision) {
        this.precision = precision;
        return this;
    }

    public void log() {
    	TimeMeter.log("Result", tag, Math.round(times.get(times.size() - 1) / precision.divider) + precision.unit);
    }

    Result result() {
        return new Result(this);
    }

    void add(long time) {
        times.add(time);
    }
}