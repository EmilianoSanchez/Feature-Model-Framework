package com.emiliano.fmframework.core.constraints.crossTreeConstraints;

import java.util.Set;

import com.emiliano.fmframework.core.constraints.Clause;

public class Exclude extends BinaryConstraint {

	public Exclude(String leftFeature, String rightFeature) {
		super(leftFeature, rightFeature);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause clause = new Clause();
		clause.literals.put(this.leftFeature, false);
		clause.literals.put(this.rightFeature, false);
		clauses.add(clause);
	}

	@Override
	public String toString() {
		return '"' + this.leftFeature + "\"-/>\"" + this.rightFeature + '"';
	}
}
