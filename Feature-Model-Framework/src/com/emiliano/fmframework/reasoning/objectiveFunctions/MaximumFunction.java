package com.emiliano.fmframework.reasoning.objectiveFunctions;

public class MaximumFunction extends AggregateFunction {

	public MaximumFunction(String attributeSelected, String attributeDeselected) {
		super(attributeSelected, attributeDeselected);
	};

	public MaximumFunction(String attributeSelected) {
		super(attributeSelected);
	};

	@Override
	protected double getNeutralElement() {
		return Double.MIN_VALUE;
	}

	@Override
	protected double operate(double operand1, double operand2) {
		return Math.max(operand1, operand2);
	}
}
