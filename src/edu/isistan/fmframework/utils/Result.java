package edu.isistan.fmframework.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

public class Result {

    String tag;
    ArrayList<Long> times;
    TimeMeter.Precision precision;

    double average;
    double deviation;

    long min;
    long max;


    Result(Measure measure) {
        tag = measure.tag;
        times = measure.times;
        precision = measure.precision;

        average = average();
        deviation = deviation();

        long[] minMax = range();
        min = minMax[0];
        max = minMax[1];
    }

    private double average() {
        int size = times.size();
        long total = 0;
        for (int i = 0; i < size; i++) {
            total += times.get(i);
        }
        return total / (double) size;
    }

    private long[] range() {
        int size = times.size();
        long min = times.get(0);
        long max = times.get(0);
        for (int i = 0; i < size; i++) {
            long time = times.get(i);
            if (time > max) {
                max = time;
            } else if (time < min) {
                min = time;
            }
        }

        return new long[]{min, max};
    }

    private double deviation() {
        double mean = average();
        double temp = 0;

        int size = times.size();
        for (int i = 0; i < size; i++) {
            long t = times.get(i);
            temp += (mean - t) * (mean - t);
        }
        return Math.sqrt(temp / size);
    }

    double getStat(TimeMeter.Stat stat) {
        switch (stat) {
            case AVERAGE:
                return average;
            case STANDARD_DEVIATION:
                return deviation;
            case RANGE:
                return max - min;
            default:
                throw new IllegalArgumentException("Stat not implemented yet: " + stat);
        }
    }

    public Result log() {
        List<Pair<String, String>> stats = new ArrayList<>();

        stats.add(new Pair<>("Sample Size", times.size() + ""));

        if (TimeMeter.STATISTICS.contains(TimeMeter.Stat.AVERAGE)) {
            stats.add(new Pair<>("Average", time(average) + precision.unit));
        }
        if (TimeMeter.STATISTICS.contains(TimeMeter.Stat.RANGE)) {
            stats.add(new Pair<>("Range", time(min) + precision.unit + " --> " + time(max) + precision.unit));
        }
        if (TimeMeter.STATISTICS.contains(TimeMeter.Stat.STANDARD_DEVIATION)) {
            stats.add(new Pair<>("Deviation", time(deviation) + precision.unit));
        }

        TimeMeter.logMany(tag, stats);
        return this;
    }

    private long time(long stat) {
        return Math.round(stat / (double) precision.divider);
    }

    private long time(double stat) {
        return Math.round(stat / precision.divider);
    }
}