package com.emiliano.fmframework.optimization.objectiveFunctions;

import java.util.Set;
import java.util.Map.Entry;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureState;
import com.emiliano.fmframework.optimization.aggregateFunctions.AggregateFunction;

public class AggregateObjective implements ObjectiveFunction{

	private String attributeSelected;
	private String attributeDeselected;
	private AggregateFunction function;

	public AggregateObjective(String attributeSelected, String attributeDeselected,AggregateFunction function) {
		this.attributeSelected = attributeSelected;
		this.attributeDeselected = attributeDeselected;
		this.function=function;
	};

	public AggregateObjective(String attributeSelected,AggregateFunction function) {
		this(attributeSelected, null,function);
	};

	@Override
	public double evaluate(Configuration conf) {
		double result = this.function.getNeutralElement();
		for (Entry<String, FeatureState> featureState : (Set<Entry<String, FeatureState>>) conf.getFeatureStates()) {
			Feature feature = conf.getFeature(featureState.getKey());
			switch (featureState.getValue()) {
			case SELECTED:
				Double valueSelected = (Double) feature.getAttribute(attributeSelected);
				if (valueSelected == null)
					result = this.function.operate(result, this.function.getNeutralElement());
				else
					result = this.function.operate(result, valueSelected);
				break;
			case DESELECTED:
				Double valueDeselected = (Double) feature.getAttribute(attributeDeselected);
				if (valueDeselected == null)
					result = this.function.operate(result, this.function.getNeutralElement());
				else
					result = this.function.operate(result, valueDeselected);
				break;
			default:
			}
		}
		return result;
	}

	public String getAttributeSelected() {
		return attributeSelected;
	}

	public void setAttributeSelected(String attributeSelected) {
		this.attributeSelected = attributeSelected;
	}

	public String getAttributeDeselected() {
		return attributeDeselected;
	}

	public void setAttributeDeselected(String attributeDeselected) {
		this.attributeDeselected = attributeDeselected;
	}

	public AggregateFunction getFunction() {
		return function;
	}

	public void setFunction(AggregateFunction function) {
		this.function = function;
	}
}
