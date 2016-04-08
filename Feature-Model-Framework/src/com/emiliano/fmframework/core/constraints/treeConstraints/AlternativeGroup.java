package com.emiliano.fmframework.core.constraints.treeConstraints;

import java.util.Set;

import com.emiliano.fmframework.core.constraints.Clause;

public class AlternativeGroup extends TreeConstraint {

	public AlternativeGroup(String parent, String... children) {
		super(parent, children);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {

		for (int i = 0; i < this.children.length; i++)
			for (int j = i + 1; j < this.children.length; j++) {
				Clause xorClause = new Clause();
				xorClause.literals.put(this.children[i], false);
				xorClause.literals.put(this.children[j], false);
				clauses.add(xorClause);
			}

		OrGroup orGroup = new OrGroup(parent, children);
		orGroup.generateClauses(clauses);
	}

}
