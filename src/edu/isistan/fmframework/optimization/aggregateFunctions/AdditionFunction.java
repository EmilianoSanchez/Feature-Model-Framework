package edu.isistan.fmframework.optimization.aggregateFunctions;

public class AdditionFunction implements AggregateFunction {

	@Override
	public double getNeutralElement() {
		return 0;
	}

	@Override
	public double operate(double operand1, double operand2) {
		return operand1 + operand2;
	}

	@Override
	public double inverseOperator(double operand1, double operand2) {
		return operand1-operand2;
	}
}
