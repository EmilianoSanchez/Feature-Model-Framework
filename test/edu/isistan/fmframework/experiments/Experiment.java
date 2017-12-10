package edu.isistan.fmframework.experiments;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVWriter;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.utils.CSVUtils;
import edu.isistan.fmframework.utils.StatsUtils;
import edu.isistan.fmframework.utils.TimeMeter;

public class Experiment<P extends Problem<?,?>> {

	public Algorithm<P> algorithms[];
	public P instances[];
	public InstanceMetric<P> instanceMetrics[];
	
	public long[][] responseTime;
	public double[][] values;
	public boolean[][] correctness;
//	public long[][] expandedStates;
//	public long[][] maxFrontier;
	
	public Experiment(Algorithm<P>[] algs, P[] instances) {
		this(algs,instances,new InstanceMetric[]{InstanceMetrics.FM_NAME});
	}
	
	public Experiment(Algorithm<P>[] algs, List<P> instances) {
		this(algs,instances,new InstanceMetric[]{InstanceMetrics.FM_NAME});
	}
	
	public Experiment(Algorithm<P>[] algs, List<P> instances, InstanceMetric<P>[] instanceMetrics) {
		this(algs,(P[]) instances.toArray(new Problem[instances.size()]),instanceMetrics);
	}
	
	public Experiment(Algorithm<P>[] algs, P[] instances, InstanceMetric<P>[] instanceMetrics) {
		this.algorithms =algs;
		this.instances=instances;
		this.instanceMetrics=instanceMetrics;	
	}
	
	public void setProblemInstances(List<P> instances) {
		this.instances = (P[]) instances.toArray(new Problem[instances.size()]);
	}
	
	public void setProblemInstances(P ...instances) {
		this.instances = instances;
	}
	
	public void setAlgorithms(Algorithm<P> ...algorithms ) {
		this.algorithms = algorithms;
	}
	
	public void setInstanceMetrics(InstanceMetric<P> ...instanceMetrics) {
		this.instanceMetrics = instanceMetrics;
	}

	public void execute() {
		if (instances != null && algorithms != null) {
			responseTime = new long[instances.length][algorithms.length];
			values = new double[instances.length][algorithms.length];
			correctness = new boolean[instances.length][algorithms.length];

			for (int i = 0; i < instances.length; i++) {
//				if (i % 100 == 0)
					System.out.println((i + 1) + " of " + instances.length);

				for (int a = 0; a < algorithms.length; a++) {
					algorithms[a].preprocessInstance(instances[i]);

					long start = System.nanoTime();
					// System.gc();
					Configuration conf = algorithms[a].selectConfiguration(instances[i]);
					
					long end = System.nanoTime();

					responseTime[i][a] = end - start;
					
					if(conf != null){
						correctness[i][a] = instances[i].isSatisfied(conf);
						values[i][a] = instances[i].evaluateObjectives(conf)[0];
						
					}else{
//						System.out.println(algorithms[a].getName());
						correctness[i][a] = false;
						values[i][a] = 0.0;
					}

				}
			}
		}
	}



	public void printStats(PrintStream output) {
		for (int a = 0; a < algorithms.length; a++) {
			printStats(output, a);
		}
	}

	private void printStats(PrintStream output, int algorithm) {
		output.println(algorithms[algorithm].getName() + ":");
		if (this.responseTime != null) {
			List<Long> times = new ArrayList<Long>(this.responseTime.length);
			List<Double> values = new ArrayList<Double>(this.values.length);
			List<Boolean> corrects = new ArrayList<Boolean>(this.correctness.length);
			for (int i = 0; i < this.instances.length; i++) {
				times.add(this.responseTime[i][algorithm]);
				values.add(this.values[i][algorithm]);
				corrects.add(this.correctness[i][algorithm]);
			}

			output.println("Average time: " + StatsUtils.averageL(times));
			output.println("Average values: " + StatsUtils.averageD(values));
			output.println("Average correctness: " + StatsUtils.averageB(corrects));
		}
//		if (this.expandedStates != null) {
//			List<Long> expanded = new ArrayList<Long>(this.expandedStates.length);
//			List<Long> maxfront = new ArrayList<Long>(this.maxFrontier.length);
//			for (int i = 0; i < this.instances.length; i++) {
//				expanded.add(this.expandedStates[i][algorithm]);
//				maxfront.add(this.maxFrontier[i][algorithm]);
//			}
//
//			output.println("Average expanded states: " + StatsUtils.averageL(expanded));
//			output.println("Average max frontier: " + StatsUtils.averageL(maxfront));
//		}
	}
	
