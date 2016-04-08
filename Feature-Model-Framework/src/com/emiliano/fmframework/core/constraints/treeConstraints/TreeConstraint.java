package com.emiliano.fmframework.core.constraints.treeConstraints;

import com.emiliano.fmframework.core.constraints.Constraint;

public abstract class TreeConstraint extends Constraint {

	protected String parent;
	protected String[] children;

	public TreeConstraint(String parent, String... children) {
		this.parent = parent;
		this.children = children;
	}

	public String getParent() {
		return this.parent;
	}

	public String[] getChildren() {
		return this.children;
	}

}
