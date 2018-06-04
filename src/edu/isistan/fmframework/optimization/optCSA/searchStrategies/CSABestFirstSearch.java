package edu.isistan.fmframework.optimization.optCSA.searchStrategies;

import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.optCSA.containers.PriorityQueue;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicFunction;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.VariableSelector;

public class CSABestFirstSearch extends CSABacktracking {

	public CSABestFirstSearch(String name) {
		super(name, new PriorityQueue<State>());
	}

	public CSABestFirstSearch(String name, HeuristicFunction heuristic) {
		super(name, new PriorityQueue<State>(), heuristic);
	}

	public CSABestFirstSearch(String name, VariableSelector unassignedVariableSelector) {
		super(name, new PriorityQueue<State>(), unassignedVariableSelector);
	}

	public CSABestFirstSearch(String name, HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		super(name, new PriorityQueue<State>(), heuristic, unassignedVariableSelector);
	}

}
