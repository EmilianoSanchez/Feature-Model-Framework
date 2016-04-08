package com.emiliano.fmframework.reasoning.csa.inequalityRestrictions;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.constraints.IConstraint;

public class NoRestriction implements IConstraint {

	@Override
	public boolean isSatisfied(Configuration configuration) {
		return true;
	}

}
