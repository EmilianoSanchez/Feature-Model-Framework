package edu.isistan.fmframework.optimization.optCSA.variableSelectors;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.optimization.Problem;

public interface VariableSelector<P extends Problem<?,?>> {

	public int NO_UNASSIGNED_VARIABLES = -1;

	public int selectUnassignedVariable(Configuration conf);
	
	public void setup(P instance);
}
