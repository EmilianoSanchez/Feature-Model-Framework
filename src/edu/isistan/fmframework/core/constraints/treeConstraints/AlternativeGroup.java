package edu.isistan.fmframework.core.constraints.treeConstraints;

import java.util.Set;

import edu.isistan.fmframework.core.constraints.Clause;

public class AlternativeGroup extends TreeConstraint {

	public AlternativeGroup(int parent, int... children) {
		super(1, 1, parent, children);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {

		for (int i = 0; i < this.children.length; i++)
			for (int j = i + 1; j < this.children.length; j++) {
				Clause xorClause = new Clause(new int[] { this.children[i], this.children[j] },
						new boolean[] { false, false });
				clauses.add(xorClause);
			}

		OrGroup orGroup = new OrGroup(parent, children);
		orGroup.generateClauses(clauses);
	}

	@Override
	public TreeConstraintType getType() {
		return TreeConstraintType.ALTERNATIVE_GROUP;
	}

}
