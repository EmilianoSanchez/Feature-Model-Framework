package edu.isistan.fmframework.optimization.optCSA.searchStrategies;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagator;
import edu.isistan.fmframework.optimization.optCSA.containers.Container;
import edu.isistan.fmframework.optimization.optCSA.containers.PriorityStack;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicFunction;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.VariableSelector;

public class CSABacktracking extends CSAalgorithm {

	@Override
	public Configuration selectConfiguration(Problem<?, ?> instance) {
//		int openStates = 1;
		Configuration initial=this.cpropagator.getPartialConfiguration(instance.model);
		if(initial!=null && instance.satisfyGlobalConstraints(initial)){
			this.unassignedVariableSelector.setup(instance);
			this.heuristic.setup(instance);
			this.open.clear();
			this.open.push(new State(initial, 0.0));
			
			while (!this.open.isEmpty()) {
				Configuration current = this.open.pop().conf;
				int unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
				if (unassignedVariable == VariableSelector.NO_UNASSIGNED_VARIABLES) {
					// System.out.println("Open states "+openStates);
					return current;
				} else {
					Configuration succesor1 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
							&& instance.satisfyGlobalConstraints(succesor1)) {
						this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1)));
	//					openStates++;
					}
					Configuration succesor2 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
							&& instance.satisfyGlobalConstraints(succesor2)) {
						this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2)));
	//					openStates++;
					}
				}
			}
		}
		// System.out.println("Open states "+openStates);
		return null;
	}
	
	public CSABacktracking() {
		super(new PriorityStack<State>());
	}

	public CSABacktracking(HeuristicFunction heuristic) {
		super(new PriorityStack<State>(), heuristic);
	}
	
	public CSABacktracking(VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		super(new PriorityStack<State>(), unassignedVariableSelector);
	}
	

	public CSABacktracking(HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		super(new PriorityStack<State>(), heuristic, unassignedVariableSelector);
	}
	
	protected CSABacktracking(Container<State> container) {
		super(container);
	}
	
	protected CSABacktracking(Container<State> container,HeuristicFunction heuristic) {
		super(container, heuristic);
	}
	
	public CSABacktracking(Container<State> container,VariableSelector unassignedVariableSelector) {
		super(container, unassignedVariableSelector);
	}

	protected CSABacktracking(Container<State> container,HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		super(container, heuristic ,unassignedVariableSelector);
	}
	
	protected CSABacktracking(Container<State> container,HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector, ConstraintPropagator constraintPropagator) {
		super(container, heuristic,unassignedVariableSelector,constraintPropagator);
	}
	
	public String getName() {
		return "BT"+super.getName();
	};
}
