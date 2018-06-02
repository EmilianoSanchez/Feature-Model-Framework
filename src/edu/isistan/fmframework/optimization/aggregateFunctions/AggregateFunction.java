package edu.isistan.fmframework.optimization.aggregateFunctions;

public interface AggregateFunction {
	public double operate(double operand1,double operand2);
	public double getNeutralElement();
	public double inverseOperator(double operand1,double operand2);
	
	public AggregateFunctionType getType();
	
	public static enum AggregateFunctionType{
		ADDITION, PRODUCT, MAXIMUM, MINIMUM
	}
}
