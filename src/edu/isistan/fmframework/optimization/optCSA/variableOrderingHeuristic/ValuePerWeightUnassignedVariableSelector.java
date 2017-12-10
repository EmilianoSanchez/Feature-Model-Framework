package edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.globalConstraints.InequalityRestriction;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;

public class ValuePerWeightUnassignedVariableSelector implements UnassignedVariableSelector<Problem<?, ?>> {

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

		BasicProblem binstance = (BasicProblem) instance;

		if (binstance.model != null) {
			variableorder = new int[binstance.model.getNumFeatures()];
			if (binstance.globalConstraints != null && binstance.globalConstraints.length > 0) {
				SortedMap<Double, Integer> orderM = new TreeMap<>();
				for (int i = 0; i < binstance.model.getNumFeatures(); i++) {
					double value = binstance.objectiveFunctions[0].attributes[i];
					double relativeweight = 0.0;
					for (int a = 0; a < binstance.globalConstraints.length; a++) {
						relativeweight += binstance.globalConstraints[a].attributes[i]
								/ binstance.globalConstraints[a].restrictionLimit;
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

}
