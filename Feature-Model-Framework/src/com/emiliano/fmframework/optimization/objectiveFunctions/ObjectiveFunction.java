package com.emiliano.fmframework.optimization.objectiveFunctions;

import com.emiliano.fmframework.core.Configuration;

public interface ObjectiveFunction {
	public double evaluate(Configuration configuration);
}
