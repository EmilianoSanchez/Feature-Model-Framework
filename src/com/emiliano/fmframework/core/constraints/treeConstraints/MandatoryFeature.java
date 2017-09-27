package com.emiliano.fmframework.core.constraints.treeConstraints;

import java.util.Set;

import com.emiliano.fmframework.core.constraints.Clause;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint.TreeConstraintType;

public class MandatoryFeature extends TreeConstraint {

	public MandatoryFeature(String parent, String child) {
		super(parent, child);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause clause = new Clause();
		clause.literals.put(this.children[0], false);
		clause.literals.put(this.parent, true);
		clauses.add(clause);

		clause = new Clause();
		clause.literals.put(this.children[0], true);
		clause.literals.put(this.parent, false);
		clauses.add(clause);
	}

	@Override
	public TreeConstraintType getType() {
		return TreeConstraintType.MANDATORY;
	}
	
	public String getChild(){
		return this.children[0];
	}
}
