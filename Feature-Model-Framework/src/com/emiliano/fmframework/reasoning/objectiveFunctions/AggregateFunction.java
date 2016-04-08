package com.emiliano.fmframework.reasoning.objectiveFunctions;

import java.util.Map.Entry;
import java.util.Set;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureState;

public abstract class AggregateFunction implements ObjectiveFunction {

	private String attributeSelected;
	private String attributeDeselected;

	public AggregateFunction(String attributeSelected, String attributeDeselected) {
		this.attributeSelected = attributeSelected;
		this.attributeDeselected = attributeDeselected;
	};

	public AggregateFunction(String attributeSelected) {
		this(attributeSelected, null);
	};

	@Override
	public double evaluate(Configuration conf) {
		double result = this.getNeutralElement();
		for (Entry<String, FeatureState> featureState : (Set<Entry<String, FeatureState>>) conf.getFeatureStates()) {
			Feature feature = conf.getFeature(featureState.getKey());
			switch (featureState.getValue()) {
			case SELECTED:
				Double valueSelected = (Double) feature.getAttribute(attributeSelected);
				if (valueSelected == null)
					result = this.operate(result, this.getNeutralElement());
				else
					result = this.operate(result, valueSelected);
				break;
			case DESELECTED:
				Double valueDeselected = (Double) feature.getAttribute(attributeDeselected);
				if (valueDeselected == null)
					result = this.operate(result, this.getNeutralElement());
				else
					result = this.operate(result, valueDeselected);
				break;
			default:
			}
		}
		return result;
	}

	protected abstract double getNeutralElement();

	protected abstract double operate(double operand1, double operand2);
}
