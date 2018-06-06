package edu.isistan.fmframework.evaluation;

import java.io.IOException;

import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicMO;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicTC;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxHeuristicValue;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxValuePerWeight;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MostConstrainedFeature;
import edu.isistan.fmframework.optimization.optGAFES.GAFESalgorithm;
import edu.isistan.fmframework.optimization.optSPLConfig.SPLConfigAlgorithm;
import fm.FeatureModelException;

public class Experiment_2 {

	public static void main(String[] args) throws FeatureModelException, IOException {

		final int rounds = 1;

		Algorithm<BasicProblem> algorithms[] = new Algorithm[] {
				CSAalgorithm.build("BT+HMO+MHV", CSAalgorithm.Strategy.BT, new HeuristicMO(),
						new MaxHeuristicValue(new HeuristicMO())),
				CSAalgorithm.build("BT+HTC+MHV", CSAalgorithm.Strategy.BT, new HeuristicTC(),
						new MaxHeuristicValue(new HeuristicTC())),
				CSAalgorithm.build("BT+HMO+MCF", CSAalgorithm.Strategy.BT, new HeuristicMO(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BT+HTC+MCF", CSAalgorithm.Strategy.BT, new HeuristicTC(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BT+HMO+MVW", CSAalgorithm.Strategy.BT, new HeuristicMO(), new MaxValuePerWeight()),
				CSAalgorithm.build("BT+HTC+MVW", CSAalgorithm.Strategy.BT, new HeuristicTC(), new MaxValuePerWeight()),

				new GAFESalgorithm(), new SPLConfigAlgorithm() };

		Experiment experiment = new Experiment(algorithms, null);

		experiment.instances = SPLOTModels.generateValidBasicProblems(rounds, SPLOTModels.getModels(0, 882),
				new double[] { 2.0 });
		experiment.executeAprox();
		experiment.saveResults("Results/Exp2/Exp2-Sample100");

		experiment.instances = SPLOTModels.generateValidBasicProblems(rounds, SPLOTModels.getModels(0, 882),
				new double[] { 0.9 });
		experiment.executeAprox();
		experiment.saveResults("Results/Exp2/Exp2-Sample90");

		experiment.instances = SPLOTModels.generateValidBasicProblems(rounds, SPLOTModels.getModels(0, 882),
				new double[] { 0.8 });
		experiment.executeAprox();
		experiment.saveResults("Results/Exp2/Exp2-Sample80");

		experiment.instances = SPLOTModels.generateValidBasicProblems(rounds, SPLOTModels.getModels(0, 882),
				new double[] { 0.7 });
		experiment.executeAprox();
		experiment.saveResults("Results/Exp2/Exp2-Sample70");

	}

}
