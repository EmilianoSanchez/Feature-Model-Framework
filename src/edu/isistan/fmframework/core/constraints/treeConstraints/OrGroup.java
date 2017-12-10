package edu.isistan.fmframework.core.constraints.treeConstraints;

import java.util.Set;

import edu.isistan.fmframework.core.constraints.Clause;

public class OrGroup extends TreeConstraint implements Cloneable{

	public OrGroup(int parent, int... children) {
		super(1,children.length,parent, children);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause clause1 = new Clause();
		clause1.add(this.parent, false);
		for (int i = 0; i < this.children.length; i++) {
			Clause clauseN = new Clause();
			clauseN.add(this.parent, true);
			clauseN.add(this.children[i], false);
			clause1.add(this.children[i], true);
			clauses.add(clauseN);
		}
		clauses.add(clause1);
	}

	@Override
	public TreeConstraintType getType() {
		return TreeConstraintType.OR_GROUP;
	}
	
}
