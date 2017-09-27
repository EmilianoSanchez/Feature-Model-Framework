package com.emiliano.fmframework.operations;

import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.constraints.crossTreeConstraints.Exclude;
import com.emiliano.fmframework.core.constraints.crossTreeConstraints.Imply;
import com.emiliano.fmframework.core.constraints.crossTreeConstraints.MutualExclusion;
import com.emiliano.fmframework.core.constraints.crossTreeConstraints.MutualImplication;
import com.emiliano.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OptionalFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OrGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint;

public interface Visitor {

	public void visit(FeatureModel model);

	public void visit(Feature feature);

	public void visit(TreeConstraint constraint);

	public void visit(AlternativeGroup constraint);

	public void visit(OrGroup constraint);

	public void visit(OptionalFeature constraint);

	public void visit(MandatoryFeature constraint);

	public void visit(Exclude constraint);

	public void visit(Imply constraint);

	public void visit(MutualExclusion constraint);

	public void visit(MutualImplication constraint);

}
