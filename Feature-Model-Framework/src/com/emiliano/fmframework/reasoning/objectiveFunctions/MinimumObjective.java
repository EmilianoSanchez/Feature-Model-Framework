package com.emiliano.fmframework.reasoning.objectiveFunctions;

import com.emiliano.fmframework.reasoning.aggregateFunctions.MinimumFunction;

public class MinimumObjective extends AggregateObjective{

	public MinimumObjective(String attributeSelected) {
		super(attributeSelected,new MinimumFunction());
	}
	public MinimumObjective(String attributeSelected, String attributeDeselected) {
		super(attributeSelected,attributeDeselected,new MinimumFunction());
	}

}