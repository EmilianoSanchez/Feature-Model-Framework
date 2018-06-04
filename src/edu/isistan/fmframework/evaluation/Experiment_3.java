package edu.isistan.fmframework.evaluation;

import java.io.IOException;

import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.opt01LP.Java01LPalgorithm;
import edu.isistan.fmframework.optimization.optBoolOpt.JavaBoolOptAlgorithm;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicB;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxHeuristicValue;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxValuePerWeight;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MostConstrainedFeature;
import fm.FeatureModelException;

public class Experiment_3 {

	public static void main(String[] args) throws FeatureModelException, IOException {

		final int rounds = 1;

		Algorithm<BasicProblem> algorithms[] = new Algorithm[] {

				CSAalgorithm.build("BandB+HB+MCF", CSAalgorithm.Strategy.BandB, new HeuristicB(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BandB+HB+MVW", CSAalgorithm.Strategy.BandB, new HeuristicB(),
						new MaxValuePerWeight()),
				CSAalgorithm.build("BandB+HB+MHV", CSAalgorithm.Strategy.BandB, new HeuristicB(),
						new MaxHeuristicValue(new HeuristicB())),

				CSAalgorithm.build("BestFS+HB+MCF", CSAalgorithm.Strategy.BestFS, new HeuristicB(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BestFS+HB+MVW", CSAalgorithm.Strategy.BestFS, new HeuristicB(),
						new MaxValuePerWeight()),
				CSAalgorithm.build("BestFS+HB+MHV", CSAalgorithm.Strategy.BestFS, new HeuristicB(),
						new MaxHeuristicValue(new HeuristicB())),

				new Java01LPalgorithm(), new JavaBoolOptAlgorithm() };

		Experiment experiment = new Experiment(algorithms, null);

		experiment.instances = SPLOTModels.generateValidBasicProblems(rounds, SPLOTModels.getModels(0, 882), 0);
		experiment.executeResponseTime();
		experiment.saveResultsResponseTime("Results/Exp3/Exp3-SampleIR0");

		experiment.instances = SPLOTModels.generateValidBasicProblems(rounds, SPLOTModels.getModels(0, 882), 1);
		experiment.executeResponseTime();
		experiment.saveResultsResponseTime("Results/Exp3/Exp3-SampleIR1");

		experiment.instances = SPLOTModels.generateValidBasicProblems(rounds, SPLOTModels.getModels(0, 882), 2);
		experiment.executeResponseTime();
		experiment.saveResultsResponseTime("Results/Exp3/Exp3-SampleIR2");

		experiment.instances = SPLOTModels.generateValidBasicProblems(rounds, SPLOTModels.getModels(0, 882), 5);
		experiment.executeResponseTime();
		experiment.saveResultsResponseTime("Results/Exp3/Exp3-SampleIR5");
	}

}
