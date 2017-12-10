package edu.isistan.fmframework.optimization.objectiveFunctions;

import edu.isistan.fmframework.optimization.aggregateFunctions.ProductFunction;

public class ProductObjective extends AggregateObjective {

	public ProductObjective(double[] attributes) {
		super(attributes, new ProductFunction());
	}

}