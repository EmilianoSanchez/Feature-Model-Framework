package edu.isistan.fmframework.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagator;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagators;
import edu.isistan.fmframework.optimization.optCSA.containers.Container;
import edu.isistan.fmframework.optimization.optCSA.containers.Stack;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicMO;
import edu.isistan.fmframework.optimization.optCSA.heuristicFunctions.HeuristicTC;
import edu.isistan.fmframework.optimization.optCSA.searchStrategies.State;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxHeuristicValue;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MaxValuePerWeight;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.MostConstrainedFeature;
import edu.isistan.fmframework.optimization.optCSA.variableSelectors.VariableSelector;
import edu.isistan.fmframework.utils.CSVUtils;
import edu.isistan.fmframework.utils.StatsUtils;
import fm.FeatureModelException;

public class Experiment_1_state_space {

	public static void main(String[] args) throws IOException, FeatureModelException {
		loadAlgorithms();

		// experiment1_part1_state_space();
		experiment1_part2_state_space();
	}

	static CSAalgorithm algs[];
	static CSAalgorithm algsCardinalityPropagador[];

	private static void loadAlgorithms() {
		algs = new CSAalgorithm[] {

				CSAalgorithm.build("BT+HMO+MCF", CSAalgorithm.Strategy.BT, new HeuristicMO(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BT+HMO+MVW", CSAalgorithm.Strategy.BT, new HeuristicMO(), new MaxValuePerWeight()),
				CSAalgorithm.build("BT+HTC+MCF", CSAalgorithm.Strategy.BT, new HeuristicTC(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BT+HTC+MVW", CSAalgorithm.Strategy.BT, new HeuristicTC(), new MaxValuePerWeight()),
				CSAalgorithm.build("BT+HMO+MHV", CSAalgorithm.Strategy.BT, new HeuristicMO(),
						new MaxHeuristicValue(new HeuristicMO())),
				CSAalgorithm.build("BT+HTC+MHV", CSAalgorithm.Strategy.BT, new HeuristicTC(),
						new MaxHeuristicValue(new HeuristicTC())),

				CSAalgorithm.build("BandB+HTC+MHV", CSAalgorithm.Strategy.BandB, new HeuristicTC(),
						new MaxHeuristicValue(new HeuristicTC())),
				CSAalgorithm.build("BestFS+HTC+MHV", CSAalgorithm.Strategy.BestFS, new HeuristicTC(),
						new MaxHeuristicValue(new HeuristicTC())),

				CSAalgorithm.build("BandB+HTC+MCF", CSAalgorithm.Strategy.BandB, new HeuristicTC(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BestFS+HTC+MCF", CSAalgorithm.Strategy.BestFS, new HeuristicTC(),
						new MostConstrainedFeature()),

				CSAalgorithm.build("BandB+HTC+MVW", CSAalgorithm.Strategy.BandB, new HeuristicTC(),
						new MaxValuePerWeight()),
				CSAalgorithm.build("BestFS+HTC+MVW", CSAalgorithm.Strategy.BestFS, new HeuristicTC(),
						new MaxValuePerWeight()),

				CSAalgorithm.build("BandB+HMO+MCF", CSAalgorithm.Strategy.BandB, new HeuristicMO(),
						new MostConstrainedFeature()),
				CSAalgorithm.build("BestFS+HMO+MCF", CSAalgorithm.Strategy.BestFS, new HeuristicMO(),
						new MostConstrainedFeature()),

				CSAalgorithm.build("BandB+HMO+MHV", CSAalgorithm.Strategy.BandB, new HeuristicMO(),
						new MaxHeuristicValue(new HeuristicMO())),
				CSAalgorithm.build("BestFS+HMO+MHV", CSAalgorithm.Strategy.BestFS, new HeuristicMO(),
						new MaxHeuristicValue(new HeuristicMO())),

				CSAalgorithm.build("BandB+HMO+MVW", CSAalgorithm.Strategy.BandB, new HeuristicMO(),
						new MaxValuePerWeight()),
				CSAalgorithm.build("BestFS+HMO+MVW", CSAalgorithm.Strategy.BestFS, new HeuristicMO(),
						new MaxValuePerWeight())

		};

		for (CSAalgorithm algorithm : algs) {
			algorithm.setOpenContainer(new InstrumentedContainer(algorithm.getOpenContainer()));
		}

		algsCardinalityPropagador = new CSAalgorithm[] {

				CSAalgorithm.build("BT+HMO+MCF", CSAalgorithm.Strategy.BT, new HeuristicMO(),
						new MostConstrainedFeature(), ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BT+HMO+MVW", CSAalgorithm.Strategy.BT, new HeuristicMO(), new MaxValuePerWeight(),
						ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BT+HTC+MCF", CSAalgorithm.Strategy.BT, new HeuristicTC(),
						new MostConstrainedFeature(), ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BT+HTC+MVW", CSAalgorithm.Strategy.BT, new HeuristicTC(), new MaxValuePerWeight(),
						ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BT+HMO+MHV", CSAalgorithm.Strategy.BT, new HeuristicMO(),
						new MaxHeuristicValue(new HeuristicMO()),
						ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BT+HTC+MHV", CSAalgorithm.Strategy.BT, new HeuristicTC(),
						new MaxHeuristicValue(new HeuristicTC()),
						ConstraintPropagators.cardinalityBasedConstraintPropagator),

				CSAalgorithm.build("BandB+HTC+MHV", CSAalgorithm.Strategy.BandB, new HeuristicTC(),
						new MaxHeuristicValue(new HeuristicTC()),
						ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BestFS+HTC+MHV", CSAalgorithm.Strategy.BestFS, new HeuristicTC(),
						new MaxHeuristicValue(new HeuristicTC()),
						ConstraintPropagators.cardinalityBasedConstraintPropagator),

				CSAalgorithm.build("BandB+HTC+MCF", CSAalgorithm.Strategy.BandB, new HeuristicTC(),
						new MostConstrainedFeature(), ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BestFS+HTC+MCF", CSAalgorithm.Strategy.BestFS, new HeuristicTC(),
						new MostConstrainedFeature(), ConstraintPropagators.cardinalityBasedConstraintPropagator),

				CSAalgorithm.build("BandB+HTC+MVW", CSAalgorithm.Strategy.BandB, new HeuristicTC(),
						new MaxValuePerWeight(), ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BestFS+HTC+MVW", CSAalgorithm.Strategy.BestFS, new HeuristicTC(),
						new MaxValuePerWeight(), ConstraintPropagators.cardinalityBasedConstraintPropagator),

				CSAalgorithm.build("BandB+HMO+MCF", CSAalgorithm.Strategy.BandB, new HeuristicMO(),
						new MostConstrainedFeature(), ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BestFS+HMO+MCF", CSAalgorithm.Strategy.BestFS, new HeuristicMO(),
						new MostConstrainedFeature(), ConstraintPropagators.cardinalityBasedConstraintPropagator),

				CSAalgorithm.build("BandB+HMO+MHV", CSAalgorithm.Strategy.BandB, new HeuristicMO(),
						new MaxHeuristicValue(new HeuristicMO()),
						ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BestFS+HMO+MHV", CSAalgorithm.Strategy.BestFS, new HeuristicMO(),
						new MaxHeuristicValue(new HeuristicMO()),
						ConstraintPropagators.cardinalityBasedConstraintPropagator),

				CSAalgorithm.build("BandB+HMO+MVW", CSAalgorithm.Strategy.BandB, new HeuristicMO(),
						new MaxValuePerWeight(), ConstraintPropagators.cardinalityBasedConstraintPropagator),
				CSAalgorithm.build("BestFS+HMO+MVW", CSAalgorithm.Strategy.BestFS, new HeuristicMO(),
						new MaxValuePerWeight(), ConstraintPropagators.cardinalityBasedConstraintPropagator)

		};

		for (CSAalgorithm algorithm : algsCardinalityPropagador) {
			algorithm.setOpenContainer(new InstrumentedContainer(algorithm.getOpenContainer()));
		}
	}

	static boolean[] allAlgorithms = new boolean[] { true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true };
	static boolean[] allAlgorithmsLessExactVariantsWithHMOwithMHVandMVW = new boolean[] { true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, false, false, false, false };
	static boolean[] allAlgorithmsLessExactVariantsWithHMO = new boolean[] { true, true, true, true, true, true, true,
			true, true, true, true, true, false, false, false, false, false, false };
	static boolean[] allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW = new boolean[] { true, true, true, true,
			true, true, true, true, false, false, false, false, false, false, false, false, false, false };
	static boolean[] allAlgorithmsLessExactVariants = new boolean[] { true, true, true, true, true, true, false, false,
			false, false, false, false, false, false, false, false, false, false };

	private static void experiment1_part1_state_space() throws IOException {

		ProblemGenerator[] samples_original_numFeatures_splotdist = new ProblemGenerator[45];
		for (int i = 0; i < samples_original_numFeatures_splotdist.length; i++) {
			int numFeatures = (i + 1) * 10;
			samples_original_numFeatures_splotdist[i] = new ProblemGenerator(Integer.toString(numFeatures), numFeatures)
					.setBranchingFactor(ProblemGenerator.SPLOT_BRANCHING_FACTOR)
					.setTreeConstraintDistribution(ProblemGenerator.SPLOT_TCTDISTRIBUTION)
					.setCTCRatio(ProblemGenerator.SPLOT_CTC_RATIO).setIRs(1);
		}

		boolean[][] runningalgorithms_original_numFeatures_splotdist = new boolean[][] { allAlgorithms, allAlgorithms,
				allAlgorithms, allAlgorithms, allAlgorithms, // 50
				allAlgorithmsLessExactVariantsWithHMOwithMHVandMVW, allAlgorithmsLessExactVariantsWithHMOwithMHVandMVW,
				allAlgorithmsLessExactVariantsWithHMOwithMHVandMVW, allAlgorithmsLessExactVariantsWithHMOwithMHVandMVW,
				allAlgorithmsLessExactVariantsWithHMOwithMHVandMVW, // 100
				allAlgorithmsLessExactVariantsWithHMO, allAlgorithmsLessExactVariantsWithHMO,
				allAlgorithmsLessExactVariantsWithHMO, allAlgorithmsLessExactVariantsWithHMO, // 140
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW,
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW,
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW,
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW,
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW,
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW,
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW,
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW,
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW,
				allAlgorithmsLessExactVariantsWithHMOandHTCwithMCFandMVW, // 240
				allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants,
				allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants,
				allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants,
				allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants,
				allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants,
				allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants,
				allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants, allAlgorithmsLessExactVariants // 450
		};

		run_experiment_state_space(200, samples_original_numFeatures_splotdist,
				runningalgorithms_original_numFeatures_splotdist, "Results/Exp1-Part1-state-space", algs);

	};

	private static void experiment1_part2_state_space() throws IOException {
		ProblemGenerator[] samples = new ProblemGenerator[] {
				new ProblemGenerator("OPTIONALS", 30)
						.setTreeConstraintDistribution(ProblemGenerator.SPLOT_TCTDISTRIBUTION_OR_XOR_INTO_OPTIONALS),
				new ProblemGenerator("NORMAL", 30)
						.setTreeConstraintDistribution(ProblemGenerator.SPLOT_TCTDISTRIBUTION),
				new ProblemGenerator("CARDINALITY", 30)
						.setTreeConstraintDistribution(ProblemGenerator.SPLOT_TCTDISTRIBUTION_OR_INTO_CARDINALITY),
				new ProblemGenerator("CARDINALITY+CTC0.147", 30)
						.setTreeConstraintDistribution(ProblemGenerator.SPLOT_TCTDISTRIBUTION_OR_INTO_CARDINALITY)
						.setCTCRatio(ProblemGenerator.SPLOT_CTC_RATIO),
				new ProblemGenerator("CARDINALITY+CTC0.147+IR0.9x5", 30)
						.setTreeConstraintDistribution(ProblemGenerator.SPLOT_TCTDISTRIBUTION_OR_INTO_CARDINALITY)
						.setCTCRatio(ProblemGenerator.SPLOT_CTC_RATIO).setIRs(5, 0.9), };

		for (ProblemGenerator problemGenerator : samples) {
			((ProblemGenerator) problemGenerator).setBranchingFactor(ProblemGenerator.SPLOT_BRANCHING_FACTOR);
			BasicProblem instance = problemGenerator.generateBasicProblemInstance();
		}

		boolean[][] runningalgorithms = new boolean[][] { allAlgorithms, allAlgorithms, allAlgorithms, allAlgorithms,
				allAlgorithms };

		run_experiment_state_space(200, samples, runningalgorithms, "Results/Exp1-Part2-state-space",
				algsCardinalityPropagador);
	}

	private static void run_experiment_state_space(int instances_per_sample, ProblemGenerator[] samples,
			boolean[][] runalgorithms, String folderPath, CSAalgorithm algs[]) throws IOException {

		// RandomUtils.setSeed(123121);
		int NUM_EXPERIMENTS = instances_per_sample;

		String[] algnames = null;
		algnames = new String[algs.length + 1];
		algnames[0] = "Sample name";
		for (int a = 0; a < algs.length; a++) {
			algnames[a + 1] = algs[a].getName();
		}

		File directory = new File(folderPath);
		if (!directory.exists()) {
			directory.mkdir();
		}

		String fileexpandedstatesaverage = folderPath + "/Expanded-states-average.csv";
		String filemaxfrontieraverage = folderPath + "/Max-frontier-average.csv";
		String filenumberofstatesaverage = folderPath + "/Number-of-states-average.csv";

		CSVUtils.appendRow(fileexpandedstatesaverage, algnames);
		CSVUtils.appendRow(filemaxfrontieraverage, algnames);
		CSVUtils.appendRow(filenumberofstatesaverage, new String[] { algnames[0], "Average number of states" });
		System.out.println(Arrays.toString(algnames));

		String[] algexpandedstatesaverage = new String[algs.length + 1];
		String[] algmaxfrontieraverage = new String[algs.length + 1];
		String[] algnumberofstatesaverage = new String[2];

		for (int s = 0; s < samples.length; s++) {

			ProblemGenerator generator = samples[s];
			boolean[] algorithms = runalgorithms[s];
			long expandedstates[][] = new long[algs.length][NUM_EXPERIMENTS];
			long maxfrontier[][] = new long[algs.length][NUM_EXPERIMENTS];
			long numberofstates[] = new long[NUM_EXPERIMENTS];

			for (int e = 0; e < NUM_EXPERIMENTS; e++) {
				System.out.println(e);
				BasicProblem instance = generator.generateBasicProblemInstance();
				numberofstates[e] = countNumberOfStates(instance);

				try {
					for (int a = 0; a < algs.length; a++) {
						if (algorithms[a]) {
							algs[a].preprocessInstance(instance);
							algs[a].selectConfiguration(instance);
							InstrumentedContainer<State> openContainer = (InstrumentedContainer) algs[a]
									.getOpenContainer();
							expandedstates[a][e] = openContainer.getExpandedStates();
							maxfrontier[a][e] = openContainer.getMaxFrontier();
						} else {
							expandedstates[a][e] = 0;
							maxfrontier[a][e] = 0;
						}
					}
				} catch (RuntimeException exc) {
					exc.printStackTrace();
					e--;
				}
				;
			}
			algexpandedstatesaverage[0] = generator.toString();
			algmaxfrontieraverage[0] = generator.toString();
			algnumberofstatesaverage[0] = generator.toString();

			for (int a = 0; a < algs.length; a++) {
				double expandedstatesaverage = StatsUtils.averageL(expandedstates[a]);
				double maxfrontieraverage = StatsUtils.deviationL(maxfrontier[a]);
				algexpandedstatesaverage[a + 1] = Double.toString(expandedstatesaverage);
				algmaxfrontieraverage[a + 1] = Double.toString(maxfrontieraverage);
			}
			double numberofstatesaverage = StatsUtils.averageL(numberofstates);
			algnumberofstatesaverage[1] = Double.toString(numberofstatesaverage);

			CSVUtils.appendRow(fileexpandedstatesaverage, algexpandedstatesaverage);
			CSVUtils.appendRow(filemaxfrontieraverage, algmaxfrontieraverage);
			CSVUtils.appendRow(filenumberofstatesaverage, algnumberofstatesaverage);
			System.out.println(Arrays.toString(algexpandedstatesaverage));
			System.out.println(Arrays.toString(algmaxfrontieraverage));
			System.out.println(Arrays.toString(algnumberofstatesaverage));
		}

	}

	private static VariableSelector<Problem<?, ?>> unassignedVariableSelector = new VariableSelector<Problem<?, ?>>() {
		@Override
		public int selectUnassignedVariable(Configuration conf) {
			for (int i = 0; i < conf.getNumFeatures(); i++) {
				if (conf.getFeatureState(i) == FeatureState.UNSELECTED)
					return i;
			}
			return NO_UNASSIGNED_VARIABLES;
		}

		@Override
		public void setup(Problem<?, ?> instance) {
		}
	};
	private static Stack<Configuration> open = new Stack<>();

	private static long countNumberOfStates(BasicProblem instance) {
		long stateCount = 0;
		ConstraintPropagator cpropagator = ConstraintPropagators.clauseBasedConstraintPropagator;
		Configuration initial = cpropagator.getPartialConfiguration(instance.model);
		if (initial != null && instance.satisfyGlobalConstraints(initial)) {
			unassignedVariableSelector.setup(instance);
			open.clear();
			open.push(initial);
			while (!open.isEmpty()) {
				Configuration current = open.pop();
				int unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
				if (unassignedVariable != VariableSelector.NO_UNASSIGNED_VARIABLES) {
					Configuration succesor1 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
							&& instance.satisfyGlobalConstraints(succesor1)) {
						open.push(succesor1);
						stateCount++;
					}
					Configuration succesor2 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
							&& instance.satisfyGlobalConstraints(succesor2)) {
						open.push(succesor2);
						stateCount++;
					}
				}
			}
		}
		return stateCount;
	}

	static class InstrumentedContainer<Element> implements Container<Element> {

		long expandedStates;
		long elements = 0;
		long maxFrontier;
		Container<Element> container;

		public InstrumentedContainer(Container<Element> container) {
			this.container = container;
			this.expandedStates = 0;
			this.maxFrontier = 0;
		}

		public void push(Element content) {
			expandedStates++;
			elements++;
			if (elements > maxFrontier)
				maxFrontier = elements;
			this.container.push(content);
		};

		public Element pop() {
			elements--;
			return this.container.pop();
		};

		public void clear() {
			expandedStates = 0;
			maxFrontier = 0;
			elements = 0;
			this.container.clear();
		}

		@Override
		public boolean isEmpty() {
			return this.container.isEmpty();
		}

		@Override
		public Element peek() {
			return this.container.peek();
		}

		public long getExpandedStates() {
			return expandedStates;
		}

		public long getMaxFrontier() {
			return maxFrontier;
		}

		public Container<Element> getContainer() {
			return container;
		};

	}
}
