package edu.isistan.fmframework.evaluation;

import java.io.IOException;
import java.util.Arrays;

import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.opt01LP.Java01LPalgorithm;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicA;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicB;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxHeuristicValue;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxValuePerWeight;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MostConstrainedFeature;
import edu.isistan.fmframework.optimization.optGAFES.GAFESalgorithm;
import edu.isistan.fmframework.optimization.optSAT.JavaSAT01LPalgorithm;
import edu.isistan.fmframework.optimization.optSPLConfig.SPLConfigAlgorithm;
import edu.isistan.fmframework.utils.CSVUtils;
import edu.isistan.fmframework.utils.StatsUtils;
import fm.FeatureModelException;

public class Experiments {

	public static void main(String[] args) throws IOException, FeatureModelException {
//		experiment1();
//		experiment2();
		experiment3();
	}

	private static void experiment1() throws IOException {

		final int NUM_INSTANCES_PER_SAMPLE = 200;

		Algorithm<Problem> algs[] = new Algorithm[] {

				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(), new MaxHeuristicValue(new HeuristicA())),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(), new MaxHeuristicValue(new HeuristicB())),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(), new MostConstrainedFeature()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(), new MostConstrainedFeature()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(), new MaxValuePerWeight()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(), new MaxValuePerWeight()),

				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, new HeuristicB(),
						new MaxHeuristicValue(new HeuristicB())),
				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, new HeuristicB(),
						new MaxHeuristicValue(new HeuristicB())),

				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, new HeuristicB(), new MostConstrainedFeature()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, new HeuristicB(), new MostConstrainedFeature()),

				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, new HeuristicB(), new MaxValuePerWeight()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, new HeuristicB(), new MaxValuePerWeight()),

				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, new HeuristicA(),
						new MaxHeuristicValue(new HeuristicA())),
				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, new HeuristicA(),
						new MaxHeuristicValue(new HeuristicA())),

				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, new HeuristicA(), new MostConstrainedFeature()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, new HeuristicA(), new MostConstrainedFeature()),

				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, new HeuristicA(), new MaxValuePerWeight()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, new HeuristicA(), new MaxValuePerWeight()), };

		boolean algB[] = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true, false,
				false, true, true, false, false };
		boolean algC[] = new boolean[] { true, true, true, true, true, true, true, true, true, true, true, true, false,
				false, false, false, false, false };
		boolean algD[] = new boolean[] { true, true, true, true, true, true, true, true, false, false, false, false,
				false, false, false, false, false, false };
		boolean algE[] = new boolean[] { true, true, true, true, true, true, false, false, false, false, false, false,
				false, false, false, false, false, false };

		String[] algnames = null;
		algnames = new String[algs.length + 1];
		algnames[0] = "Num features";
		for (int a = 0; a < algs.length; a++) {
			algnames[a + 1] = algs[a].getName();
		}

		String fileaverages = "Results/Exp1/Exp1-" + NUM_INSTANCES_PER_SAMPLE + "xStep-average.csv";
		String filedesvest = "Results/Exp1/Exp1-" + NUM_INSTANCES_PER_SAMPLE + "xStep-desvest.csv";
		String fileall = "Results/Exp1/Exp1-" + NUM_INSTANCES_PER_SAMPLE + "xStep.csv";

		CSVUtils.appendRow(fileaverages, algnames);
		CSVUtils.appendRow(filedesvest, algnames);
		CSVUtils.appendRow(fileall, algnames);
		System.out.println(Arrays.toString(algnames));

		String[] algaverage = new String[algs.length + 1];
		String[] algdesvest = new String[algs.length + 1];
		String[] algall = new String[algs.length + 1];
		for (int f = 0; f < 60; f += 10) {
			long times[][] = new long[algs.length][NUM_INSTANCES_PER_SAMPLE];
			for (int e = 0; e < NUM_INSTANCES_PER_SAMPLE; e++) {
				Problem instance;
				if (f == 0)
					instance = BasicProblemGenerator.generateBasicProblemInstance(10, 0);
				else
					instance = BasicProblemGenerator.generateBasicProblemInstance(f, 0);
				for (int a = 0; a < algs.length; a++) {
					algs[a].preprocessInstance(instance);
					// System.gc();
					long start = System.nanoTime();
					algs[a].selectConfiguration(instance);
					times[a][e] = System.nanoTime() - start;
				}
			}
			algaverage[0] = Integer.toString(f);
			algdesvest[0] = Integer.toString(f);
			algall[0] = Integer.toString(f);

			for (int a = 0; a < algs.length; a++) {
				double average = StatsUtils.averageL(times[a]) / 1000000.0;
				double desvest = StatsUtils.deviationL(times[a]) / 1000000.0;
				algaverage[a + 1] = Double.toString(average);
				algdesvest[a + 1] = Double.toString(desvest);
				algall[a + 1] = String.format("%.4f", average) + " ± " + String.format("%.4f", desvest);
			}
			CSVUtils.appendRow(fileaverages, algaverage);
			CSVUtils.appendRow(filedesvest, algdesvest);
			CSVUtils.appendRow(fileall, algall);
			System.out.println(Arrays.toString(algaverage));
			System.out.println(Arrays.toString(algdesvest));
			System.out.println(Arrays.toString(algall));
		}

		for (int f = 60; f < 110; f += 10) {

			long times[][] = new long[algs.length][NUM_INSTANCES_PER_SAMPLE];
			for (int e = 0; e < NUM_INSTANCES_PER_SAMPLE; e++) {
				Problem instance = BasicProblemGenerator.generateBasicProblemInstance(f, 0);
				for (int a = 0; a < algB.length; a++) {
					if (algB[a]) {
						algs[a].preprocessInstance(instance);
						// System.gc();
						long start = System.nanoTime();
						algs[a].selectConfiguration(instance);
						times[a][e] = System.nanoTime() - start;
					} else {
						times[a][e] = 0;
					}
				}
			}
			algaverage[0] = Integer.toString(f);
			algdesvest[0] = Integer.toString(f);
			algall[0] = Integer.toString(f);
			for (int a = 0; a < algB.length; a++) {
				double average = StatsUtils.averageL(times[a]) / 1000000.0;
				double desvest = StatsUtils.deviationL(times[a]) / 1000000.0;
				algaverage[a + 1] = Double.toString(average);
				algdesvest[a + 1] = Double.toString(desvest);
				algall[a + 1] = String.format("%.4f", average) + " ± " + String.format("%.4f", desvest);
			}

			CSVUtils.appendRow(fileaverages, algaverage);
			CSVUtils.appendRow(filedesvest, algdesvest);
			CSVUtils.appendRow(fileall, algall);
			System.out.println(Arrays.toString(algaverage));
			System.out.println(Arrays.toString(algdesvest));
			System.out.println(Arrays.toString(algall));
		}

		for (int f = 110; f < 150; f += 10) {

			long times[][] = new long[algs.length][NUM_INSTANCES_PER_SAMPLE];
			for (int e = 0; e < NUM_INSTANCES_PER_SAMPLE; e++) {
				Problem instance = BasicProblemGenerator.generateBasicProblemInstance(f, 0);
				for (int a = 0; a < algC.length; a++) {
					if (algC[a]) {
						algs[a].preprocessInstance(instance);
						// System.gc();
						long start = System.nanoTime();
						algs[a].selectConfiguration(instance);
						times[a][e] = System.nanoTime() - start;
					} else {
						times[a][e] = 0;
					}
				}
			}
			algaverage[0] = Integer.toString(f);
			algdesvest[0] = Integer.toString(f);
			algall[0] = Integer.toString(f);
			for (int a = 0; a < algC.length; a++) {
				double average = StatsUtils.averageL(times[a]) / 1000000.0;
				double desvest = StatsUtils.deviationL(times[a]) / 1000000.0;
				algaverage[a + 1] = Double.toString(average);
				algdesvest[a + 1] = Double.toString(desvest);
				algall[a + 1] = String.format("%.4f", average) + " ± " + String.format("%.4f", desvest);
			}

			CSVUtils.appendRow(fileaverages, algaverage);
			CSVUtils.appendRow(filedesvest, algdesvest);
			CSVUtils.appendRow(fileall, algall);
			System.out.println(Arrays.toString(algaverage));
			System.out.println(Arrays.toString(algdesvest));
			System.out.println(Arrays.toString(algall));
		}

		for (int f = 150; f < 250; f += 10) {

			long times[][] = new long[algs.length][NUM_INSTANCES_PER_SAMPLE];
			for (int e = 0; e < NUM_INSTANCES_PER_SAMPLE; e++) {
				Problem instance = BasicProblemGenerator.generateBasicProblemInstance(f, 0);
				for (int a = 0; a < algD.length; a++) {
					if (algD[a]) {
						algs[a].preprocessInstance(instance);
						// System.gc();
						long start = System.nanoTime();
						algs[a].selectConfiguration(instance);
						times[a][e] = System.nanoTime() - start;
					} else {
						times[a][e] = 0;
					}
				}
			}
			algaverage[0] = Integer.toString(f);
			algdesvest[0] = Integer.toString(f);
			algall[0] = Integer.toString(f);
			for (int a = 0; a < algD.length; a++) {
				double average = StatsUtils.averageL(times[a]) / 1000000.0;
				double desvest = StatsUtils.deviationL(times[a]) / 1000000.0;
				algaverage[a + 1] = Double.toString(average);
				algdesvest[a + 1] = Double.toString(desvest);
				algall[a + 1] = String.format("%.4f", average) + " ± " + String.format("%.4f", desvest);
			}

			CSVUtils.appendRow(fileaverages, algaverage);
			CSVUtils.appendRow(filedesvest, algdesvest);
			CSVUtils.appendRow(fileall, algall);
			System.out.println(Arrays.toString(algaverage));
			System.out.println(Arrays.toString(algdesvest));
			System.out.println(Arrays.toString(algall));
		}

		for (int f = 250; f < 520; f += 10) {

			long times[][] = new long[algs.length][NUM_INSTANCES_PER_SAMPLE];
			for (int e = 0; e < NUM_INSTANCES_PER_SAMPLE; e++) {
				Problem instance = BasicProblemGenerator.generateBasicProblemInstance(f, 0);
				for (int a = 0; a < algE.length; a++) {
					if (algE[a]) {
						algs[a].preprocessInstance(instance);
						// System.gc();
						long start = System.nanoTime();
						algs[a].selectConfiguration(instance);
						times[a][e] = System.nanoTime() - start;
					} else {
						times[a][e] = 0;
					}
				}
			}
			algaverage[0] = Integer.toString(f);
			algdesvest[0] = Integer.toString(f);
			algall[0] = Integer.toString(f);
			for (int a = 0; a < algE.length; a++) {
				double average = StatsUtils.averageL(times[a]) / 1000000.0;
				double desvest = StatsUtils.deviationL(times[a]) / 1000000.0;
				algaverage[a + 1] = Double.toString(average);
				algdesvest[a + 1] = Double.toString(desvest);
				algall[a + 1] = String.format("%.4f", average) + " ± " + String.format("%.4f", desvest);
			}

			CSVUtils.appendRow(fileaverages, algaverage);
			CSVUtils.appendRow(filedesvest, algdesvest);
			CSVUtils.appendRow(fileall, algall);
			System.out.println(Arrays.toString(algaverage));
			System.out.println(Arrays.toString(algdesvest));
			System.out.println(Arrays.toString(algall));
		}
	}

	private static void experiment2() throws FeatureModelException, IOException {

		final int rounds = 1;

		Algorithm<BasicProblem> algorithms[] = new Algorithm[] {
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(), new MaxHeuristicValue(new HeuristicA())),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(), new MaxHeuristicValue(new HeuristicB())),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(), new MostConstrainedFeature()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(), new MostConstrainedFeature()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicA(), new MaxValuePerWeight()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BT, new HeuristicB(), new MaxValuePerWeight()),

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

	private static void experiment3() throws FeatureModelException, IOException {

		final int rounds = 1;

		Algorithm<BasicProblem> algorithms[] = new Algorithm[] {

				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, new HeuristicB(), new MostConstrainedFeature()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BandB, new HeuristicB(), new MaxValuePerWeight()), CSAalgorithm
						.build(CSAalgorithm.Strategy.BandB, new HeuristicB(), new MaxHeuristicValue(new HeuristicB())),

				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, new HeuristicB(), new MostConstrainedFeature()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, new HeuristicB(), new MaxValuePerWeight()),
				CSAalgorithm.build(CSAalgorithm.Strategy.BestFS, new HeuristicB(),
						new MaxHeuristicValue(new HeuristicB())),

				new Java01LPalgorithm(), new JavaSAT01LPalgorithm() };

		Algorithm<BasicProblem> algorithms2[] = new Algorithm[] { algorithms[1], algorithms[2], algorithms[4],
				algorithms[5], algorithms[6], algorithms[7] };

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
