package edu.isistan.fmframework.evaluation;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.globalConstraints.InequalityRestriction;
import edu.isistan.fmframework.evaluation.FMGenerator.CrossTreeConstraintType;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.Utils;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.MultiLinearPolynomialObjective;
import edu.isistan.fmframework.utils.RandomUtils;

public class ProblemGenerator {

	public static final double DEFAULT_RESTRICTION_RATIO = 0.9;
	public static final double THUM_BRANCHING_FACTOR = 5.0;
	public static final double SPLOT_BRANCHING_FACTOR = 3.5;
	public static final double THUM_CTC_RATIO = 0.1;
	public static final double SPLOT_CTC_RATIO = 0.147;
	public static final double[] THUM_TCTDISTRIBUTION = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
	public static final double[] SPLOT_TCTDISTRIBUTION = new double[] { 0.0, 0.0, 0.1992, 0.2444, 0.2243, 0.3321, 0.0 };
	public static final double[] SPLOT_TCTDISTRIBUTION_OR_XOR_INTO_OPTIONALS = new double[] { 0.0, 0.0, 0.0, 0.0,
			0.6679, 0.3321, 0.0 };
	public static final double[] SPLOT_TCTDISTRIBUTION_OR_INTO_CARDINALITY = new double[] { 0.0, 0.0, 0.0, 0.2444,
			0.2243, 0.3321, 0.1992 };

	private int numFeatures;
	private double[] treeConstraintTypeDistribution;
	private int numTreeConstraints;
	private CrossTreeConstraintType ctctype;
	private int numCrossTreeConstraints;
	private double[] restrictionRatios;
	private String name;

	public ProblemGenerator(String name, int numFeatures) {
		this(name, numFeatures, 0.0);
	}

	public ProblemGenerator(String name, int numFeatures, double ctcRatio) {
		this(name, numFeatures, ctcRatio, 0);
	}

	public ProblemGenerator(String name, int numFeatures, double ctcRatio, int numIR) {
		this(name, numFeatures, SPLOT_TCTDISTRIBUTION, (int) (numFeatures / SPLOT_BRANCHING_FACTOR),
				CrossTreeConstraintType.BINARY_CONSTRAINT, (int) (numFeatures * ctcRatio), numIR);
	}

	public ProblemGenerator(String name, int numFeatures, double ctcRatio, int numIR, double branchingFactor) {
		this(name, numFeatures, SPLOT_TCTDISTRIBUTION, (int) (numFeatures / branchingFactor),
				CrossTreeConstraintType.BINARY_CONSTRAINT, (int) (numFeatures * ctcRatio), numIR);
	}

	public ProblemGenerator setBranchingFactor(double branchingFactor) {
		this.numTreeConstraints = (int) ((numFeatures - 1) / branchingFactor);
		return this;
	}

	public ProblemGenerator setTreeConstraintDistribution(double[] treeConstraintTypeDistribution) {
		this.treeConstraintTypeDistribution = treeConstraintTypeDistribution;
		return this;
	}

	public ProblemGenerator setCTCRatio(double ctcRatio) {
		this.numCrossTreeConstraints = (int) (numFeatures * ctcRatio);
		return this;
	}

	public ProblemGenerator setIRs(int numIR) {
		return setIRs(numIR, DEFAULT_RESTRICTION_RATIO);
	}

	public ProblemGenerator setIRs(int numIR, double restrictionRatio) {
		this.restrictionRatios = new double[numIR];
		for (int i = 0; i < restrictionRatios.length; i++) {
			restrictionRatios[i] = restrictionRatio;
		}
		return this;
	}

	public ProblemGenerator(String name, int numFeatures, double[] treeConstraintTypeDistribution,
			int numTreeConstraints, CrossTreeConstraintType ctctype, int numCrossTreeConstraints, int numIR) {
		this.name = name;
		this.numFeatures = numFeatures;
		this.treeConstraintTypeDistribution = treeConstraintTypeDistribution;
		this.numTreeConstraints = numTreeConstraints;
		this.ctctype = ctctype;
		this.numCrossTreeConstraints = numCrossTreeConstraints;

		this.restrictionRatios = new double[numIR];
		for (int i = 0; i < restrictionRatios.length; i++) {
			restrictionRatios[i] = DEFAULT_RESTRICTION_RATIO;
		}
	}

