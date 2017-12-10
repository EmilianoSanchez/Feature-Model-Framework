package edu.isistan.fmframework.core.constraints;

import java.util.Set;

public class AssignedValue extends ClauseBasedConstraint {

	private int featureId;
	private boolean isSelected;

	public AssignedValue(int featureId, boolean isSelected) {
		this.featureId = featureId;
		this.isSelected = isSelected;
	}

	public int getFeatureId() {
		return featureId;
	}

	public void setFeature(int featureId) {
		this.featureId = featureId;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		Clause aux = new Clause(featureId, isSelected);
		clauses.add(aux);
	}

	@Override
	public String toString() {
		if (this.isSelected)
			return "\"" + this.featureId + "\"=SELECTED";
		else
			return "\"" + this.featureId + "\"=DESELECTED";
	}

	@Override
	public AssignedValue clone() {
		AssignedValue clone = new AssignedValue(this.featureId, this.isSelected);
		return clone;
	}
}
