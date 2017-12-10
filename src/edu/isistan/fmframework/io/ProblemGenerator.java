package edu.isistan.fmframework.io;

import java.util.LinkedList;
import java.util.List;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.globalConstraints.InequalityRestriction;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;
import edu.isistan.fmframework.utils.RandomUtils;

public class ProblemGenerator {

	public static BasicProblem generateValidBasicProblem(FeatureModel model, int numInequalityRestrictions) {
		return generateValidBasicProblem(model, numInequalityRestrictions, 0.9);
	}

	public static BasicProblem generateValidBasicProblem(FeatureModel model, int numInequalityRestrictions,
			double restrictionRatio) {
		double[] restrictionRatios = new double[numInequalityRestrictions];
		for (int i = 0; i < numInequalityRestrictions; i++) {
			restrictionRatios[i] = restrictionRatio;
		}
		return generateValidBasicProblem(model, restrictionRatios);
	}

	public static BasicProblem generateValidBasicProblem(FeatureModel model, double[] restrictionRatios) {
		BasicProblem instance = null;
		do {
			instance = generateBasicProblem(model, restrictionRatios);
		} while (!instance.isValid());

		return instance;
	}

	public static BasicProblem generateBasicProblem(FeatureModel model, double[] restrictionRatios) {
		
		BasicProblem instance = new BasicProblem(model,
				new AdditionObjective(RandomUtils.randomArray(model.getNumFeatures())));

		instance.globalConstraints = new InequalityRestriction[restrictionRatios.length];

		for (int i = 0; i < restrictionRatios.length; i++) {
			double[] attributes = RandomUtils.randomArray(model.getNumFeatures());
			double sum = 0.0;
			for (int a = 0; a < attributes.length; a++)
				sum += attributes[i];

			instance.globalConstraints[i] = new InequalityRestriction(attributes, sum * restrictionRatios[i]);
		}

		return instance;
	}

	public static List<BasicProblem> generateValidBasicProblems(List<FeatureModel> models, int numInequalityRestrictions) {
		List<BasicProblem> instances=new LinkedList<>();  
		for (int i = 0; i < models.size(); i++) {
			instances.add(generateValidBasicProblem(models.get(i),numInequalityRestrictions));
		}
		return instances;
	};
	public static List<BasicProblem> generateValidBasicProblems(List<FeatureModel> models, double[] restrictionRatios) {
		List<BasicProblem> instances=new LinkedList<>();  
		for (int i = 0; i < models.size(); i++) {
			instances.add(generateValidBasicProblem(models.get(i),restrictionRatios));
		}
		return instances;
	};

}