	public void saveResults(String filePath) throws IOException {
		saveResults(filePath, TimeMeter.Precision.MILLI);
	};

	public void saveResults(String filePath, TimeMeter.Precision precision) throws IOException {
		if (this.responseTime != null) {
			long divider = 1;
			switch (precision) {
			case NANO:
				divider = 1;
				break;
			case MICRO:
				divider = 1000;
				break;
			case MILLI:
				divider = 1000000;
				break;
			case SECOND:
				divider = 1000000000;
				break;
			}
			// System.out.println(divider);

			CSVWriter writer = CSVUtils.newCSVWriter(filePath + "-ResponseTime.csv");
			CSVWriter writer2 = CSVUtils.newCSVWriter(filePath + "-Values.csv");
			CSVWriter writer3 = CSVUtils.newCSVWriter(filePath + "-Correctness.csv");
			List<String> header = new LinkedList<String>();

			if (this.instanceMetrics != null)
				for (InstanceMetric<P> metric : this.instanceMetrics) {
					header.add(metric.getName());
				}

			if (this.algorithms != null)
				for (Algorithm<P> algorithm : this.algorithms) {
					header.add(algorithm.getName());
				}

			writer.writeNext(header.toArray(new String[header.size()]));
			writer2.writeNext(header.toArray(new String[header.size()]));
			writer3.writeNext(header.toArray(new String[header.size()]));
			for (int i = 0; i < instances.length; i++) {
				List<String> responsetime = new LinkedList<String>();
				List<String> value = new LinkedList<String>();
				List<String> correct = new LinkedList<String>();

				if (this.instanceMetrics != null)
					for (InstanceMetric<P> metric : this.instanceMetrics) {
						String measure = metric.getInstanceMeasure(instances[i]).toString();
						responsetime.add(measure);
						value.add(measure);
						correct.add(measure);
					}

				if (this.algorithms != null)
					for (int a = 0; a < algorithms.length; a++) {
						responsetime.add(Double.toString(((double) this.responseTime[i][a]) / ((double) divider)));
						value.add(Double.toString(this.values[i][a]));
						if (this.correctness[i][a])
							correct.add("1");
						else
							correct.add("0");
					}
				writer.writeNext(responsetime.toArray(new String[responsetime.size()]));
				writer2.writeNext(value.toArray(new String[value.size()]));
				writer3.writeNext(correct.toArray(new String[correct.size()]));
			}
			writer.close();
			writer2.close();
			writer3.close();
		}
//		if (this.expandedStates != null) {
//			CSVWriter writer = new CSVWriter(new FileWriter(new File(filePath + "-ExpandedStates.csv")));
//			CSVWriter writer2 = new CSVWriter(new FileWriter(new File(filePath + "-MaxFrontier.csv")));
//			List<String> header = new LinkedList<String>();
//
//			if (this.instanceMetrics != null)
//				for (InstanceMetric<Instance> metric : this.instanceMetrics) {
//					header.add(metric.getName());
//				}
//
//			if (this.algorithms != null)
//				for (ConfigurationSelectionAlgorithm<Instance> algorithm : this.algorithms) {
//					header.add(algorithm.getName());
//				}
//
//			writer.writeNext(header.toArray(new String[header.size()]));
//			writer2.writeNext(header.toArray(new String[header.size()]));
//			for (int i = 0; i < instances.length; i++) {
//				List<String> expanded = new LinkedList<String>();
//				List<String> frontier = new LinkedList<String>();
//
//				if (this.instanceMetrics != null)
//					for (InstanceMetric<Instance> metric : this.instanceMetrics) {
//						String measure = metric.getInstanceMeasure(instances[i]).toString();
//						expanded.add(measure);
//						frontier.add(measure);
//					}
//
//				if (this.algorithms != null)
//					for (int a = 0; a < algorithms.length; a++) {
//						expanded.add(Long.toString(this.expandedStates[i][a]));
//						frontier.add(Long.toString(this.maxFrontier[i][a]));
//					}
//				writer.writeNext(expanded.toArray(new String[expanded.size()]));
//				writer2.writeNext(frontier.toArray(new String[frontier.size()]));
//			}
//			writer.close();
//			writer2.close();
//		}
	};

}
