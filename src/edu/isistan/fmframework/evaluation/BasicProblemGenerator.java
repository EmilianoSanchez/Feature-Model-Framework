package edu.isistan.fmframework.evaluation;

import java.util.LinkedList;
import java.util.List;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.globalConstraints.InequalityRestriction;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Utils;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;
import edu.isistan.fmframework.utils.RandomUtils;

public class BasicProblemGenerator {

	public static BasicProblem generateBasicProblemInstance(int numFeatures,
			int numLinearRestrictions) {
		FeatureModel model = generateValidFeatureModel(numFeatures);
		BasicProblem instance = generateValidBasicProblemInstance(model,
				numLinearRestrictions);
		return instance;
	}
	
	public static FeatureModel generateValidFeatureModel(int numFeatures) {
		FeatureModel model = FMGenerator.generateFeatureModel(numFeatures, FMGenerator.FeatureModelType.RANDOM, numFeatures / 5,
				FMGenerator.CrossTreeConstraintType.BINARY_CONSTRAINT, numFeatures / 10);
		for (;;) {
			if (Utils.isValid(model)) {
				break;
			} else {
				model = FMGenerator.generateFeatureModel(numFeatures, FMGenerator.FeatureModelType.RANDOM, numFeatures / 5,
						FMGenerator.CrossTreeConstraintType.BINARY_CONSTRAINT, numFeatures / 10);
			}
		}
		return model;
	}
	
	public static BasicProblem generateBasicProblemInstance(FeatureModel model,
			int numLinearRestrictions) {
		double[] restrictionRatios = new double[numLinearRestrictions];
		for (int i = 0; i < numLinearRestrictions; i++) {
			restrictionRatios[i] = 0.9;
		}

		BasicProblem instance = generateBasicProblemInstance(model,
				restrictionRatios);
		return instance;
	}

	public static double[] generateFeatureAttributes(int numFeatures) {
		double[] randomValues = new double[numFeatures];
		for (int i = 0; i < numFeatures; i++)
			randomValues[i] = RandomUtils.random();
		return randomValues;
	}

	public static BasicProblem generateValidBasicProblemInstance(FeatureModel featureModel,
			double[] restrictionRatios) {
		BasicProblem instance = null;
		do {
			instance = generateBasicProblemInstance(featureModel, restrictionRatios);			
		}while (!instance.isValid());

		return instance;
	}
	
	public static BasicProblem generateBasicProblemInstance(FeatureModel model, double[] restrictionRatios) {
		
		AdditionObjective objective = new AdditionObjective(generateFeatureAttributes(model.getNumFeatures()));

		double[] restrictionLimits = new double[restrictionRatios.length];
		for (int i = 0; i < restrictionRatios.length; i++) {
			restrictionLimits[i] = model.getNumFeatures() * 0.5 * restrictionRatios[i];
		}

		InequalityRestriction[] restrictions = new InequalityRestriction[0];
		if (restrictionRatios != null && restrictionRatios.length > 0) {
			restrictions = new InequalityRestriction[restrictionRatios.length];
			for (int i = 0; i < restrictionRatios.length; i++) {
				double[] attributes = generateFeatureAttributes(model.getNumFeatures());
				double restrictionLimit = 0.5 *model.getNumFeatures()* restrictionRatios[i];
				restrictions[i] = new InequalityRestriction(attributes, restrictionLimit);
			}
		}

		return new BasicProblem(model, objective, restrictions);
	};
	
	public static BasicProblem generateValidBasicProblemInstance(FeatureModel model, int numLinearRestrictions) {
		double[] restrictionRatios = new double[numLinearRestrictions];
		for (int i = 0; i < numLinearRestrictions; i++) {
			restrictionRatios[i] = 0.9;
		}

		BasicProblem instance = null;
		do {
			instance = generateBasicProblemInstance(model, restrictionRatios);			
		}while (!instance.isValid());

		return instance;
	}

	public static List<BasicProblem> generateValidBasicProblemInstances(List<FeatureModel> models, int numLinearRestrictions) {
		List<BasicProblem> problems=new LinkedList();
		for(FeatureModel model:models){
			problems.add(generateValidBasicProblemInstance(model,numLinearRestrictions));
		}
		return problems;
	};

};
