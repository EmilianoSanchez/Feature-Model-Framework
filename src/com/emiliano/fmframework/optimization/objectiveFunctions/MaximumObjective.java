package com.emiliano.fmframework.optimization.objectiveFunctions;

import com.emiliano.fmframework.optimization.aggregateFunctions.AdditionFunction;
import com.emiliano.fmframework.optimization.aggregateFunctions.MaximumFunction;

public class MaximumObjective extends AggregateObjective{

	public MaximumObjective(String attributeSelected) {
		super(attributeSelected,new MaximumFunction());
	}
	public MaximumObjective(String attributeSelected, String attributeDeselected) {
		super(attributeSelected,attributeDeselected,new MaximumFunction());
	}

}