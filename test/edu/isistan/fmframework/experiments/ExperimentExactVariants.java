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
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.Heuristics;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.UnassignedVariableSelectors;
import edu.isistan.fmframework.optimization.optSAT.JavaSAT01LPalgorithm;
import edu.isistan.fmframework.utils.RandomUtils;
import fm.FeatureModelException;

public class ExperimentExactVariants {
	
	@Test
	public void test() throws IOException, FeatureModelException {

		Algorithm<BasicProblem> algs[] = new Algorithm[] {
				
				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, UnassignedVariableSelectors.maximaxUVS),
				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, UnassignedVariableSelectors.staticMostConstrained),
//				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, UnassignedVariableSelectors.valuePerWeightUnassignedVariableSelector),
//				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, UnassignedVariableSelectors.maximaxUVS),
//				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, UnassignedVariableSelectors.staticMostConstrained),
//				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, UnassignedVariableSelectors.valuePerWeightUnassignedVariableSelector),
				
//				new JavaSAT01LPalgorithm(JavaSAT01LPalgorithm.ILPSolver.SAT4J),
				new Java01LPalgorithm(Java01LPalgorithm.ILPSolver.SAT4J)		
				
		};
		
		List<FeatureModel> models = SPLOTRepository.getModels(0, 882);
		List<BasicProblem> instances=ProblemGenerator.generateValidBasicProblems(models,0);
		
		Experiment<BasicProblem> experiment = new Experiment<>(algs,instances);
		experiment.execute();
		
		experiment.setProblemInstances(ProblemGenerator.generateValidBasicProblems(models,0));
		experiment.execute();
		experiment.printStats(System.out);
		experiment.saveResults("exp1y3_of1_ir0");
		
		experiment.setAlgorithms(algs[0],algs[2]);
		models = SPLOTRepository.getModels(0, 870);
		experiment.setProblemInstances(ProblemGenerator.generateValidBasicProblems(models,1));
		experiment.execute();
		experiment.printStats(System.out);
//		experiment.saveResults("exp1y3_of1_ir1");
		
		experiment.setProblemInstances(ProblemGenerator.generateValidBasicProblems(models,2));
		experiment.execute();
		experiment.printStats(System.out);
//		experiment.saveResults("exp1y3_of1_ir2");
//		
//		RandomUtils.setSeed(122229);
//		experiment.setProblemInstances(ProblemGenerator.generateValidBasicProblems(models,5));
//		experiment.execute();
//		experiment.printStats(System.out);
//		experiment.saveResults("exp1y3_of1_ir5");
		
	}

}
