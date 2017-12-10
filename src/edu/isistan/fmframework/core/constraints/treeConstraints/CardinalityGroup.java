package edu.isistan.fmframework.core.constraints.treeConstraints;

import java.util.Set;

import edu.isistan.fmframework.core.constraints.Clause;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint.TreeConstraintType;

public class CardinalityGroup extends TreeConstraint  {
	
	public CardinalityGroup(int minCardinality,int maxCardinality,int parent, int... children) {
		super(minCardinality,maxCardinality,parent, children);
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		for(int child:this.children){
			Clause clause = new Clause(new int[]{child,this.parent},new boolean[]{false,true});
			clauses.add(clause);
		}
		// TODO Auto-generated method stub
	}

	@Override
	public TreeConstraintType getType() {
		return TreeConstraintType.CARDINALITY_GROUP;
	}
}
