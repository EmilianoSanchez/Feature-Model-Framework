package com.emiliano.fmframework.test;

import com.emiliano.fmframework.building.FMBuilder;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.constraints.crossTreeConstraints.Imply;

public class TestFMBuilder {

	public static void main(String[] args) {
		FeatureModel model = getModel2();
		System.out.println(model);
	}

	public static FeatureModel getModel1() {
		FeatureModel model = new FMBuilder("FunctionalFeature")
				.addOptionalFeature(new FMBuilder("FACEPOSITION_DETECTION"))
				.addOptionalFeature(new FMBuilder("FACEORIENTATION_DETECTION"))
				.addOptionalFeature(new FMBuilder("LANDMARK_DETECTION"))
				.addOptionalFeature(new FMBuilder("GENDER_CLASSIFICATION"))
				.addOptionalFeature(new FMBuilder("AGE_CLASSIFICATION"))
				.addOptionalFeature(new FMBuilder("RACE_CLASSIFICATION"))
				.addOptionalFeature(new FMBuilder("OPENCLOSEEYES_CLASSIFICATION"))
				.addOptionalFeature(new FMBuilder("SMILE_CLASSIFICATION"))
				.addOptionalFeature(new FMBuilder("FACIALEXPRESSIONS_CLASSIFICATION")).buildModel();
		return model;
	};

	public static FeatureModel getModel2() {
		FeatureModel model = new FMBuilder("f1").addMandatoryFeature(new FMBuilder("fA"))
				.addOptionalFeature(new FMBuilder("f2").addAlternativeGroup(new FMBuilder("f3"), new FMBuilder("f4")))
				.addOrGroup(new FMBuilder("f6"), new FMBuilder("f7")).addCrossTreeConstraint(new Imply("f1", "f2"))
				.buildModel();
		return model;
	}

	public static FeatureModel getModel3() {
		FeatureModel model = new FMBuilder("root").addMandatoryFeature(new FMBuilder("child")).buildModel();
		return model;
	}
}
