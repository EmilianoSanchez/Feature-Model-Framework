package com.emiliano.fmframework.building;

import java.util.Arrays;
import java.util.Vector;

import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureAction;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.FeatureModelImpl;
import com.emiliano.fmframework.core.constraints.Constraint;
import com.emiliano.fmframework.core.constraints.crossTreeConstraints.LogicExpressionConstraint;
import com.emiliano.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OptionalFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OrGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint;

public class FMBuilder {

	private Feature feature;
	private Vector<TreeConstraint> treeConstraints;
	private Vector<FMBuilder> subBuilders;

	private void getFeatures(Vector<Feature> features) {
		features.add(this.feature);
		for (FMBuilder subBuilder : this.subBuilders)
			subBuilder.getFeatures(features);
	}

	private void getTreeConstraints(Vector<TreeConstraint> treeConstraints) {
		treeConstraints.addAll(this.treeConstraints);
		for (FMBuilder subBuilder : this.subBuilders)
			subBuilder.getTreeConstraints(treeConstraints);
	}

	public static FMBuilder createFMBuilder(String name) {
		return new FMBuilder(name);
	}

	public static FMBuilder createFMBuilder(String name, FeatureAction action) {
		return new FMBuilder(name, action);
	}

	public FMBuilder(String name) {
		this.feature = new Feature(name);
		this.treeConstraints = new Vector<TreeConstraint>();
		this.subBuilders = new Vector<FMBuilder>();
	}

	public FMBuilder(String name, FeatureAction action) {
		this.feature = new Feature(name, action);
		this.treeConstraints = new Vector<TreeConstraint>();
		this.subBuilders = new Vector<FMBuilder>();
	}

	public FMBuilder addOptionalFeature(FMBuilder builder) {
		String child = builder.feature.getName();
		TreeConstraint treeConstraint = new OptionalFeature(this.feature.getName(), child);
		this.treeConstraints.add(treeConstraint);
		this.subBuilders.add(builder);
		return this;
	}

	public FMBuilder addMandatoryFeature(FMBuilder builder) {
		String child = builder.feature.getName();
		TreeConstraint treeConstraint = new MandatoryFeature(this.feature.getName(), child);
		this.treeConstraints.add(treeConstraint);
		this.subBuilders.add(builder);
		return this;
	}

	public FMBuilder addAlternativeGroup(FMBuilder... builders) {
		String[] children = new String[builders.length];
		for (int i = 0; i < children.length; i++) {
			children[i] = builders[i].feature.getName();
		}
		TreeConstraint treeConstraint = new AlternativeGroup(this.feature.getName(), children);
		this.treeConstraints.add(treeConstraint);
		this.subBuilders.addAll(Arrays.asList(builders));
		return this;
	}

	public FMBuilder addOrGroup(FMBuilder... builders) {
		String[] children = new String[builders.length];
		for (int i = 0; i < children.length; i++) {
			children[i] = builders[i].feature.getName();
		}
		TreeConstraint treeConstraint = new OrGroup(this.feature.getName(), children);
		this.treeConstraints.add(treeConstraint);
		this.subBuilders.addAll(Arrays.asList(builders));
		return this;
	}

	// @SuppressWarnings("unchecked")
	// public FMBuilder addCardinalGroup(int lowerBound,
	// int upperBound, FMBuilder... builders) {
	// String[] children = new String[builders.length];
	// for (int i = 0; i < children.length; i++) {
	// children[i] = builders[i].feature.getName();
	// }
	// TreeConstraint treeConstraint = new CardinalGroup(lowerBound,
	// upperBound, this.feature.getName(), children);
	// this.treeConstraints.add(treeConstraint);
	// return this;
	// }

	public FeatureModel buildModel() {
		FeatureModel model = new FeatureModelImpl();
		Vector<Feature> features = new Vector<Feature>();
		this.getFeatures(features);
		for (Feature feature : features)
			model.addFeature(feature);
		Vector<TreeConstraint> treeConstraints = new Vector<TreeConstraint>();
		this.getTreeConstraints(treeConstraints);
		for (TreeConstraint treeConstraint : treeConstraints)
			model.addTreeConstraint(treeConstraint);
		return model;
	}

	public FMCTCBuilder addCrossTreeConstraint(Constraint constraint) {
		FeatureModel model = this.buildModel();
		return new FMCTCBuilder(model, constraint);
	}

	public FMCTCBuilder addCrossTreeConstraint(String expression) {
		LogicExpressionConstraint constraint = new LogicExpressionConstraint(expression);
		return this.addCrossTreeConstraint(constraint);
	}

	public static class FMCTCBuilder {
		private FeatureModel model;

		FMCTCBuilder(FeatureModel model, Constraint constraint) {
			this.model = model;
			this.model.addCrossTreeConstraint(constraint);
		}

		public FMCTCBuilder addCrossTreeConstraint(Constraint constraint) {
			return new FMCTCBuilder(model, constraint);
		}

		public FMCTCBuilder addCrossTreeConstraint(String expression) {
			LogicExpressionConstraint constraint = new LogicExpressionConstraint(expression);
			return this.addCrossTreeConstraint(constraint);
		}

		public FeatureModel buildModel() {
			return model;
		}
	}
}