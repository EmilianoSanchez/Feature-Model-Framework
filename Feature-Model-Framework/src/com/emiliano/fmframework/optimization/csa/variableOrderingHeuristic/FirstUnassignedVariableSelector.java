package com.emiliano.fmframework.optimization.csa.variableOrderingHeuristic;

import java.util.Set;
import java.util.Map.Entry;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.FeatureState;

public class FirstUnassignedVariableSelector implements UnassignedVariableSelector {

	@Override
	public String selectUnassignedVariable(Configuration conf) {
		Set<Entry<String, FeatureState>> states = conf.getFeatureStates();
		for (Entry<String, FeatureState> state : states)
			if (state.getValue() == FeatureState.UNSELECTED)
				return state.getKey();
		return null;
	}

	@Override
	public void setup(Configuration conf) {
	}

}
