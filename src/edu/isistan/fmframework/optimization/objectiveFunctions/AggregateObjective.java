package edu.isistan.fmframework.optimization.objectiveFunctions;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.optimization.aggregateFunctions.AggregateFunction;

public class AggregateObjective implements ObjectiveFunction{

	public double[] attributes;
	private AggregateFunction function;

	public AggregateObjective(double[] attributes,AggregateFunction function) {
		this.attributes=attributes;
		this.function=function;
	};

	@Override
	public double evaluate(Configuration conf) {
		double result = this.function.getNeutralElement();
		FeatureModel model=conf.getModel();
		for (int i=0;i<model.getNumFeatures();i++) {
			switch (conf.getFeatureState(i)) {
			case SELECTED:
				double valueSelected = attributes[i];
				result = this.function.operate(result, valueSelected);
				break;
			default:
			}
		}
		return result;
	}

	public AggregateFunction getFunction() {
		return function;
	}

	public void setFunction(AggregateFunction function) {
		this.function = function;
	}
}
