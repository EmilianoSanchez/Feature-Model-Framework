package edu.isistan.fmframework.optimization.aggregateFunctions;

public class MinimumFunction implements AggregateFunction {

	@Override
	public double getNeutralElement() {
		return Double.MAX_VALUE;
	}

	@Override
	public double operate(double operand1, double operand2) {
		return Math.min(operand1, operand2);
	}

	@Override
	public double inverseOperator(double operand1, double operand2) {
		throw new UnsupportedOperationException("Inverse operator not supported");
	}
}
