package edu.isistan.fmframework.optimization.optCSA;

import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagator;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagators;
import edu.isistan.fmframework.optimization.optCSA.containers.Container;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicFunction;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.Heuristics;
import edu.isistan.fmframework.optimization.optCSA.searchStrategies.CSABacktracking;
import edu.isistan.fmframework.optimization.optCSA.searchStrategies.CSABestFirstSearch;
import edu.isistan.fmframework.optimization.optCSA.searchStrategies.CSABranchAndBound;
import edu.isistan.fmframework.optimization.optCSA.searchStrategies.State;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.VariableSelector;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.VariableSelectors;

public abstract class CSAalgorithm implements Algorithm<Problem<?, ?>> {

	public static enum Strategy {
		BT, BestFS, BandB
	}

	public static CSAalgorithm build(String name, Strategy strategy) {
		return build(name, strategy, Heuristics.heuristicTC, VariableSelectors.mostConstrainedFeatureVariableSelector);
	}

	public static CSAalgorithm build(String name, Strategy strategy, HeuristicFunction heuristic) {
		return build(name, strategy, heuristic, VariableSelectors.mostConstrainedFeatureVariableSelector);
	}

	public static CSAalgorithm build(String name, Strategy strategy,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		return build(name, strategy, Heuristics.heuristicTC, unassignedVariableSelector);
	}

	public static CSAalgorithm build(String name, Strategy strategy, HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		CSAalgorithm result = null;
		switch (strategy) {
		case BT:
			result = new CSABacktracking(name, heuristic, unassignedVariableSelector);
			break;
		case BestFS:
			result = new CSABestFirstSearch(name, heuristic, unassignedVariableSelector);
			break;
		case BandB:
			result = new CSABranchAndBound(name, heuristic, unassignedVariableSelector);
		}
		return result;
	}
	
	public static CSAalgorithm build(String name, Strategy strategy, HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector, ConstraintPropagator cpropagator) {
		CSAalgorithm result = null;
		switch (strategy) {
		case BT:
			result = new CSABacktracking(name, heuristic, unassignedVariableSelector,cpropagator);
			break;
		case BestFS:
			result = new CSABestFirstSearch(name, heuristic, unassignedVariableSelector,cpropagator);
			break;
		case BandB:
			result = new CSABranchAndBound(name, heuristic, unassignedVariableSelector,cpropagator);
		}
		return result;
	}

	protected CSAalgorithm(String name, Container<State> container) {
		this(name, container, Heuristics.heuristicTC);
	}

	protected CSAalgorithm(String name, Container<State> container, HeuristicFunction heuristic) {
		this(name, container, heuristic, VariableSelectors.topDownVariableSelector);
	}

	public CSAalgorithm(String name, Container<State> container,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		this(name, container, Heuristics.heuristicTC, unassignedVariableSelector);
	}

	protected CSAalgorithm(String name, Container<State> container, HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		this(name, container, heuristic, unassignedVariableSelector,
				ConstraintPropagators.clauseBasedConstraintPropagator);
	}

	protected CSAalgorithm(String name, Container<State> container, HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector, ConstraintPropagator cpropagator) {
		this.open = container;
		this.cpropagator = cpropagator;
		this.heuristic = heuristic;
		this.unassignedVariableSelector = unassignedVariableSelector;
		this.name = name;
	}

	public void setOpenContainer(Container<State> open) {
		this.open = open;
	}

	public Container<State> getOpenContainer() {
		return this.open;
	}

	public void setHeuristic(HeuristicFunction heuristic) {
		this.heuristic = heuristic;
	}

	public void setUnassignedVariableSelector(VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		this.unassignedVariableSelector = unassignedVariableSelector;
	}

	public void setConstraintPropagator(ConstraintPropagator cpropagator) {
		this.cpropagator = cpropagator;
	}

	protected Container<State> open;
	protected HeuristicFunction heuristic;
	protected VariableSelector<Problem<?, ?>> unassignedVariableSelector;
	protected ConstraintPropagator cpropagator;
	protected String name;

	@Override
	public String getName() {
		return name;
	}
}