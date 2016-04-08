package com.emiliano.fmframework.reasoning.csa.inequalityRestrictions;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.reasoning.objectiveFunctions.AdditionFunction;
import com.emiliano.fmframework.reasoning.objectiveFunctions.ObjectiveFunction;

public class SingleInequalityRestriction implements IConstraint {

	private ObjectiveFunction function;
	private double limit;

	public SingleInequalityRestriction(String attributeSelected, String attributeDeselected, double limit) {
		this(new AdditionFunction(attributeSelected, attributeDeselected), limit);
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
