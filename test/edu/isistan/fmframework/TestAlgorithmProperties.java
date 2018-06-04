package edu.isistan.fmframework;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.evaluation.ProblemGenerator;
import edu.isistan.fmframework.evaluation.SPLOTModels;
import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.objectiveFunctions.MultiLinearPolynomialObjective;
import edu.isistan.fmframework.optimization.opt01LP.Java01LPalgorithm;
import edu.isistan.fmframework.optimization.optBoolOpt.JavaBoolOptAlgorithm;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagators;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicA;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicB;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.Heuristics;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.VariableSelectors;
import edu.isistan.fmframework.optimization.optRLT_01LP.Java_RLT_01LPalgorithm;
import fm.FeatureModelException;
import net.sf.javailp.OptType;

public class TestAlgorithmProperties {

	@Test
	@Ignore
	public void testExactVsAproxAlgorithms() throws FeatureModelException {

		Algorithm<BasicProblem> exactAlgorithms[] = new Algorithm[] { new Java01LPalgorithm(OptType.MIN),
				new JavaBoolOptAlgorithm(OptType.MIN),
				CSAalgorithm.build("BestFS+HB+MHV", CSAalgorithm.Strategy.BestFS, Heuristics.heuristicB,
						VariableSelectors.maxHeuristicValueVariableSelector),
				CSAalgorithm.build("BandB+HB+MHV", CSAalgorithm.Strategy.BandB, Heuristics.heuristicB,
						VariableSelectors.maxHeuristicValueVariableSelector) };
		CSAalgorithm approxAlgorithmHB = CSAalgorithm.build("BT+HB", CSAalgorithm.Strategy.BT, new HeuristicB());
		CSAalgorithm approxAlgorithmHA = CSAalgorithm.build("BT+HA", CSAalgorithm.Strategy.BT, new HeuristicA());

		List<Pair<File, FeatureModel>> models = SPLOTModels.getModels(0, 882);
		List<BasicProblem> instances = ProblemGenerator.generateValidBasicProblemInstances(models, 0);

		int count = 0;
		int counts[] = new int[3];
		for (BasicProblem instance : instances) {
			System.out.println(count);
			count++;

			double values[] = new double[exactAlgorithms.length];
			Configuration conf;
			for (int i = 0; i < exactAlgorithms.length; i++) {
				exactAlgorithms[i].preprocessInstance(instance);
				conf = exactAlgorithms[i].selectConfiguration(instance);
				values[i] = instance.evaluateObjectives(conf)[0];
			}
			approxAlgorithmHA.preprocessInstance(instance);
			Configuration confA = approxAlgorithmHA.selectConfiguration(instance);
			double aproxValueHA = instance.evaluateObjectives(confA)[0];

			approxAlgorithmHB.preprocessInstance(instance);
			Configuration confB = approxAlgorithmHB.selectConfiguration(instance);
			double aproxValueHB = instance.evaluateObjectives(confB)[0];

			for (int i = 1; i < exactAlgorithms.length; i++) {
				Assert.assertTrue(values[0] == values[i]);
			}

			Assert.assertTrue(values[0] >= aproxValueHB);
			Assert.assertTrue(aproxValueHB >= aproxValueHA);

			if (aproxValueHB == aproxValueHA) {
				counts[1] += 1;
			} else {
				if (aproxValueHB > aproxValueHA) {
					counts[0] += 1;
				} else {
					counts[2] += 1;
					System.out.println(aproxValueHB + " " + aproxValueHA);
					System.out.println(confB);
					System.out.println(confA);
					System.out.println("Sum: " + DoubleStream.of(instance.objectiveFunctions[0].attributes).sum());
					System.out.println(instance.toString());
				}
			}
		}
		System.out.println("HB>HA: " + counts[0] + " HB==HA: " + counts[1] + " HB<HA: " + counts[2]);
	}

