package com.emiliano.fmframework.optimization.inequalityRestrictions;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.optimization.objectiveFunctions.AdditionObjective;
import com.emiliano.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class SingleInequalityRestriction implements IConstraint {

	private ObjectiveFunction function;
	private double limit;

	public SingleInequalityRestriction(String attributeSelected, String attributeDeselected, double limit) {
		this(new AdditionObjective(attributeSelected, attributeDeselected), limit);
	};

	public SingleInequalityRestriction(ObjectiveFunction function, double limit) {
		this.function = function;
		this.limit = limit;
	};

	@Override
	public boolean isSatisfied(Configuration conf) {
		return this.function.evaluate(conf) <= this.limit;
	}

}
