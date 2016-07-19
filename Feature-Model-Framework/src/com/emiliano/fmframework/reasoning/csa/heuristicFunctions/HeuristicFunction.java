package com.emiliano.fmframework.reasoning.csa.heuristicFunctions;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.reasoning.objectiveFunctions.ObjectiveFunction;

public interface HeuristicFunction {

	public double evaluate(Configuration configuration,ObjectiveFunction objective);
	
}