	@Test
	// @Ignore
	public void testExactAlgorithmsMultiLinearPolynomialObjective() throws FeatureModelException {

		Algorithm<Problem> exactAlgorithms[] = new Algorithm[] { new Java_RLT_01LPalgorithm(OptType.MIN),
				CSAalgorithm.build("BestFS+HB+MHV", CSAalgorithm.Strategy.BestFS, Heuristics.heuristicB,
						VariableSelectors.maxHeuristicValueVariableSelector),
				CSAalgorithm.build("BandB+HB+MHV", CSAalgorithm.Strategy.BandB, Heuristics.heuristicB,
						VariableSelectors.maxHeuristicValueVariableSelector) };

		List<Pair<File, FeatureModel>> modelEntries = SPLOTModels.getModels(0, 882);
		List<Problem> instances = new LinkedList<Problem>();
		for (Pair<File, FeatureModel> entry : modelEntries) {
			FeatureModel model = entry.getValue();
			int numFeatures = model.getNumFeatures();
			MultiLinearPolynomialObjective objective = MultiLinearPolynomialObjective
					.generateRandomInstance(numFeatures, new int[] { numFeatures, numFeatures, numFeatures });
			instances.add(new Problem(model, objective));
		}

		int count = 0;
		for (Problem instance : instances) {
			System.out.println(count);
			count++;

			// System.out.println(instance.model.toString());
			// System.out.println(instance.objectiveFunctions[0].toString());

			double values[] = new double[exactAlgorithms.length];
			Configuration conf;
			for (int i = 0; i < exactAlgorithms.length; i++) {
				exactAlgorithms[i].preprocessInstance(instance);
				conf = exactAlgorithms[i].selectConfiguration(instance);
				values[i] = instance.evaluateObjectives(conf)[0];

				// System.out.println(conf);
				// System.out.println(instance.isSatisfied(conf));
				// System.out.println(values[i]);
			}

			for (int i = 1; i < exactAlgorithms.length; i++) {
				Assert.assertTrue(values[0] == values[i]);
			}
		}
	}

	@Test
	@Ignore
	public void testAdmissibleHeuristic() throws FeatureModelException {
		HeuristicB heuristicB = new HeuristicB();
		HeuristicA heuristicA = new HeuristicA();

		List<Pair<File, FeatureModel>> models = SPLOTModels.getModels(0, 882);
		List<BasicProblem> instances = ProblemGenerator.generateValidBasicProblemInstances(models, 0);

		int count = 0;
		for (BasicProblem instance : instances) {
			System.out.println(count);
			count++;

			Configuration partialConfiguration = ConstraintPropagators.clauseBasedConstraintPropagator
					.getPartialConfiguration(instance.model);
			heuristicA.setup(instance);
			double evaluationA = heuristicA.evaluate(partialConfiguration);
			heuristicB.setup(instance);
			double evaluationB = heuristicB.evaluate(partialConfiguration);

			Assert.assertTrue(evaluationA <= evaluationB);

		}
	}

	@Test
	@Ignore
	public void testAdmissibleHeuristicMultiLinearPolynomialObjective() throws FeatureModelException {
		HeuristicB heuristicB = new HeuristicB();
		HeuristicA heuristicA = new HeuristicA();

		List<Pair<File, FeatureModel>> models = SPLOTModels.getModels(0, 882);
		List<Pair<File, FeatureModel>> modelEntries = SPLOTModels.getModels(0, 882);
		List<Problem> instances = new LinkedList<Problem>();
		for (Pair<File, FeatureModel> entry : modelEntries) {
			FeatureModel model = entry.getValue();
			int numFeatures = model.getNumFeatures();
			MultiLinearPolynomialObjective objective = MultiLinearPolynomialObjective
					.generateRandomInstance(numFeatures, new int[] { numFeatures });
			instances.add(new Problem(model, objective));
		}

		int count = 0;
		for (Problem instance : instances) {
			System.out.println(count);
			count++;

			Configuration partialConfiguration = ConstraintPropagators.clauseBasedConstraintPropagator
					.getPartialConfiguration(instance.model);
			heuristicA.setup(instance);
			double evaluationA = heuristicA.evaluate(partialConfiguration);
			heuristicB.setup(instance);
			double evaluationB = heuristicB.evaluate(partialConfiguration);

			Assert.assertTrue(evaluationA <= evaluationB);
		}
	}
}
