package edu.isistan.fmframework.optimization.objectiveFunctions;

import edu.isistan.fmframework.optimization.aggregateFunctions.MaximumFunction;

public class MaximumObjective extends AggregateObjective {

	public MaximumObjective(double[] attributes) {
		super(attributes, new MaximumFunction());
	}

}