package com.emiliano.fmframework.optimization.objectiveFunctions;

import com.emiliano.fmframework.optimization.aggregateFunctions.AdditionFunction;

public class AdditionObjective extends AggregateObjective{

	public AdditionObjective(String attributeSelected) {
		super(attributeSelected,new AdditionFunction());
	}
	public AdditionObjective(String attributeSelected, String attributeDeselected) {
		super(attributeSelected,attributeDeselected,new AdditionFunction());
	}

}