	public BasicProblem generateBasicProblemInstance() {
		return generateValidBasicProblemInstance(numFeatures, treeConstraintTypeDistribution, numTreeConstraints,
				ctctype, numCrossTreeConstraints, restrictionRatios);
	}

	@Override
	public String toString() {
		return name;
	}

	public static BasicProblem generateValidBasicProblemInstance(int numFeatures,
			double[] treeConstraintTypeDistribution, int numTreeConstraints, CrossTreeConstraintType ctctype,
			int numCrossTreeConstraints, double[] restrictionRatios) {
		FeatureModel model = generateValidFeatureModel(numFeatures, treeConstraintTypeDistribution, numTreeConstraints,
				ctctype, numCrossTreeConstraints);
		BasicProblem instance = generateValidBasicProblemInstance(model, restrictionRatios);
		return instance;
	}

	public static BasicProblem generateValidBasicProblemInstance(int numFeatures, int numLinearRestrictions) {
		FeatureModel model = generateValidFeatureModel(numFeatures);
		BasicProblem instance = generateValidBasicProblemInstance(model, numLinearRestrictions);
		return instance;
	}

	public static FeatureModel generateValidFeatureModel(int numFeatures, double[] treeConstraintTypeDistribution,
			int numTreeConstraints, CrossTreeConstraintType ctctype, int numCrossTreeConstraints) {
		FeatureModel model;
		do {
			model = FMGenerator.generateFeatureModel(numFeatures, treeConstraintTypeDistribution, numTreeConstraints,
					ctctype, numCrossTreeConstraints);
		} while (!Utils.isValid(model));
		return model;
	}

	public static FeatureModel generateValidFeatureModel(int numFeatures) {
		FeatureModel model;
		do {
			model = FMGenerator.generateFeatureModel(numFeatures, FMGenerator.FeatureModelType.RANDOM, numFeatures / 5,
					FMGenerator.CrossTreeConstraintType.BINARY_CONSTRAINT, numFeatures / 10);
		} while (!Utils.isValid(model));
		return model;
	}

	public static BasicProblem generateBasicProblemInstance(FeatureModel model, int numLinearRestrictions) {
		double[] restrictionRatios = new double[numLinearRestrictions];
		for (int i = 0; i < numLinearRestrictions; i++) {
			restrictionRatios[i] = 0.9;
		}

		BasicProblem instance = generateBasicProblemInstance(model, restrictionRatios);
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
		} while (!instance.isValid());

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
				double restrictionLimit = 0.5 * model.getNumFeatures() * restrictionRatios[i];
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
		} while (!instance.isValid());

		return instance;
	}
	
	public static List<BasicProblem> generateValidBasicProblemInstances(List<Pair<File,FeatureModel>> models,
			int numLinearRestrictions) {
		List<BasicProblem> problems = new LinkedList<>();
		for (Pair<File,FeatureModel> modelEntry : models) {
			problems.add(generateValidBasicProblemInstance(modelEntry.getValue(), numLinearRestrictions));
		}
		return problems;
	}

	public static Problem<?,?> generateValidGeneralProblemInstance_SPLOT_plus_THOR(FeatureModel model,
			MultiLinearPolynomialObjective[] modelThorObjectives, int numLinearRestrictions) {
		double[] restrictionRatios = new double[numLinearRestrictions];
		for (int i = 0; i < numLinearRestrictions; i++) {
			restrictionRatios[i] = 0.9;
		}

		Problem<?,?> instance = null;
		do {
			instance = generateGeneralProblemInstance_SPLOT_plus_THOR(model, modelThorObjectives, restrictionRatios);
		} while (!instance.isValid());

		return instance;
	};

	// TODO extender para mas objetivos y restricciones
	public static Problem<?,?> generateGeneralProblemInstance_SPLOT_plus_THOR(FeatureModel model,
			MultiLinearPolynomialObjective[] modelThorObjectives, double[] restrictionRatios) {
		return new Problem<>(model, modelThorObjectives[0], new InequalityRestriction[0]);
	};

}
