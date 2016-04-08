package com.emiliano.fmframework.reasoning.csa;

import java.util.List;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.FeatureState;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.reasoning.ConfOperations;
import com.emiliano.fmframework.reasoning.csa.inequalityRestrictions.NoRestriction;
import com.emiliano.fmframework.reasoning.csa.strategies.Container;
import com.emiliano.fmframework.reasoning.csa.strategies.PriorityQueue;
import com.emiliano.fmframework.reasoning.csa.strategies.PriorityStack;
import com.emiliano.fmframework.reasoning.csa.strategies.Queue;
import com.emiliano.fmframework.reasoning.csa.strategies.Stack;
import com.emiliano.fmframework.reasoning.csa.variableOrderingHeuristic.FirstUnassignedVariableSelector;
import com.emiliano.fmframework.reasoning.csa.variableOrderingHeuristic.UnassignedVariableSelector;
import com.emiliano.fmframework.reasoning.objectiveFunctions.ObjectiveFunction;

public class ConfigurationSelectionAlgorithm {

	public enum SearchStrategy {
		DepthFirstSearch, BreadthFirstSearch, GreedyBestFirstSearch, BestFirstSearchStar
	}

	public static enum HeuristicFunction {
		HeuristicA, HeuristicB
	}

	public static enum UnselectedVariableSelector {
		First
	}

	public ConfigurationSelectionAlgorithm(SearchStrategy strategy, ObjectiveFunction heuristic) {
		this(strategy, heuristic, new FirstUnassignedVariableSelector(), new NoRestriction());
	}

	public ConfigurationSelectionAlgorithm(SearchStrategy strategy, ObjectiveFunction heuristic,
			UnassignedVariableSelector unassignedVariableSelector) {
		this(strategy, heuristic, unassignedVariableSelector, new NoRestriction());
	}

	public ConfigurationSelectionAlgorithm(SearchStrategy strategy, ObjectiveFunction heuristic,
			IConstraint restriction) {
		this(strategy, heuristic, new FirstUnassignedVariableSelector(), restriction);
	}

	public ConfigurationSelectionAlgorithm(SearchStrategy strategy, ObjectiveFunction heuristic,
			UnassignedVariableSelector unassignedVariableSelector, IConstraint restriction) {
		switch (strategy) {
		case DepthFirstSearch:
			this.open = new Stack<State>();
			break;
		case BreadthFirstSearch:
			this.open = new Queue<State>();
			break;
		case BestFirstSearchStar:
			this.open = new PriorityQueue<State>();
			break;
		case GreedyBestFirstSearch:
			this.open = new PriorityStack<State>();
		}
		this.heuristic = heuristic;
		this.unassignedVariableSelector = unassignedVariableSelector;
		this.restriction = restriction;
	}

	public ConfigurationSelectionAlgorithm(ObjectiveFunction function, IConstraint constraint) {
		this(SearchStrategy.GreedyBestFirstSearch, function, constraint);
	}

	protected static class State implements Comparable<State> {
		public Configuration conf;
		public Double value;

		public State(Configuration conf, Double value) {
			this.conf = conf;
			this.value = value;
		}

		@Override
		public int compareTo(State other) {
			if (this.value < other.value)
				return -1;
			else if (this.value > other.value)
				return 1;
			else
				return 0;
		}
	}

	protected Container<State> open;
	protected ObjectiveFunction heuristic;
	protected UnassignedVariableSelector unassignedVariableSelector;
	protected IConstraint restriction;

