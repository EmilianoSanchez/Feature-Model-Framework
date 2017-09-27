package com.emiliano.fmframework.optimization;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public interface ConfigurationSelectionAlgorithm {
	Configuration selectConfiguration(ConfigurationSelectionInstance instance);
}
