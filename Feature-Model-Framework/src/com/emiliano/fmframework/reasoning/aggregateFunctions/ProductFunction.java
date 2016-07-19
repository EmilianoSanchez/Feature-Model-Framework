package com.emiliano.fmframework.reasoning.aggregateFunctions;

public class ProductFunction implements AggregateFunction {

	@Override
	public double getNeutralElement() {
		return 1.0;
	}

	@Override
	public double operate(double operand1, double operand2) {
		return operand1 * operand2;
	}

	@Override
	public double inverseOperator(double operand1, double operand2) {
		return operand1/operand2;
	}

}
