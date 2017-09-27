package com.emiliano.fmframework.optimization.csa;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.FeatureState;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.operations.ConfOperations;
import com.emiliano.fmframework.optimization.ConfigurationSelectionAlgorithm;
import com.emiliano.fmframework.optimization.ConfigurationSelectionInstance;
import com.emiliano.fmframework.optimization.csa.heuristicFunctions.HeuristicA;
import com.emiliano.fmframework.optimization.csa.heuristicFunctions.HeuristicFunction;
import com.emiliano.fmframework.optimization.csa.strategies.Container;
import com.emiliano.fmframework.optimization.csa.strategies.SearchStrategy;
import com.emiliano.fmframework.optimization.csa.variableOrderingHeuristic.FirstUnassignedVariableSelector;
import com.emiliano.fmframework.optimization.csa.variableOrderingHeuristic.UnassignedVariableSelector;
import com.emiliano.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class BranchAndBoundCSA implements ConfigurationSelectionAlgorithm{

	public BranchAndBoundCSA() {
		this(SearchStrategy.GreedyBestFirstSearch, new HeuristicA(), new FirstUnassignedVariableSelector());
	}
	
	public BranchAndBoundCSA(SearchStrategy strategy) {
		this(strategy, new HeuristicA(), new FirstUnassignedVariableSelector());
	}
	
	public BranchAndBoundCSA(SearchStrategy strategy, HeuristicFunction heuristic) {
		this(strategy, heuristic, new FirstUnassignedVariableSelector());
	}

	public BranchAndBoundCSA(SearchStrategy strategy, HeuristicFunction heuristic,
			UnassignedVariableSelector unassignedVariableSelector) {
		this.open=strategy.container;
		this.heuristic = heuristic;
		this.unassignedVariableSelector = unassignedVariableSelector;
	}

	protected Container<State> open;
	protected HeuristicFunction heuristic;
	protected UnassignedVariableSelector unassignedVariableSelector;

	@Override
	public Configuration selectConfiguration(ConfigurationSelectionInstance instance) {

//		int openStates = 1;
		this.unassignedVariableSelector.setup(instance.initialConfiguration);
		while (!this.open.isEmpty())
			this.open.pop();
		if (instance.restriction.isSatisfied(instance.initialConfiguration))
			this.open.push(new State(instance.initialConfiguration, this.heuristic.evaluate(instance.initialConfiguration,instance.objective)));
		else
			return null;
		
		Configuration solution=null;
		double upperBound = Double.MAX_VALUE;
		while (!this.open.isEmpty()) {
			State current = this.open.pop();
			if (current.value < upperBound) {
				String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current.conf);
				if (unassignedVariable == null) {
					solution=current.conf;
					upperBound = current.value;
				} else {
					Configuration succesor1 = (Configuration) current.conf.clone();
					if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
							&& instance.restriction.isSatisfied(succesor1)) {
						this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1,instance.objective)));
//						openStates++;
					}
					Configuration succesor2 = (Configuration) current.conf.clone();
					if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
							&& instance.restriction.isSatisfied(succesor2)) {
						this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2,instance.objective)));
//						openStates++;
					}
				}
			}
		}
//		System.out.println("Open states " + openStates);
//		return upperBound != Double.MAX_VALUE;
		return solution;
	}

	public boolean searchSolution_BandB(Configuration initial, Configuration solution, ObjectiveFunction objective,
			IConstraint restriction) {
//		int openStates = 1;
		this.unassignedVariableSelector.setup(initial);
		while (!this.open.isEmpty())
			this.open.pop();
		if (restriction==null || restriction.isSatisfied(initial))
			this.open.push(new State(initial, this.heuristic.evaluate(initial,objective)));
		else
			return false;
		double upperBound = Double.MAX_VALUE;
		while (!this.open.isEmpty()) {
			State current = this.open.pop();
			if (current.value < upperBound) {
				String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current.conf);
				if (unassignedVariable == null) {
					solution.copy(current.conf);
					upperBound = current.value;
				} else {
					Configuration succesor1 = (Configuration) current.conf.clone();
					if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
							&& (restriction==null || restriction.isSatisfied(succesor1))) {
						this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1,objective)));
//						openStates++;
					}
					Configuration succesor2 = (Configuration) current.conf.clone();
					if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
							&& (restriction==null || restriction.isSatisfied(succesor2))) {
						this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2,objective)));
//						openStates++;
					}
				}
			}
		}
//		System.out.println("Open states " + openStates);
		return upperBound != Double.MAX_VALUE;
	}
}
