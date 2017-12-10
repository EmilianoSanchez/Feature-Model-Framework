package edu.isistan.fmframework.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparisonResult {

    List<Result> results;
    TimeMeter.Stat orderBy;
    TimeMeter.Order order;

    public ComparisonResult(TimeMeter.Stat orderBy, TimeMeter.Order order, List<Result> results) {
        this.orderBy = orderBy;
        this.order = order;
        this.results = results;

        sort();
    }

    private void sort() {
        Collections.sort(results, new Comparator<Result>() {
            @Override
            public int compare(Result lhs, Result rhs) {

                double statOne = lhs.getStat(orderBy);
                double statTwo = rhs.getStat(orderBy);
                if (order == TimeMeter.Order.ASCENDING) {
                    return Double.compare(statOne, statTwo);
                } else {
                    return Double.compare(statTwo, statOne);
                }
            }
        });
    }

    public void log() {
    	TimeMeter.log("------");
    	TimeMeter.log("Comparison Results: Number of Benchmarks[" + results.size() + "], Ordered By[" + orderBy.name() + " " + order.name() + "]");
    	TimeMeter.log("------");
        for (Result result : results) {
            result.log();
        }
        TimeMeter.log("------");
    }
}