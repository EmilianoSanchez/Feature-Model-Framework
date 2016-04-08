package com.emiliano.fmframework.reasoning.objectiveFunctions;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.emiliano.fmframework.core.Configuration;

public class LinearWeightedFunction implements ObjectiveFunction {

	public static final double MINIMIZE = 1.0;// !If a positive value is used as
												// weight (w), the function is
												// minimized
	public static final double MAXIMIZE = -1.0;// !If a negative value is used
												// as weight (w), the function
												// is maximized

	private List<SimpleEntry<ObjectiveFunction, Double>> terms;

	public LinearWeightedFunction() {
		this.terms = new LinkedList<AbstractMap.SimpleEntry<ObjectiveFunction, Double>>();
	}

	public void addTerm(ObjectiveFunction cof, double weight) {
		this.terms.add(new AbstractMap.SimpleEntry<ObjectiveFunction, Double>(cof, weight));
	}

	public void addTerm(ObjectiveFunction cof) {
		this.addTerm(cof, MINIMIZE);
	}

	public void clear() {
		this.terms.clear();
	};

	@Override
	public double evaluate(Configuration conf) {
		double result = 0.0;
		for (AbstractMap.SimpleEntry<ObjectiveFunction, Double> cof : terms)
			result += cof.getKey().evaluate(conf) * cof.getValue();
		return result;
	}

}
