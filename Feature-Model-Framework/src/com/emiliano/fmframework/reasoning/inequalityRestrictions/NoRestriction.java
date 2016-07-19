package com.emiliano.fmframework.reasoning.inequalityRestrictions;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.constraints.IConstraint;

public class NoRestriction implements IConstraint {

	@Override
	public boolean isSatisfied(Configuration configuration) {
		return true;
	}

}
