package com.emiliano.fmframework.reasoning.objectiveFunctions;

public class ProductFunction extends AggregateFunction {

	public ProductFunction(String attributeSelected, String attributeDeselected) {
		super(attributeSelected, attributeDeselected);
	};

	public ProductFunction(String attributeSelected) {
		super(attributeSelected);
	};

	@Override
	protected double getNeutralElement() {
		return 1.0;
	}

	@Override
	protected double operate(double operand1, double operand2) {
		return operand1 * operand2;
	}
}
