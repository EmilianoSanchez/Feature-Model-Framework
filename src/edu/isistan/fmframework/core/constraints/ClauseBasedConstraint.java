package edu.isistan.fmframework.core.constraints;

import java.util.HashSet;
import java.util.Set;

import edu.isistan.fmframework.core.Configuration;

public abstract class ClauseBasedConstraint implements Constraint {
	private Set<Clause> clauses;

	protected abstract void generateClauses(Set<Clause> clauses);

	public Set<Integer> getInvolvedFeatures() {
		Set<Integer> ids = new HashSet<Integer>();
		for (Clause clause : getClauses()) {
			for (int id : clause.literalIds) {
				ids.add(id);
			}
		}
		return ids;
	}

	public Set<Clause> getClauses() {
		if (this.clauses == null) {
			this.clauses = new HashSet<Clause>();
			generateClauses(this.clauses);
		}
		return this.clauses;
	}

	@Override
	public boolean isSatisfied(Configuration conf) {
		for (Clause clause : getClauses())
			if (!clause.isSatisfied(conf))
				return false;
		return true;
	};

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Clause clause : getClauses()) {
			builder.append(clause.toString());
			builder.append("&");
		}
		return builder.toString();
	}

}
