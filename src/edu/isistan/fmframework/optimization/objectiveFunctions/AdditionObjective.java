package edu.isistan.fmframework.optimization.objectiveFunctions;

import java.util.Arrays;

import edu.isistan.fmframework.optimization.aggregateFunctions.AdditionFunction;

public class AdditionObjective extends AggregateObjective{

	public AdditionObjective(double[] attributes) {
		super(attributes,new AdditionFunction());
	}

	@Override
	public String toString() {
		return "+("+Arrays.toString(this.attributes)+")";
	}
}
