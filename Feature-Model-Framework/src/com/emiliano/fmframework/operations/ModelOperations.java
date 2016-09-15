package com.emiliano.fmframework.operations;

import java.util.ArrayList;

import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.constraints.Constraint;
import com.emiliano.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OptionalFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OrGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint;

public class ModelOperations {

	public static boolean removeSubModel(FeatureModel featureModel, String rootFeature) {
		// TODO Auto-generated method stub
		return true;
	}

	private static boolean hasEqualFeatureNames(FeatureModel featureModel1, FeatureModel featureModel2) {
		for (Feature feature : featureModel1.getFeatures())
			if (featureModel2.getFeature(feature.getName()) != null)
				return true;
		return false;
	}

	public static boolean insertSubModel(FeatureModel featureModel, FeatureModel subModel) {
		if (!hasEqualFeatureNames(featureModel, subModel)) {
			for (Feature feature : subModel.getFeatures())
				featureModel.addFeature(feature);
			for (TreeConstraint treeConstraint : subModel.getTreeConstraints())
				featureModel.addTreeConstraint(treeConstraint);
			for (Constraint constraint : subModel.getCrossTreeConstraints())
				featureModel.addCrossTreeConstraint(constraint);
			return true;
		}
		return false;
	}

	// public static boolean insertCardinalGroupSubModels(
	// FeatureModel featureModel, String insertionPoint,
	// int lowerBound, int upperBound,
	// FeatureModel... subModels) {
	// // TODO Auto-generated method stub
	// return true;
	// }

	public static boolean insertOrGroupSubModels(FeatureModel featureModel, String insertionPoint, int lowerBound,
			int upperBound, FeatureModel... subModels) {
		// return insertCardinalGroupSubModels(featureModel, insertionPoint, 1,
		// subModels.length, subModels);
		ArrayList<String> subFeatures = new ArrayList<String>();
		for (FeatureModel subModel : subModels) {
			subFeatures.add(subModel.getRoot().getName());
			if (!insertSubModel(featureModel, subModel))
				return false;
		}
		if (featureModel.getFeature(insertionPoint) != null)
			featureModel.addTreeConstraint(
					new OrGroup(insertionPoint, subFeatures.toArray(new String[subFeatures.size()])));
		return true;
	}

	public static boolean insertAlternativeGroupSubModels(FeatureModel featureModel, String insertionPoint,
			int lowerBound, int upperBound, FeatureModel... subModels) {
		// return insertCardinalGroupSubModels(featureModel, insertionPoint, 1,
		// 1,
		// subModels);
		ArrayList<String> subFeatures = new ArrayList<String>();
		for (FeatureModel subModel : subModels) {
			subFeatures.add(subModel.getRoot().getName());
			if (!insertSubModel(featureModel, subModel))
				return false;
		}
		if (featureModel.getFeature(insertionPoint) != null)
			featureModel.addTreeConstraint(
					new AlternativeGroup(insertionPoint, subFeatures.toArray(new String[subFeatures.size()])));
		return true;
	}

	public static boolean insertMandatorySubModel(FeatureModel featureModel, String insertionPoint,
			FeatureModel subModel) {
		if (insertSubModel(featureModel, subModel) && featureModel.getFeature(insertionPoint) != null) {
			featureModel.addTreeConstraint(new MandatoryFeature(insertionPoint, subModel.getRoot().getName()));
		}
		return false;
	}

	public static boolean insertOptionalSubModel(FeatureModel featureModel, String insertionPoint,
			FeatureModel subModel) {
		if (insertSubModel(featureModel, subModel) && featureModel.getFeature(insertionPoint) != null) {
			featureModel.addTreeConstraint(new OptionalFeature(insertionPoint, subModel.getRoot().getName()));
		}
		return false;
	}

}
