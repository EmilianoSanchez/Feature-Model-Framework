package com.emiliano.fmframework.reasoning.objectiveFunctions;

public class AdditionFunction extends AggregateFunction {

	public AdditionFunction(String attributeSelected, String attributeDeselected) {
		super(attributeSelected, attributeDeselected);
	};

	public AdditionFunction(String attributeSelected) {
		super(attributeSelected);
	};

	@Override
	protected double getNeutralElement() {
		return 0;
	}

	@Override
	protected double operate(double operand1, double operand2) {
		return operand1 + operand2;
	}

}
