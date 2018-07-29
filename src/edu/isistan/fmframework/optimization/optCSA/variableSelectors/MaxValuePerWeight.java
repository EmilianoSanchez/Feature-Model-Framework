package edu.isistan.fmframework.optimization.optCSA.variableSelectors;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.globalConstraints.InequalityRestriction;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;

public class MaxValuePerWeight implements VariableSelector<Problem<?, ?>> {

	int[] variableorder;

	@Override
	public int selectUnassignedVariable(Configuration conf) {
		for (int i = 0; i < variableorder.length; i++) {
			if (conf.getFeatureState(variableorder[i]) == FeatureState.UNSELECTED)
				return variableorder[i];
		}
		return NO_UNASSIGNED_VARIABLES;
	}

	@Override
	public void setup(Problem<?, ?> instance) {

		if (instance.model != null) {
			variableorder = new int[instance.model.getNumFeatures()];
			if (instanceHasLinearObjectiveAndLinearInequalityRestrictions(instance)) {
				SortedMap<Double, Integer> orderM = new TreeMap<>();
				AdditionObjective objective = ((AdditionObjective) instance.objectiveFunctions[0]);
				for (int i = 0; i < instance.model.getNumFeatures(); i++) {
					double value = objective.attributes[i];
					double relativeweight = 0.0;
					for (int a = 0; a < instance.globalConstraints.length; a++) {
						InequalityRestriction restriction = (InequalityRestriction) instance.globalConstraints[a];
						relativeweight += restriction.attributes[i] / restriction.restrictionLimit;
					}
					value /= relativeweight;
					orderM.put(value, i);
				}
				Iterator<Integer> iterator = orderM.values().iterator();
				for (int i = variableorder.length - 1; i >= 0; i--)
					variableorder[i] = iterator.next();
			} else {
				for (int i = 0; i < variableorder.length; i++)
					variableorder[i] = i;
			}
		}
	}

	private boolean instanceHasLinearObjectiveAndLinearInequalityRestrictions(Problem<?, ?> instance) {
		if(!(instance.objectiveFunctions[0] instanceof AdditionObjective)) {
			return false;
		}
		if (instance.globalConstraints != null && instance.globalConstraints.length > 0) {
			for(int i=0;i<instance.globalConstraints.length;i++) {
				if(!(instance.globalConstraints[i] instanceof InequalityRestriction)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
