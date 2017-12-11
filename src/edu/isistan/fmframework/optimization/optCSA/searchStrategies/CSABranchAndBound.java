package edu.isistan.fmframework.optimization.optCSA.searchStrategies;

import java.util.Stack;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagator;
import edu.isistan.fmframework.optimization.optCSA.containers.Container;
import edu.isistan.fmframework.optimization.optCSA.containers.PriorityStack;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicFunction;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.VariableSelector;

public class CSABranchAndBound extends CSAalgorithm {

	@Override
	public Configuration selectConfiguration(Problem<?, ?> instance) {
		
		Configuration initial=this.cpropagator.getPartialConfiguration(instance.model);
		if(initial!=null && instance.satisfyGlobalConstraints(initial)){
			this.unassignedVariableSelector.setup(instance);
			this.heuristic.setup(instance);
			this.open.clear();
			this.open.push(new State(initial,
						this.heuristic.evaluate(initial)));

			Configuration solution = null;
			double upperBound = Double.MAX_VALUE;
			while (!this.open.isEmpty()) {
				State current = this.open.pop();
				if (current.value < upperBound) {
					int unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current.conf);
					if (unassignedVariable == VariableSelector.NO_UNASSIGNED_VARIABLES) {
						solution = current.conf;
						upperBound = current.value;
					} else {
						Configuration succesor1 = (Configuration) current.conf.clone();
						if (cpropagator.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
								&& (instance.satisfyGlobalConstraints(succesor1))) {
							this.open
									.push(new State(succesor1, this.heuristic.evaluate(succesor1)));
							// openStates++;
						}
						Configuration succesor2 = (Configuration) current.conf.clone();
						if (cpropagator.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
								&& (instance.satisfyGlobalConstraints(succesor2))) {
							this.open
									.push(new State(succesor2, this.heuristic.evaluate(succesor2)));
							// openStates++;
						}
					}
				}
			}
			// System.out.println("Open states " + openStates);
			// return upperBound != Double.MAX_VALUE;
			return solution;
		}
		return null;
	}
	
	public CSABranchAndBound() {
		super(new PriorityStack<State>());
	}

	public CSABranchAndBound(HeuristicFunction heuristic) {
		super(new PriorityStack<State>(), heuristic);
	}

	public CSABranchAndBound(HeuristicFunction heuristic,
			VariableSelector unassignedVariableSelector) {
		super(new PriorityStack<State>(), heuristic, unassignedVariableSelector);
	}
	
	public CSABranchAndBound(VariableSelector unassignedVariableSelector) {
		super(new PriorityStack<State>(), unassignedVariableSelector);
	}
	
	protected CSABranchAndBound(Container<State> container) {
		super(container);
	}
	
	protected CSABranchAndBound(Container<State> container,HeuristicFunction heuristic) {
		super(container, heuristic);
	}

	protected CSABranchAndBound(Container<State> container,HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		super(container, heuristic ,unassignedVariableSelector);
	}
	
	protected CSABranchAndBound(Container<State> container,HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector, ConstraintPropagator constraintPropagator) {
		super(container, heuristic,unassignedVariableSelector,constraintPropagator);
	}
	
	public String getName() {
		return "BandB"+super.getName();
	};
}