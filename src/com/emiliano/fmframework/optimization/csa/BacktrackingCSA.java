package com.emiliano.fmframework.optimization.csa;

import java.util.List;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.FeatureState;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.operations.ConfOperations;
import com.emiliano.fmframework.optimization.ConfigurationSelectionAlgorithm;
import com.emiliano.fmframework.optimization.ConfigurationSelectionInstance;
import com.emiliano.fmframework.optimization.csa.heuristicFunctions.HeuristicA;
import com.emiliano.fmframework.optimization.csa.heuristicFunctions.HeuristicFunction;
import com.emiliano.fmframework.optimization.csa.strategies.Container;
import com.emiliano.fmframework.optimization.csa.strategies.PriorityQueue;
import com.emiliano.fmframework.optimization.csa.strategies.PriorityStack;
import com.emiliano.fmframework.optimization.csa.strategies.Queue;
import com.emiliano.fmframework.optimization.csa.strategies.SearchStrategy;
import com.emiliano.fmframework.optimization.csa.strategies.Stack;
import com.emiliano.fmframework.optimization.csa.variableOrderingHeuristic.FirstUnassignedVariableSelector;
import com.emiliano.fmframework.optimization.csa.variableOrderingHeuristic.UnassignedVariableSelector;
import com.emiliano.fmframework.optimization.inequalityRestrictions.NoRestriction;
import com.emiliano.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class BacktrackingCSA implements ConfigurationSelectionAlgorithm{

	public BacktrackingCSA() {
		this(SearchStrategy.GreedyBestFirstSearch, new HeuristicA(), new FirstUnassignedVariableSelector());
	}
	
	public BacktrackingCSA(SearchStrategy strategy) {
		this(strategy, new HeuristicA(), new FirstUnassignedVariableSelector());
	}
	
	public BacktrackingCSA(SearchStrategy strategy, HeuristicFunction heuristic) {
		this(strategy, heuristic, new FirstUnassignedVariableSelector());
	}

	public BacktrackingCSA(SearchStrategy strategy, HeuristicFunction heuristic,
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
			this.open.push(new State(instance.initialConfiguration, 0.0));
		else
			return null;
		
		while (!this.open.isEmpty()) {
			Configuration current = this.open.pop().conf;
			String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
			if (unassignedVariable == null) {
				// System.out.println("Open states "+openStates);
				return current;
			} else {
				Configuration succesor1 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
						&& instance.restriction.isSatisfied(succesor1)) {
					this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1,instance.objective)));
//					openStates++;
				}
				Configuration succesor2 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
						&& instance.restriction.isSatisfied(succesor2)) {
					this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2,instance.objective)));
//					openStates++;
				}
			}
		}
		// System.out.println("Open states "+openStates);
		return null;
	}

	public int searchAllSolutions(Configuration initial, List<Configuration> solutions,ObjectiveFunction objective,IConstraint restriction) {
		this.unassignedVariableSelector.setup(initial);
		while (!this.open.isEmpty())
			this.open.pop();
		if (restriction==null || restriction.isSatisfied(initial))
			this.open.push(new State(initial, 0.0));
		else
			return 0;
		while (!this.open.isEmpty()) {
			Configuration current = this.open.pop().conf;

			String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
			if (unassignedVariable == null) {
				solutions.add(current);
			} else {
				Configuration succesor1 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
						&& (restriction==null || restriction.isSatisfied(succesor1))) {
					this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1,objective)));
				}
				Configuration succesor2 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
						&& (restriction==null || restriction.isSatisfied(succesor2))) {
					this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2,objective)));
				}
			}
		}
		return solutions.size();
	}

//	public int searchAllSolutions(Configuration initial) {
//		this.unassignedVariableSelector.setup(initial);
//		while (!this.open.isEmpty())
//			this.open.pop();
//		if (this.restriction.isSatisfied(initial))
//			this.open.push(new State(initial, 0.0));
//		else
//			return 0;
//		int amount = 0;
//		while (!this.open.isEmpty()) {
//			Configuration current = this.open.pop().conf;
//
//			String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
//			if (unassignedVariable == null) {
//				amount++;
//			} else {
//				Configuration succesor1 = (Configuration) current.clone();
//				if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
//						&& this.restriction.isSatisfied(succesor1)) {
//					this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1)));
//				}
//				Configuration succesor2 = (Configuration) current.clone();
//				if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
//						&& this.restriction.isSatisfied(succesor2)) {
//					this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2)));
//				}
//			}
//		}
//		return amount;
//	}

}
