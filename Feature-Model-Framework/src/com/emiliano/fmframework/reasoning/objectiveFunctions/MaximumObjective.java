package com.emiliano.fmframework.reasoning.objectiveFunctions;

import com.emiliano.fmframework.reasoning.aggregateFunctions.AdditionFunction;
import com.emiliano.fmframework.reasoning.aggregateFunctions.MaximumFunction;

public class MaximumObjective extends AggregateObjective{

	public MaximumObjective(String attributeSelected) {
		super(attributeSelected,new MaximumFunction());
	}
	public MaximumObjective(String attributeSelected, String attributeDeselected) {
		super(attributeSelected,attributeDeselected,new MaximumFunction());
	}

}