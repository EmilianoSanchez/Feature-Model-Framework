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
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.UnassignedVariableSelector;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.UnassignedVariableSelectors;

public abstract class CSAalgorithm implements Algorithm<Problem<?, ?>> {

	public static enum Strategy {
		BT, BestFS, BandB
	}

	public static CSAalgorithm build(Strategy strategy) {
		return build(strategy, Heuristics.heuristicB, UnassignedVariableSelectors.staticMostConstrained);
	}

	public static CSAalgorithm build(Strategy strategy, HeuristicFunction heuristic) {
		return build(strategy, heuristic, UnassignedVariableSelectors.staticMostConstrained);
	}
	
	public static CSAalgorithm build(Strategy strategy, UnassignedVariableSelector<Problem<?, ?>> unassignedVariableSelector){
		return build(strategy, Heuristics.heuristicB, unassignedVariableSelector);
	}

	public static CSAalgorithm build(Strategy strategy, HeuristicFunction heuristic,
			UnassignedVariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		switch (strategy) {
		case BT:
			return new CSABacktracking(heuristic, unassignedVariableSelector);
		case BestFS:
			return new CSABestFirstSearch(heuristic, unassignedVariableSelector);
		case BandB:
			return new CSABranchAndBound(heuristic, unassignedVariableSelector);
		}
		return null;
	}

	protected CSAalgorithm(Container<State> container) {
		this(container, Heuristics.heuristicB);
	}

	protected CSAalgorithm(Container<State> container, HeuristicFunction heuristic) {
		this(container, heuristic, UnassignedVariableSelectors.topDownUnassignedVariableSelector);
	}
	
	public CSAalgorithm(Container<State> container, UnassignedVariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		this(container, Heuristics.heuristicB, unassignedVariableSelector);
	}

	protected CSAalgorithm(Container<State> container, HeuristicFunction heuristic,
			UnassignedVariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		this(container, heuristic, unassignedVariableSelector, ConstraintPropagators.clauseBasedConstraintPropagator);
	}

	protected CSAalgorithm(Container<State> container, HeuristicFunction heuristic,
			UnassignedVariableSelector<Problem<?, ?>> unassignedVariableSelector, ConstraintPropagator cpropagator) {
		this.open = container;
		this.cpropagator = cpropagator;
		this.heuristic = heuristic;
		this.unassignedVariableSelector = unassignedVariableSelector;
	}

	public void setOpenContainer(Container<State> open) {
		this.open = open;
	}

	public void setHeuristic(HeuristicFunction heuristic) {
		this.heuristic = heuristic;
	}

	public void setUnassignedVariableSelector(UnassignedVariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		this.unassignedVariableSelector = unassignedVariableSelector;
	}

	public void setConstraintPropagator(ConstraintPropagator cpropagator) {
		this.cpropagator = cpropagator;
	}

	protected Container<State> open;
	protected HeuristicFunction heuristic;
	protected UnassignedVariableSelector<Problem<?, ?>> unassignedVariableSelector;
	protected ConstraintPropagator cpropagator;

	@Override
	public String getName() {
		return "CSA" + "-" + open.getClass().getSimpleName() + "-" + cpropagator.getClass().getSimpleName() + "-"
				+ heuristic.getClass().getSimpleName() + "-" + unassignedVariableSelector.getClass().getSimpleName();
	}
}