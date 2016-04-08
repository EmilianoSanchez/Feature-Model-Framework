package com.emiliano.fmframework.core.constraints.treeConstraints;

import java.util.Set;

import com.emiliano.fmframework.core.constraints.Clause;

public class OrGroup extends TreeConstraint {

	public OrGroup(String parent, String... children) {
		super(parent, children);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause clause1 = new Clause();
		clause1.literals.put(this.parent, false);
		for (int i = 0; i < this.children.length; i++) {
			Clause clauseN = new Clause();
			clauseN.literals.put(this.parent, true);
			clauseN.literals.put(this.children[i], false);
			clause1.literals.put(this.children[i], true);
			clauses.add(clauseN);
		}
		clauses.add(clause1);
	}

}
