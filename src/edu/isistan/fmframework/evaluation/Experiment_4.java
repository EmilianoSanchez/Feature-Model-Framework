package edu.isistan.fmframework.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicA;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicB;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxHeuristicValue;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxValuePerWeight;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MostConstrainedFeature;
import edu.isistan.fmframework.optimization.optRLT_01LP.Java_RLT_01LPalgorithm;
import edu.isistan.fmframework.utils.CSVUtils;
import edu.isistan.fmframework.utils.StatsUtils;
import fm.FeatureModelException;
import net.sf.javailp.OptType;

public class Experiment_4 {

	public static void main(String[] args) throws FeatureModelException, IOException {

		run_experiment_sample(FOLDER_EXP4_SAMPLE_1, FOLDER_SPLOT_MODELS_THOR_OUTPUT_SAMPLE_1);
		run_experiment_sample(FOLDER_EXP4_SAMPLE_2, FOLDER_SPLOT_MODELS_THOR_OUTPUT_SAMPLE_2);

	}

	private static String FOLDER_EXP4_SAMPLE_1 = "Results/Exp4/Exp4-Sample1";
	private static String FOLDER_SPLOT_MODELS_THOR_OUTPUT_SAMPLE_1 = "Models/Thor-attributes-Sample1";
	private static String FOLDER_EXP4_SAMPLE_2 = "Results/Exp4/Exp4-Sample2";
	private static String FOLDER_SPLOT_MODELS_THOR_OUTPUT_SAMPLE_2 = "Models/Thor-attributes-Sample2";

	static Algorithm<Problem<?, ?>> algWorseSolution = new Java_RLT_01LPalgorithm(OptType.MAX);
	static Algorithm<Problem<?, ?>> algBestSolution = new Java_RLT_01LPalgorithm(OptType.MIN);

	static Algorithm<Problem<?, ?>> algorithms[] = new Algorithm[] {

			CSAalgorithm.build("BT+HA+MHV", CSAalgorithm.Strategy.BT, new HeuristicA(),
					new MaxHeuristicValue(new HeuristicA())),
			CSAalgorithm.build("BT+HB+MHV", CSAalgorithm.Strategy.BT, new HeuristicB(),
					new MaxHeuristicValue(new HeuristicB())),
			CSAalgorithm.build("BT+HA+MCF", CSAalgorithm.Strategy.BT, new HeuristicA(), new MostConstrainedFeature()),
			CSAalgorithm.build("BT+HB+MCF", CSAalgorithm.Strategy.BT, new HeuristicB(), new MostConstrainedFeature()),
			CSAalgorithm.build("BT+HA+MVW", CSAalgorithm.Strategy.BT, new HeuristicA(), new MaxValuePerWeight()),
			CSAalgorithm.build("BT+HB+MVW", CSAalgorithm.Strategy.BT, new HeuristicB(), new MaxValuePerWeight()),

			CSAalgorithm.build("BandB+HB+MCF", CSAalgorithm.Strategy.BandB, new HeuristicB(),
					new MostConstrainedFeature()),
			CSAalgorithm.build("BandB+HB+MVW", CSAalgorithm.Strategy.BandB, new HeuristicB(), new MaxValuePerWeight()),
			CSAalgorithm.build("BandB+HB+MHV", CSAalgorithm.Strategy.BandB, new HeuristicB(),
					new MaxHeuristicValue(new HeuristicB())),
			CSAalgorithm.build("BestFS+HB+MCF", CSAalgorithm.Strategy.BestFS, new HeuristicB(),
					new MostConstrainedFeature()),
			CSAalgorithm.build("BestFS+HB+MVW", CSAalgorithm.Strategy.BestFS, new HeuristicB(),
					new MaxValuePerWeight()),
			CSAalgorithm.build("BestFS+HB+MHV", CSAalgorithm.Strategy.BestFS, new HeuristicB(),
					new MaxHeuristicValue(new HeuristicB())),

			new Java_RLT_01LPalgorithm() };

