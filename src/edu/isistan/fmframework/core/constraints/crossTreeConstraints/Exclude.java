package edu.isistan.fmframework.core.constraints.crossTreeConstraints;

import java.util.Set;

import edu.isistan.fmframework.core.constraints.Clause;

public class Exclude extends BinaryConstraint {

	public Exclude(int leftFeature, int rightFeature) {
		super(leftFeature, rightFeature);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause clause = new Clause(new int[] { this.leftFeature, this.rightFeature }, new boolean[] { false, false });
		clauses.add(clause);
	}

	@Override
	public String toString() {
		return "\"" + this.leftFeature + "\"-/>\"" + this.rightFeature + "\"";
	}

	@Override
	public Exclude clone() {
		Exclude clone = new Exclude(this.leftFeature, this.rightFeature);
		return clone;
	}
}
