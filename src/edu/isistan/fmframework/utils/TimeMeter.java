package edu.isistan.fmframework.utils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.Pair;

public class TimeMeter {

    static TimeMeter.Precision DEFAULT_PRECISION = TimeMeter.Precision.MICRO;
    static PrintStream DEFAULT_OUTPUT_LOG = System.out;
    static Set<Stat> STATISTICS = new HashSet<>();

    private static TimeMeter singleton;

    private HashMap<String, Long> starts;
    private HashMap<String, Measure> benchmarks;

    TimeMeter() {
        starts = new HashMap<>();
        benchmarks = new HashMap<>();
        TimeMeter.setEnabledStats(Stat.AVERAGE, Stat.RANGE, Stat.STANDARD_DEVIATION);
    }

    private static TimeMeter get() {
        if (singleton == null) {
            singleton = new TimeMeter();
        }
        return singleton;
    }

    public static TimeMeter begin(String tag) {
        if (singleton == null) {
            singleton = get();
        }
        return singleton.beginInternal(tag);
    }

    public static Measure end(String tag) {
        if (singleton == null) {
            singleton = get();
        }
        return singleton.endInternal(tag);
    }

    public static Result analyze(String tag) {
        if (singleton == null) {
            singleton = get();
        }
        return singleton.analyzeInternal(tag);
    }

    public static TimeMeter clear() {
        if (singleton == null) {
            singleton = get();
        }
        return singleton.clearInternal();
    }

    public static ComparisonResult compare(Stat orderBy, Order order, String... tags) {
        if (singleton == null) {
            singleton = get();
        }
        return singleton.compareInternal(orderBy, order, tags);
    }

    public static ComparisonResult compare(Stat orderBy, String... tags) {
        return compare(orderBy, Order.ASCENDING, tags);
    }

    public static void setDefaultPrecision(Precision precision) {
        if (precision != null) {
            DEFAULT_PRECISION = precision;
        }
    }
    
    public static void setDefaultOutputLog(PrintStream printStream) {
        if (printStream != null) {
            DEFAULT_OUTPUT_LOG = printStream;
        }
    }

    public static void setEnabledStats(Stat... stats) {
        STATISTICS = new HashSet<>();
        Collections.addAll(STATISTICS, stats);
    }

    private TimeMeter beginInternal(String tag) {
        starts.put(tag, System.nanoTime());
        return this;
    }

    private Measure endInternal(String tag) {
        long end = System.nanoTime();
        long start = starts.get(tag);
        long taken = end - start;

        Measure benchmark = benchmarks.get(tag);
        if (benchmark == null) {
            benchmark = new Measure(tag, taken);
            benchmarks.put(tag, benchmark);
        } else {
            benchmark.add(taken);
        }
        return benchmark;
    }

    private Result analyzeInternal(String tag) {
    	Measure benchmark = benchmarks.get(tag);
        return benchmark.result();
    }

    private TimeMeter clearInternal() {
        starts.clear();
        benchmarks.clear();
        return this;
    }

    private ComparisonResult compareInternal(Stat orderBy, Order order, String... tags) {
        ArrayList<Result> results = new ArrayList<>();

        if (tags.length == 0) {
            for (String tag : benchmarks.keySet()) {
                results.add(analyzeInternal(tag));
            }
        } else {
            for (String tag : tags) {
                results.add(analyzeInternal(tag));
            }
        }

        return new ComparisonResult(orderBy, order, results);
    }

    static void log(String message) {
//        Log.d(TAG, message);
    	DEFAULT_OUTPUT_LOG.println(message);
    }

    static void log(String type, String tag, String result) {
        String log = String.format("%s [%s] --> %s", type, tag, result);
//        Log.d(TAG, log);
        DEFAULT_OUTPUT_LOG.println(log);
    }

    static void logMany(String tag, List<Pair<String, String>> stats) {
        StringBuilder sb = new StringBuilder("[" + tag + "] --> ");

        for (Pair<String, String> stat : stats) {
            sb.append(String.format("%s[%s], ", stat.getFirst(), stat.getSecond()));
        }
        sb.delete(sb.length() - 2, sb.length() - 1);

//        Log.d(TAG, sb.toString());
        DEFAULT_OUTPUT_LOG.println(sb.toString());
    }

    public static enum Stat {
        AVERAGE, RANGE, STANDARD_DEVIATION
    }

    public static enum Order {
        ASCENDING, DESCENDING
    }

    public static enum Precision {
        NANO("ns", 1),
        MICRO("µs", 1000),
        MILLI("ms", 1000000),
        SECOND("s", 1000000000);

    	public final String unit;
        public final long divider;

        Precision(String unit, long divider) {
            this.unit = unit;
            this.divider = divider;
        }
    }
}