package com.emiliano.fmframework.optimization.csa.variableOrderingHeuristic;

import com.emiliano.fmframework.core.Configuration;

public interface UnassignedVariableSelector {

	public int NO_UNASSIGNED_VARIABLES = -1;

	public String selectUnassignedVariable(Configuration conf);

	public void setup(Configuration conf);
}
