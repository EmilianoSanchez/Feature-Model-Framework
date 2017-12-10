package edu.isistan.fmframework.core.constraints.crossTreeConstraints;

import edu.isistan.fmframework.core.constraints.ClauseBasedConstraint;

public abstract class BinaryConstraint extends ClauseBasedConstraint {

	protected int leftFeature, rightFeature;

	public BinaryConstraint(int leftFeature, int rightFeature) {
		this.leftFeature = leftFeature;
		this.rightFeature = rightFeature;
	}

	public int getLeftFeature() {
		return leftFeature;
	}

	public void setLeftFeature(int leftFeature) {
		this.leftFeature = leftFeature;
	}

	public int getRightFeature() {
		return rightFeature;
	}

	public void setRightFeature(int rightFeature) {
		this.rightFeature = rightFeature;
	}

}
