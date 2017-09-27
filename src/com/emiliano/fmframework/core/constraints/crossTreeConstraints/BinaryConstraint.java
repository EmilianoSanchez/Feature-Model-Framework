package com.emiliano.fmframework.core.constraints.crossTreeConstraints;

import com.emiliano.fmframework.core.constraints.Constraint;

public abstract class BinaryConstraint extends Constraint {

	protected String leftFeature, rightFeature;

	public BinaryConstraint(String leftFeature, String rightFeature) {
		this.leftFeature = leftFeature;
		this.rightFeature = rightFeature;
	}

}
