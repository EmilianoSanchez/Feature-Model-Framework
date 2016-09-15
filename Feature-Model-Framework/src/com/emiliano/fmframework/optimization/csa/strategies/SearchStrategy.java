package com.emiliano.fmframework.optimization.csa.strategies;

import com.emiliano.fmframework.optimization.csa.State;

public enum SearchStrategy {
	DepthFirstSearch(new Stack<State>()), 
	BreadthFirstSearch(new Queue<State>()), 
	GreedyBestFirstSearch(new PriorityStack<State>()), 
	BestFirstSearchStar(new PriorityQueue<State>());
	
	public Container<State> container;
	SearchStrategy(Container<State> container){
		this.container=container;
	}
}