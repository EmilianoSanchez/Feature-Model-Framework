package edu.isistan.fmframework.experiments;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.io.ProblemGenerator;
import edu.isistan.fmframework.io.SPLOTRepository;
import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.opt01LP.Java01LPalgorithm;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicA;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicB;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.Heuristics;
import edu.isistan.fmframework.optimization.optCSA.searchStrategies.CSABacktracking;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.MaximaxUVS;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.StaticMostConstrained;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.TopDownUnassignedVariableSelector;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.UnassignedVariableSelectors;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.ValuePerWeightUnassignedVariableSelector;
import edu.isistan.fmframework.optimization.optGAFES.GAFESalgorithm;
import edu.isistan.fmframework.optimization.optSPLConfig.SPLConfigAlgorithm;
import edu.isistan.fmframework.optimization.optSPLConfigOriginal.SPLConfigAlgorithmOriginal;
import edu.isistan.fmframework.utils.RandomUtils;
import fm.FeatureModelException;

public class ExperimentApproxVariants {

	@Test
	public void test() throws IOException, FeatureModelException {

		Algorithm<BasicProblem> algs[] = new Algorithm[] {

				new Java01LPalgorithm(),

				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(),
						new MaximaxUVS(new HeuristicA())),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(),
						new MaximaxUVS(new HeuristicB())),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(),
						new MaximaxUVS(new HeuristicB())),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(),
						new StaticMostConstrained()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(),
						new StaticMostConstrained()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(),
						new ValuePerWeightUnassignedVariableSelector()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(),
						new ValuePerWeightUnassignedVariableSelector()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(),
						new TopDownUnassignedVariableSelector()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(),
						new TopDownUnassignedVariableSelector()),

//				new SPLConfigAlgorithm(), 
				new SPLConfigAlgorithmOriginal(), 
				new GAFESalgorithm() };

		List<FeatureModel> models = SPLOTRepository.getModels(0, 882);
		List<BasicProblem> instances = ProblemGenerator.generateValidBasicProblems(models, new double[] { 2.0 });

		Experiment<BasicProblem> experiment = new Experiment<>(algs, instances);
		experiment.execute();

		experiment.setProblemInstances(ProblemGenerator.generateValidBasicProblems(models, new double[] { 2.0 }));
		experiment.execute();
		experiment.printStats(System.out);
//		experiment.saveResults("Exp4_GAFES_and_SPLConfig_0");

		experiment.setProblemInstances(ProblemGenerator.generateValidBasicProblems(models, new double[] { 0.9 }));
		experiment.execute();
		experiment.printStats(System.out);
//		experiment.saveResults("Exp4_GAFES_and_SPLConfig_9");

		experiment.setProblemInstances(ProblemGenerator.generateValidBasicProblems(models, new double[] { 0.8 }));
		experiment.execute();
		experiment.printStats(System.out);
//		experiment.saveResults("Exp4_GAFES_and_SPLConfig_8");

		experiment.setProblemInstances(ProblemGenerator.generateValidBasicProblems(models, new double[] { 0.7 }));
		experiment.execute();
		experiment.printStats(System.out);
//		experiment.saveResults("Exp4_GAFES_and_SPLConfig_7");

	}
}
