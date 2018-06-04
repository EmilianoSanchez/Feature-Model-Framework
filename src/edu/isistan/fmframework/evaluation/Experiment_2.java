package edu.isistan.fmframework.evaluation;

import java.io.IOException;

import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicA;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicB;
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
				CSAalgorithm.build("BT+HA+MHV", CSAalgorithm.Strategy.BT, new HeuristicA(),
						new MaxHeuristicValue(new HeuristicA())),
				CSAalgorithm.build("BT+HB+MHV", CSAalgorithm.Strategy.BT, new HeuristicB(),
						new MaxHeuristicValue(new HeuristicB())),
				CSAalgorithm.build("BT+HA+MCF", CSAalgorithm.Strategy.BT, new HeuristicA(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BT+HB+MCF", CSAalgorithm.Strategy.BT, new HeuristicB(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BT+HA+MVW", CSAalgorithm.Strategy.BT, new HeuristicA(), new MaxValuePerWeight()),
				CSAalgorithm.build("BT+HB+MVW", CSAalgorithm.Strategy.BT, new HeuristicB(), new MaxValuePerWeight()),

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