	public boolean searchSolution(Configuration initial, Configuration solution) {
		int openStates = 1;
		this.unassignedVariableSelector.setup(initial);
		while (!this.open.isEmpty())
			this.open.pop();
		if (this.restriction.isSatisfied(initial))
			this.open.push(new State(initial, 0.0));
		else
			return false;
		while (!this.open.isEmpty()) {
			Configuration current = this.open.pop().conf;

			String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
			if (unassignedVariable == null) {
				solution.copy(current);
				// System.out.println("Open states "+openStates);
				return true;
			} else {
				Configuration succesor1 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
						&& this.restriction.isSatisfied(succesor1)) {
					this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1)));
					openStates++;
				}
				Configuration succesor2 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
						&& this.restriction.isSatisfied(succesor2)) {
					this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2)));
					openStates++;
				}
			}
		}
		// System.out.println("Open states "+openStates);
		return false;
	}

	public boolean searchSolution_BandB(Configuration initial, Configuration solution) {
		int openStates = 1;
		this.unassignedVariableSelector.setup(initial);
		while (!this.open.isEmpty())
			this.open.pop();
		if (this.restriction.isSatisfied(initial))
			this.open.push(new State(initial, this.heuristic.evaluate(initial)));
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
							&& this.restriction.isSatisfied(succesor1)) {
						this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1)));
						openStates++;
					}
					Configuration succesor2 = (Configuration) current.conf.clone();
					if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
							&& this.restriction.isSatisfied(succesor2)) {
						this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2)));
						openStates++;
					}
				}
			}
		}
		System.out.println("Open states " + openStates);
		return upperBound != Double.MAX_VALUE;
	}

	public void searchAllSolutions(Configuration initial, List<Configuration> solutions) {
		this.unassignedVariableSelector.setup(initial);
		while (!this.open.isEmpty())
			this.open.pop();
		if (this.restriction.isSatisfied(initial))
			this.open.push(new State(initial, 0.0));
		else
			return;
		while (!this.open.isEmpty()) {
			Configuration current = this.open.pop().conf;

			String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
			if (unassignedVariable == null) {
				solutions.add(current);
			} else {
				Configuration succesor1 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
						&& this.restriction.isSatisfied(succesor1)) {
					this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1)));
				}
				Configuration succesor2 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
						&& this.restriction.isSatisfied(succesor2)) {
					this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2)));
				}
			}
		}
	}

	public int searchAllSolutions(Configuration initial) {
		this.unassignedVariableSelector.setup(initial);
		while (!this.open.isEmpty())
			this.open.pop();
		if (this.restriction.isSatisfied(initial))
			this.open.push(new State(initial, 0.0));
		else
			return 0;
		int amount = 0;
		while (!this.open.isEmpty()) {
			Configuration current = this.open.pop().conf;

			String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
			if (unassignedVariable == null) {
				amount++;
			} else {
				Configuration succesor1 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
						&& this.restriction.isSatisfied(succesor1)) {
					this.open.push(new State(succesor1, this.heuristic.evaluate(succesor1)));
				}
				Configuration succesor2 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
						&& this.restriction.isSatisfied(succesor2)) {
					this.open.push(new State(succesor2, this.heuristic.evaluate(succesor2)));
				}
			}
		}
		return amount;
	}

	public static int searchAllSolutionsS(Configuration initial) {
		Container<State> open = new Stack<State>();
		UnassignedVariableSelector unassignedVariableSelector = new FirstUnassignedVariableSelector();

		open.push(new State(initial, 0.0));
		int amount = 0;

		while (!open.isEmpty()) {
			Configuration current = open.pop().conf;

			String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
			if (unassignedVariable == null) {
				amount++;
			} else {
				Configuration succesor1 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)) {
					open.push(new State(succesor1, 0.0));
				}
				Configuration succesor2 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)) {
					open.push(new State(succesor2, 0.0));
				}
			}
		}
		return amount;
	}

	public static void searchAllSolutionsS(Configuration initial, List<Configuration> solutions) {
		Container<State> open = new Stack<State>();
		UnassignedVariableSelector unassignedVariableSelector = new FirstUnassignedVariableSelector();

		open.push(new State(initial, 0.0));

		while (!open.isEmpty()) {
			Configuration current = open.pop().conf;

			String unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
			if (unassignedVariable == null) {
				solutions.add(current);
			} else {
				Configuration succesor1 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)) {
					open.push(new State(succesor1, 0.0));
				}
				Configuration succesor2 = (Configuration) current.clone();
				if (ConfOperations.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)) {
					open.push(new State(succesor2, 0.0));
				}
			}
		}
	}
}
