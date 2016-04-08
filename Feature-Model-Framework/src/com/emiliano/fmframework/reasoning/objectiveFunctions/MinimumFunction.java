package com.emiliano.fmframework.reasoning.objectiveFunctions;

public class MinimumFunction extends AggregateFunction {

	public MinimumFunction(String attributeSelected, String attributeDeselected) {
		super(attributeSelected, attributeDeselected);
	};

	public MinimumFunction(String attributeSelected) {
		super(attributeSelected);
	};

	@Override
	protected double getNeutralElement() {
		return Double.MAX_VALUE;
	}

	@Override
	protected double operate(double operand1, double operand2) {
		return Math.min(operand1, operand2);
	}
}
