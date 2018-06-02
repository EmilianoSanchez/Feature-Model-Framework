package edu.isistan.fmframework.optimization.optCSA.heuristicFunctions;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.optimization.Problem;

public interface HeuristicFunction<P extends Problem<?, ?>> {

	public void setup(P instance);

	public double evaluate(Configuration configuration);

}