	private static void run_experiment_sample(String output_folder, String thor_attributes_folder)
			throws FeatureModelException, IOException {

		String[] algnames = null;
		algnames = new String[algorithms.length + 1];
		algnames[0] = "Model(Num features)";
		for (int a = 0; a < algorithms.length; a++) {
			algnames[a + 1] = algorithms[a].getName();
		}

		File directory = new File(output_folder);
		if (!directory.exists()) {
			directory.mkdir();
		}

		String filetimeaverages = output_folder + "/Time-average.csv";
		String filetimedesvest = output_folder + "/Time-desvest.csv";
		String filetime = output_folder + "/Time.csv";
		String filetimeall = output_folder + "/Time-allResults.csv";
		String fileoptimalityaverages = output_folder + "/Optimality-average.csv";
		String fileoptimalitydesvest = output_folder + "/Optimality-desvest.csv";
		String fileoptimality = output_folder + "/Optimality.csv";
		String fileoptimalityall = output_folder + "/Optimality-allResults.csv";
		// String filevaluesall = output_folder + "/exp4-allValues.csv";

		CSVUtils.appendRow(filetimeaverages, algnames);
		CSVUtils.appendRow(filetimedesvest, algnames);
		CSVUtils.appendRow(filetime, algnames);
		CSVUtils.appendRow(filetimeall, algnames);
		CSVUtils.appendRow(fileoptimalityaverages, algnames);
		CSVUtils.appendRow(fileoptimalitydesvest, algnames);
		CSVUtils.appendRow(fileoptimality, algnames);
		CSVUtils.appendRow(fileoptimalityall, algnames);
		// CSVUtils.appendRow(filevaluesall, algnames);
		System.out.println(Arrays.toString(algnames));

		String[] algtimeaverage = new String[algorithms.length + 1];
		String[] algtimedesvest = new String[algorithms.length + 1];
		String[] algtime = new String[algorithms.length + 1];
		String[] algtimeall = new String[algorithms.length + 1];
		String[] algoptimalityaverages = new String[algorithms.length + 1];
		String[] algoptimalitydesvest = new String[algorithms.length + 1];
		String[] algoptimality = new String[algorithms.length + 1];
		String[] algoptimalityall = new String[algorithms.length + 1];
		String[] algvaluesall = new String[algorithms.length + 1];

		Configuration conf;
		double worseSolution, bestSolution;

		Problem<?, ?>[] instances = SPLOTModels.generateValidProblemInstancesWithThorAttributes(1,
				SPLOTModels.getModels(0, 882), SPLOTModels.getModelsThorObjectives(thor_attributes_folder), 0)[0];

		long times[][] = new long[algorithms.length][instances.length];
		double optimality[][] = new double[algorithms.length][instances.length];
		double values[][] = new double[algorithms.length][instances.length];

		for (int e = 0; e < instances.length; e++) {
			Problem<?, ?> instance = instances[e];

			algtimeall[0] = algoptimalityall[0] = algvaluesall[0] = instance.model.getName() + "("
					+ instance.model.getNumFeatures() + ")";

			algWorseSolution.preprocessInstance(instance);
			conf = algWorseSolution.selectConfiguration(instance);
			worseSolution = instance.objectiveFunctions[0].evaluate(conf);
			algBestSolution.preprocessInstance(instance);
			conf = algBestSolution.selectConfiguration(instance);
			bestSolution = instance.objectiveFunctions[0].evaluate(conf);

			// int contadorNullConf=0;
			for (int a = 0; a < algorithms.length; a++) {

				System.out.println(output_folder + " instance: " + e + " algorithm: " + a);

				algorithms[a].preprocessInstance(instance);
				// System.gc();
				long start = System.nanoTime();
				conf = algorithms[a].selectConfiguration(instance);
				// if(conf==null)
				// contadorNullConf++;
				times[a][e] = System.nanoTime() - start;
				algtimeall[a + 1] = Double.toString(((double) times[a][e]) / 1000000.0);

				values[a][e] = instance.objectiveFunctions[0].evaluate(conf);

				// System.out.println(values[a][e]);
				// System.out.println(conf);

				algvaluesall[a + 1] = Double.toString(values[a][e]);

				double abs = Math.abs(worseSolution - bestSolution);
				if (abs == 0.0)
					optimality[a][e] = 1.0;
				else
					optimality[a][e] = Math.abs(worseSolution - values[a][e]) / abs;
				algoptimalityall[a + 1] = Double.toString(optimality[a][e]);
			}
			// System.out.println(contadorNullConf);

			CSVUtils.appendRow(filetimeall, algtimeall);
			CSVUtils.appendRow(fileoptimalityall, algoptimalityall);
			// CSVUtils.appendRow(filevaluesall, algvaluesall);
		}

		algtimeaverage[0] = "NA";
		algtimedesvest[0] = "NA";
		algtime[0] = "NA";
		algoptimalityaverages[0] = "NA";
		algoptimalitydesvest[0] = "NA";
		algoptimality[0] = "NA";

		for (int a = 0; a < algorithms.length; a++) {
			double average = StatsUtils.averageL(times[a]) / 1000000.0;
			double desvest = StatsUtils.deviationL(times[a]) / 1000000.0;
			algtimeaverage[a + 1] = Double.toString(average);
			algtimedesvest[a + 1] = Double.toString(desvest);
			algtime[a + 1] = String.format("%.4f", average) + " ± " + String.format("%.4f", desvest);

			double averageOptimality = StatsUtils.averageD(optimality[a]);
			double desvestOptimality = StatsUtils.deviationD(optimality[a]);
			algoptimalityaverages[a + 1] = Double.toString(averageOptimality);
			algoptimalitydesvest[a + 1] = Double.toString(desvestOptimality);
			algoptimality[a + 1] = String.format("%.4f", averageOptimality) + " ± "
					+ String.format("%.4f", desvestOptimality);
		}

		CSVUtils.appendRow(filetimeaverages, algtimeaverage);
		CSVUtils.appendRow(filetimedesvest, algtimedesvest);
		CSVUtils.appendRow(filetime, algtime);
		CSVUtils.appendRow(fileoptimalityaverages, algoptimalityaverages);
		CSVUtils.appendRow(fileoptimalitydesvest, algoptimalitydesvest);
		CSVUtils.appendRow(fileoptimality, algoptimality);

	}

}
