package com.emiliano.fmframework.optimization.objectiveFunctions;

import com.emiliano.fmframework.optimization.aggregateFunctions.MinimumFunction;

public class MinimumObjective extends AggregateObjective{

	public MinimumObjective(String attributeSelected) {
		super(attributeSelected,new MinimumFunction());
	}
	public MinimumObjective(String attributeSelected, String attributeDeselected) {
		super(attributeSelected,attributeDeselected,new MinimumFunction());
	}

}