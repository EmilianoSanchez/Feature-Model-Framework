package edu.isistan.fmframework;

import java.util.List;
import java.util.stream.DoubleStream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.io.ProblemGenerator;
import edu.isistan.fmframework.io.SPLOTRepository;
import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.opt01LP.Java01LPalgorithm;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagators;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicA;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicB;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.Heuristics;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.UnassignedVariableSelectors;
import edu.isistan.fmframework.optimization.optSAT.JavaSAT01LPalgorithm;
import fm.FeatureModelException;

public class TestAlgorithmProperties {

	@Test
	public void testExactVsAproxAlgorithms() throws FeatureModelException {
		
		Algorithm<BasicProblem> exactAlgorithms[] = new Algorithm[] {
				new Java01LPalgorithm(Java01LPalgorithm.ILPSolver.SAT4J), 
				new JavaSAT01LPalgorithm(JavaSAT01LPalgorithm.ILPSolver.SAT4J), 
				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, Heuristics.heuristicB,UnassignedVariableSelectors.maximaxUVS),
				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, Heuristics.heuristicB,UnassignedVariableSelectors.maximaxUVS)
		};
		CSAalgorithm approxAlgorithmHB = CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB());
		CSAalgorithm approxAlgorithmHA = CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA());

		List<FeatureModel> models = SPLOTRepository.getModels(0, 882);
		List<BasicProblem> instances=ProblemGenerator.generateValidBasicProblems(models,0);
		
		int count = 0;
		int counts[] = new int[3];
		for (BasicProblem instance : instances) {
			System.out.println(count);
			count++;
			
			double values[]=new double[exactAlgorithms.length];
			Configuration conf;
			for (int i=0;i<exactAlgorithms.length;i++) {
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
			
			for (int i=1;i<exactAlgorithms.length;i++) {
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
					System.out.println(aproxValueHB+ " "+ aproxValueHA);
					System.out.println(confB);
					System.out.println(confA);
					System.out.println("Sum: "+DoubleStream.of(instance.objectiveFunctions[0].attributes).sum());
					System.out.println(instance.toString());
				}
			}
		}
		
		System.out.println("HB>HA: "+counts[0]+ " HB==HA: "+counts[1]+" HB<HA: "+counts[2]);

	}
	
	@Test
	@Ignore
	public void testAdmissibleHeuristic() throws FeatureModelException {
		HeuristicB heuristicB = new HeuristicB();
		HeuristicA heuristicA = new HeuristicA();
		
		List<FeatureModel> models = SPLOTRepository.getModels(0, 882);
		List<BasicProblem> instances=ProblemGenerator.generateValidBasicProblems(models,0);
		
		int count = 0;
//		int counts[] = new int[3];
		for (BasicProblem instance : instances) {
			System.out.println(count);
			count++;
			
			Configuration partialConfiguration = ConstraintPropagators.clauseBasedConstraintPropagator.getPartialConfiguration(instance.model);
			heuristicA.setup(instance);
			double evaluationA = heuristicA.evaluate(partialConfiguration);
			heuristicB.setup(instance);
			double evaluationB = heuristicB.evaluate(partialConfiguration);
		
			Assert.assertTrue(evaluationA <= evaluationB);
			
//			if(evaluationB==evaluationA){
//				counts[1]+=1;
//			}else{
//				if(evaluationB>evaluationA){
//					counts[0]+=1;
//				}else{
//					counts[2]+=1;
//					System.out.println(evaluationA+ " "+ evaluationB);
//					System.out.println(partialConfiguration);
//					System.out.println("Sum: "+DoubleStream.of(instance.objectiveFunctions[0].attributes).sum());
//					System.out.println(instance.toString());
//				}
//			}
		}
		
//		System.out.println("HB>HA: "+counts[0]+ " HB==HA: "+counts[1]+" HB<HA: "+counts[2]);
	}
}
