package edu.isistan.fmframework.optimization;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.globalConstraints.InequalityRestriction;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;

public class BasicProblem extends Problem<InequalityRestriction, AdditionObjective> {

	public BasicProblem() {
		super();
	}

	public BasicProblem(FeatureModel model) {
		super(model);
	}

	public BasicProblem(FeatureModel model, AdditionObjective objective) {
		super(model);
		this.objectiveFunctions = new AdditionObjective[] { objective };
	}
}
