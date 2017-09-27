package com.emiliano.fmframework.core.constraints;

import java.util.Set;

public class AssignedValue extends Constraint {

	private String feature;
	private boolean selected;

	public AssignedValue(String feature, boolean selected) {
		this.feature = feature;
		this.selected = selected;
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause aux = new Clause();
		aux.literals.put(feature, selected);
		clauses.add(aux);
	}

	@Override
	public String toString() {
		if(this.selected)
			return '"' + this.feature + "\"=SELECTED";
		else
			return '"' + this.feature + "\"=DESELECTED";
	}
}
