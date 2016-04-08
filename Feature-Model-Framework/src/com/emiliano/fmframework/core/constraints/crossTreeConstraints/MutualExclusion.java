package com.emiliano.fmframework.core.constraints.crossTreeConstraints;

import java.util.Set;

import com.emiliano.fmframework.core.constraints.Clause;

public class MutualExclusion extends BinaryConstraint {

	public MutualExclusion(String leftFeature, String rightFeature) {
		super(leftFeature, rightFeature);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause clause1 = new Clause();
		clause1.literals.put(this.leftFeature, false);
		clause1.literals.put(this.rightFeature, false);

		Clause clause2 = new Clause();
		clause2.literals.put(this.leftFeature, true);
		clause2.literals.put(this.rightFeature, true);

		clauses.add(clause1);
		clauses.add(clause2);
	}

	@Override
	public String toString() {
		return '"' + this.leftFeature + "\"<-/>\"" + this.rightFeature + '"';
	}
}
