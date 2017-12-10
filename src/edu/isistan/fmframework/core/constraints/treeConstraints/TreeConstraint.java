package edu.isistan.fmframework.core.constraints.treeConstraints;


import edu.isistan.fmframework.core.constraints.ClauseBasedConstraint;

public abstract class TreeConstraint extends ClauseBasedConstraint{

	protected int parent;
	protected int[] children;
	protected int minCardinality;
	protected int maxCardinality;

	public TreeConstraint(int minCardinality,int maxCardinality,int parent, int... children) {
		this.minCardinality = minCardinality;
		if(maxCardinality==-1)
			this.maxCardinality = children.length;
		else
			this.maxCardinality = maxCardinality;
		this.parent = parent;
		this.children = children;
	}
	
	public int getMinCardinality() {
		return minCardinality;
	}

	public int getMaxCardinality() {
		return maxCardinality;
	}

	public int getParent() {
		return this.parent;
	}

	public int[] getChildren() {
		return this.children;
	}
	
	public abstract TreeConstraintType getType();
	
	public static enum TreeConstraintType{
		MANDATORY, OPTIONAL, ALTERNATIVE_GROUP, OR_GROUP, CARDINALITY_GROUP
	}
}
