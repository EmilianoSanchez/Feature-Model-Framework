package edu.isistan.fmframework.optimization.optCSA.variableSelectors;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ClauseBasedConstraintPropagator;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagators;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicB;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicFunction;

public class MaxHeuristicValue implements VariableSelector<Problem<?, ?>> {

	private HeuristicFunction<Problem<?, ?>> heuristic;
	private ClauseBasedConstraintPropagator cpropagator;

	public MaxHeuristicValue(){
		this(new HeuristicB());
	}
	public MaxHeuristicValue(HeuristicFunction<Problem<?, ?>> heuristic){
		this.heuristic = heuristic;
		this.cpropagator = new ClauseBasedConstraintPropagator();
	}
	
	@Override
	public void setup(Problem<?, ?> instance) {
		java.util.PriorityQueue<Map.Entry<Integer, Double>> unassignedVariables = new java.util.PriorityQueue<Map.Entry<Integer, Double>>(
				instance.model.getNumFeatures(), new Comparator<Map.Entry<Integer, Double>>() {
					@Override
					public int compare(Entry<Integer, Double> e1, Entry<Integer, Double> e2) {
						if (e1.getValue() < e2.getValue())
							return 1;
						else if (e1.getValue() > e2.getValue())
							return -1;
						else
							return 0;
					}
				});

		Configuration conf = ConstraintPropagators.clauseBasedConstraintPropagator
				.getPartialConfiguration(instance.model);
		this.heuristic.setup(instance);
		variableorder = new int[instance.model.getNumFeatures()];
		int vorder = 0;
		for (int i = 0; i < conf.length(); i++) {
			if (conf.getFeatureState(i) == FeatureState.UNSELECTED) {
				Configuration confAux1 = (Configuration) conf.clone();
				cpropagator.assignFeature(confAux1, i, FeatureState.SELECTED);
				Configuration confAux2 = (Configuration) conf.clone();
				cpropagator.assignFeature(confAux2, i, FeatureState.DESELECTED);
				double max = Math.max(this.heuristic.evaluate(confAux1),
						this.heuristic.evaluate(confAux2));
				unassignedVariables.add(new AbstractMap.SimpleEntry<Integer, Double>(i, max));
			} else {
				variableorder[vorder] = i;
				vorder++;
			}
		}

		while (!unassignedVariables.isEmpty()) {
			variableorder[vorder] = unassignedVariables.remove().getKey();
			vorder++;
		}
	}

	int[] variableorder;

	@Override
	public int selectUnassignedVariable(Configuration conf) {
		for (int i = 0; i < variableorder.length; i++) {
			if (conf.getFeatureState(variableorder[i]) == FeatureState.UNSELECTED)
				return variableorder[i];
		}
		return NO_UNASSIGNED_VARIABLES;
	}
}