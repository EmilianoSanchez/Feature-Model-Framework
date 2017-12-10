package edu.isistan.fmframework.core.constraints.crossTreeConstraints;

import java.util.Set;

import edu.isistan.fmframework.core.constraints.Clause;

public class MutualImplication extends BinaryConstraint {

	public MutualImplication(int leftFeature, int rightFeature) {
		super(leftFeature, rightFeature);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause clause1 = new Clause(new int[]{this.leftFeature,this.rightFeature},new boolean[]{false,true});
		Clause clause2 = new Clause(new int[]{this.leftFeature,this.rightFeature},new boolean[]{true,false});

		clauses.add(clause1);
		clauses.add(clause2);
	}

	@Override
	public String toString() {
		return "\"" + this.leftFeature + "\"<->\"" + this.rightFeature + "\"";
	}
	
	@Override
	public MutualImplication clone() {
		MutualImplication clone = new MutualImplication(this.leftFeature,this.rightFeature);
		return clone;
	}
}
