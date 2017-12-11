package edu.isistan.fmframework.optimization.optCSA.searchStrategies;

import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.optCSA.containers.PriorityQueue;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicFunction;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.VariableSelector;

public class CSABestFirstSearch extends CSABacktracking {

	public CSABestFirstSearch() {
		super(new PriorityQueue<State>());
	}

	public CSABestFirstSearch(HeuristicFunction heuristic) {
		super(new PriorityQueue<State>(), heuristic);
	}
	
	public CSABestFirstSearch(VariableSelector unassignedVariableSelector) {
		super(new PriorityQueue<State>(), unassignedVariableSelector);
	}

	public CSABestFirstSearch(HeuristicFunction heuristic,
			VariableSelector<Problem<?, ?>> unassignedVariableSelector) {
		super(new PriorityQueue<State>(), heuristic, unassignedVariableSelector);
	}
	
	public String getName() {
		return "BestFS"+super.getName();
	};
}
