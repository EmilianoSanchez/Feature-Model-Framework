package com.emiliano.fmframework.reasoning.aggregateFunctions;

public class MaximumFunction implements AggregateFunction {

	@Override
	public double getNeutralElement() {
		return Double.MIN_VALUE;
	}

	@Override
	public double operate(double operand1, double operand2) {
		return Math.max(operand1, operand2);
	}

	@Override
	public double inverseOperator(double operand1, double operand2) {
		throw new UnsupportedOperationException("Inverse operator not supported");
	}
}
