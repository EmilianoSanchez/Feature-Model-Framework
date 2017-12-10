package edu.isistan.fmframework.core.constraints.treeConstraints;

import java.util.Set;

import edu.isistan.fmframework.core.constraints.Clause;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint.TreeConstraintType;

public class OptionalFeature extends TreeConstraint {

	public OptionalFeature(int parent, int child) {
		super(0,1,parent, child);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause clause = new Clause(new int[]{this.children[0],this.parent},new boolean[]{false,true});
		clauses.add(clause);

	}

	public int getChild() {
		return this.children[0];
	}

	@Override
	public TreeConstraintType getType() {
		return TreeConstraintType.OPTIONAL;
	}
}
