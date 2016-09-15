package com.emiliano.fmframework.optimization.csa.heuristicFunctions;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public interface HeuristicFunction {

	public double evaluate(Configuration configuration,ObjectiveFunction objective);
	
}
