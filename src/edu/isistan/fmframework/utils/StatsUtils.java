package edu.isistan.fmframework.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

public class StatsUtils {


    public static double averageL(List<Long> times) {
        int size = times.size();
        long total = 0;
        for (int i = 0; i < size; i++) {
            total += times.get(i);
        }
        return total / (double) size;
    }

    public static long[] rangeL(List<Long> times) {
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

    public static double deviationL(List<Long> times) {
        double mean = averageL(times);
        double temp = 0;

        int size = times.size();
        for (int i = 0; i < size; i++) {
            long t = times.get(i);
            temp += (mean - t) * (mean - t);
        }
        return Math.sqrt(temp / size);
    }
    
    public static double averageD(List<Double> times) {
        int size = times.size();
        double total = 0;
        for (int i = 0; i < size; i++) {
            total += times.get(i);
        }
        return total / (double) size;
    }

    public static double[] rangeD(List<Double> times) {
        int size = times.size();
        double min = times.get(0);
        double max = times.get(0);
        for (int i = 0; i < size; i++) {
        	double time = times.get(i);
            if (time > max) {
                max = time;
            } else if (time < min) {
                min = time;
            }
        }

        return new double[]{min, max};
    }

    public static double deviationD(List<Double> times) {
        double mean = averageD(times);
        double temp = 0;

        int size = times.size();
        for (int i = 0; i < size; i++) {
            double t = times.get(i);
            temp += (mean - t) * (mean - t);
        }
        return Math.sqrt(temp / size);
    }

    public static double averageB(List<Boolean> times) {
        int size = times.size();
        int total = 0;
        for (int i = 0; i < size; i++) {
        	if(times.get(i))
        		total += 1;
        }
        return ((double) total) / ((double) size);
    }
}