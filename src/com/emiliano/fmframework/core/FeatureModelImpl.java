package com.emiliano.fmframework.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import com.emiliano.fmframework.core.constraints.Clause;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.core.constraints.Constraint;
import com.emiliano.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OptionalFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OrGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint;

public class FeatureModelImpl implements FeatureModel {

	private static class FMNode {
		Feature feature;
		TreeConstraint parentTree;
		Vector<TreeConstraint> childrenTrees;
		Vector<Constraint> involvedCrossTreeConstraints;
		Set<Clause> involvedClauses;

		FMNode(Feature feature) {
			this.feature = feature;
			this.childrenTrees = new Vector<TreeConstraint>();
			this.involvedCrossTreeConstraints = new Vector<Constraint>();
			this.involvedClauses=new HashSet<Clause>();
		}
	}

	private Map<String, FMNode> features;
	private Vector<TreeConstraint> treeConstraints;
	private Vector<Constraint> crossTreeConstraints;

	public FeatureModelImpl() {
		this.features = new HashMap<String, FMNode>();
		this.treeConstraints = new Vector<TreeConstraint>();
		this.crossTreeConstraints = new Vector<Constraint>();
	}

	public Feature getRoot() {
		for (Entry<String, FMNode> node : this.features.entrySet()) {
			if (node.getValue().parentTree == null) {
				return node.getValue().feature;
			}
		}
		return null;
	}

	@Override
	public Feature getFeature(String name) {
		FMNode node=this.features.get(name);
		if(node==null)
			return null;
		else
			return node.feature;
	}

	@Override
	public int getNumFeatures() {
		return features.size();
	}

	@Override
	public Feature addFeature(Feature feature) {
		FMNode previousFeature = this.features.put(
				feature.getName(), new FMNode(feature));
		if (previousFeature == null)
			return null;
		else
			return previousFeature.feature;
	}

	public Feature addFeature(String name) {
		return this.addFeature(new Feature(name));
	}

	@Override
	public boolean addCrossTreeConstraint(Constraint constraint) {
		
		for (String name : constraint.getInvolvedFeatures())
			if (this.features.get(name) == null)
				return false;

		for (String name : constraint.getInvolvedFeatures())
			this.features.get(name).involvedCrossTreeConstraints
					.add(constraint);
		
		this.crossTreeConstraints.add(constraint);

		for(Clause clause:constraint.getClauses())
			this.addClause(clause);
		
		return true;
	}

	@Override
	public boolean addTreeConstraint(TreeConstraint treeConstraint) {
		for (String name : treeConstraint.getInvolvedFeatures())
			if (this.features.get(name) == null)
				return false;
		for (String name : treeConstraint.getChildren())
			if (this.features.get(name).parentTree != null)
				return false;

		this.features.get(treeConstraint.getParent()).childrenTrees
				.add(treeConstraint);
		for (String name : treeConstraint.getChildren())
			this.features.get(name).parentTree = treeConstraint;
		this.treeConstraints.add(treeConstraint);

		for(Clause clause:treeConstraint.getClauses())
			this.addClause(clause);
		
		return true;
	}
	
	@Override
	public boolean hasChildren(String name) {
		Vector<TreeConstraint> childrenTrees=getChildrenTrees(name);
		if(childrenTrees==null || childrenTrees.size()==0)
			return false;
		else
			return true;
	}

	@Override
	public Vector<TreeConstraint> getChildrenTrees(String name) {
		FMNode node = this.features.get(name);
		if (node == null)
			return null;
		else
			return node.childrenTrees;
	}

	@Override
	public Vector<Feature> getChildren(String name) {
		Vector<TreeConstraint> childrenTrees = this.getChildrenTrees(name);
		if (childrenTrees == null)
			return null;
		else {
			Vector<Feature> children = new Vector<Feature>();
			for (TreeConstraint treeConstraint : childrenTrees)
				for (String child : treeConstraint.getChildren())
					children.add(this.getFeature(child));
			return children;
		}
	}

	@Override
	public TreeConstraint getParentTree(String name) {
		FMNode node = this.features.get(name);
		if (node == null)
			return null;
		else
			return node.parentTree;
	}

	@Override
	public Feature getParent(String name) {
		TreeConstraint parentTree = this.getParentTree(name);
		if (parentTree == null)
			return null;
		else {
			return this.getFeature(parentTree.getParent());
		}
	}

	@Override
	public Vector<TreeConstraint> getTreeConstraints() {
		return this.treeConstraints;
	}

	@Override
	public Vector<Constraint> getCrossTreeConstraints() {
		return this.crossTreeConstraints;
	}

	@Override
	public Vector<Feature> getFeatures() {
		Vector<Feature> features=new Vector<Feature>(this.getNumFeatures());
		for (Map.Entry<String, FMNode> entry : this.features.entrySet()){
			features.add(entry.getValue().feature);
        }
		return features;
	}
	
	@Override
	public Set<Clause> getClauses(String featureName) {
		FMNode node=this.features.get(featureName);
		if(node!=null)
			return node.involvedClauses;
		else
			return null;
	}

	private void addClause(Clause clause){
		for(String involvedFeature:clause.literals.keySet()){
			FMNode node=this.features.get(involvedFeature);
			if(node!=null)
				node.involvedClauses.add(clause);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder("FM(");
		this.toString(this.getRoot().getName(), builder);
		
		for(Constraint constraint: this.getCrossTreeConstraints()){
			builder.append(constraint.toString()+';');
		}
		builder.append(')');
		return builder.toString();
	}
	
	private void toString(String featureName,StringBuilder builder){
		builder.append('"'+featureName+'"');
		if(this.hasChildren(featureName)){
			builder.append(" : ");
			for(TreeConstraint treeConstraint: this.getChildrenTrees(featureName)){
				if(treeConstraint instanceof OptionalFeature){
					builder.append("[\""+treeConstraint.getChildren()[0]+"\"] ");
				}else{
					if(treeConstraint instanceof OrGroup){
						builder.append("Or(");
						for(String subFeature:treeConstraint.getChildren())
							builder.append('"'+subFeature+'"'+' ');
						builder.append(")");
					}else{
						if(treeConstraint instanceof AlternativeGroup){
							builder.append("Alternative(");
							for(String subFeature:treeConstraint.getChildren())
								builder.append('"'+subFeature+'"'+' ');
							builder.append(")");
						}else{
							if(treeConstraint instanceof MandatoryFeature){
								builder.append('"'+treeConstraint.getChildren()[0]+'"'+' ');
							}
						}
					}
				}
			}
			builder.append(';');
			for(Feature feature: this.getChildren(featureName)){
				if(this.hasChildren(feature.getName()))
					toString(feature.getName(),builder);
			}
		}
	}


}


