package edu.isistan.fmframework.core.constraints.crossTreeConstraints;

import java.util.Set;

import edu.isistan.fmframework.core.constraints.Clause;

public class Imply extends BinaryConstraint {

	public Imply(int leftFeature, int rightFeature) {
		super(leftFeature, rightFeature);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause clause = new Clause(new int[]{this.leftFeature,this.rightFeature},new boolean[]{false,true});
		clauses.add(clause);
	}

	@Override
	public String toString() {
		return "\"" + this.leftFeature + "\"->\"" + this.rightFeature + "\"";
	}
	
	@Override
	public Imply clone() {
		Imply clone = new Imply(this.leftFeature,this.rightFeature);
		return clone;
	}
}
