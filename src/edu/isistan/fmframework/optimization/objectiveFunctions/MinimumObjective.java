package edu.isistan.fmframework.optimization.objectiveFunctions;

import edu.isistan.fmframework.optimization.aggregateFunctions.MinimumFunction;

public class MinimumObjective extends AggregateObjective {

	public MinimumObjective(double[] attributes) {
		super(attributes, new MinimumFunction());
	}

}