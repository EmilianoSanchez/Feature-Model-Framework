package com.emiliano.fmframework.reasoning.objectiveFunctions;

import com.emiliano.fmframework.core.Configuration;

public interface MultiObjectiveFunction {
	public double[] evaluate(Configuration configuration);
}
