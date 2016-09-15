package com.emiliano.fmframework.optimization.inequalityRestrictions;

import java.util.LinkedList;
import java.util.List;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.constraints.IConstraint;

public class MultipleInequalityRestriction implements IConstraint {

	private List<IConstraint> restrictions;

	public MultipleInequalityRestriction() {
		this.restrictions = new LinkedList<IConstraint>();
	}

	public void addRestriction(IConstraint restriction) {
		this.restrictions.add(restriction);
	}

	@Override
	public boolean isSatisfied(Configuration conf) {
		for (IConstraint restriction : this.restrictions)
			if (!restriction.isSatisfied(conf))
				return false;
		return true;
	}

}
