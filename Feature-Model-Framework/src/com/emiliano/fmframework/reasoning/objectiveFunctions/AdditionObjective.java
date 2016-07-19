package com.emiliano.fmframework.reasoning.objectiveFunctions;

import com.emiliano.fmframework.reasoning.aggregateFunctions.AdditionFunction;

public class AdditionObjective extends AggregateObjective{

	public AdditionObjective(String attributeSelected) {
		super(attributeSelected,new AdditionFunction());
	}
	public AdditionObjective(String attributeSelected, String attributeDeselected) {
		super(attributeSelected,attributeDeselected,new AdditionFunction());
	}

}
