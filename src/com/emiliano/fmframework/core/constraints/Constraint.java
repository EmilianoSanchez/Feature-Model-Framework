package com.emiliano.fmframework.core.constraints;

import java.util.HashSet;
import java.util.Set;

import com.emiliano.fmframework.core.Configuration;

public abstract class Constraint implements IConstraint {
	private Set<Clause> clauses;

	protected abstract void generateClauses(Set<Clause> clauses);

	public Set<String> getInvolvedFeatures() {
		Set<String> names = new HashSet<String>();
		for (Clause clause : getClauses()) {
			for (String name : clause.literals.keySet()) {
				names.add(name);
			}
		}
		return names;
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

}
