package edu.isistan.fmframework.evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVWriter;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.objectiveFunctions.LinearWeightedObjective;
import edu.isistan.fmframework.optimization.opt01LP.Java01LPalgorithm;
import edu.isistan.fmframework.optimization.optCSA.searchStrategies.CSABestFirstSearch;
import edu.isistan.fmframework.utils.StatsUtils;
import edu.isistan.fmframework.utils.TimeMeter;
import net.sf.javailp.OptType;

public class Experiment {

	public Algorithm<BasicProblem> algorithms[];
	public BasicProblem instances[][];
	public InstanceMetric<BasicProblem> instanceMetrics[];

	public long[][][] responseTime;
	public double[][][] values;
	public double[][] bestValues;
	public double[][] worstValues;
	
	public boolean[][][] correctness;
	
	public Experiment(Algorithm<BasicProblem> algorithms[], BasicProblem instances[][],
			InstanceMetric<BasicProblem> instanceMetrics[]) {
		this.algorithms = algorithms;
		this.instances = instances;
		this.instanceMetrics = instanceMetrics;
	};
	
	public Experiment(Algorithm<BasicProblem> algorithms[], BasicProblem instances[][]) {
		this(algorithms,instances,new InstanceMetric[]{InstanceMetrics.FM_NAME});
	};
	
	public void executeResponseTime() {
		// System.gc();

		if (instances != null && algorithms != null) {
			responseTime = new long[algorithms.length][instances.length][instances[0].length];

			for (int r = 0; r < instances.length; r++) {
				
				System.out.println((r + 1) + " of " + instances.length+ " rounds");
				
				for (int i = 0; i < instances[r].length; i++) {
				
					if(i>850)
						System.out.println((i + 1) + " of " + instances[r].length+ " instances");
	
					for (int a = 0; a < algorithms.length; a++) {
						algorithms[a].preprocessInstance(instances[r][i]);
	
						if(i>850)
							System.out.println("algoritmo "+a);
						
						
						long start = System.nanoTime();
//						System.gc();
						Configuration conf = algorithms[a].selectConfiguration(instances[r][i]);
						long end = System.nanoTime();
	
						responseTime[a][r][i] = end - start;
					}
				}
			}
		}
	}


	public void executeAprox() {
		// System.gc();

		if (instances != null && algorithms != null) {
			this.responseTime = new long[algorithms.length][instances.length][instances[0].length];
			this.values = new double[algorithms.length][instances.length][instances[0].length];
			this.bestValues= new double[instances.length][instances[0].length];
			this.worstValues= new double[instances.length][instances[0].length];
			this.correctness = new boolean[algorithms.length][instances.length][instances[0].length];

			for (int r = 0; r < instances.length; r++) {
				
				System.out.println((r + 1) + " of " + instances.length+ " rounds");
				
				for (int i = 0; i < instances[r].length; i++) {
				
					if(i>850)
						System.out.println((i + 1) + " of " + instances[r].length+ " instances");
	
					
					processBestAndWorstValues(r,i);

					Configuration conf;
					for (int a = 0; a < algorithms.length; a++) {
						algorithms[a].preprocessInstance(instances[r][i]);
	
						if(i>850)
							System.out.println("algoritmo "+a);
						
						
						long start = System.nanoTime();
//						System.gc();
						conf = algorithms[a].selectConfiguration(instances[r][i]);
						long end = System.nanoTime();
	
						responseTime[a][r][i] = end - start;
						if(conf!=null){
							correctness[a][r][i] = instances[r][i].isSatisfied(conf);
							values[a][r][i] = instances[r][i].evaluateObjectives(conf)[0];
						}else{
							correctness[a][r][i] = false;
							values[a][r][i] = Double.NaN;
						}
					}
				}
			}
		}
	}
	
	private Java01LPalgorithm algbest=new Java01LPalgorithm(OptType.MAX);
	private Java01LPalgorithm algworst=new Java01LPalgorithm(OptType.MIN);

	private void processBestAndWorstValues(int r, int i) {
		algbest.preprocessInstance( instances[r][i]);
		Configuration conf = algbest.selectConfiguration(instances[r][i]);	
		this.bestValues[r][i]=instances[r][i].evaluateObjectives(conf)[0];

		algworst.preprocessInstance( instances[r][i]);
		conf = algworst.selectConfiguration( instances[r][i]);
		this.worstValues[r][i]=instances[r][i].evaluateObjectives(conf)[0];
	}
	
	public static double optimality(double found, double best, double worst){
		double optimalityValue = 0.0;
		if(Double.isNaN(found)==false && Double.isNaN(best)==false && Double.isNaN(worst)==false){
			if(best != worst){
				optimalityValue = (Math.abs(found - worst) / Math.abs(best - worst));
			}else{
				if(best!=0){
					optimalityValue = found/best;
				}
			}
		}
		return optimalityValue;
	};

	public void saveResults(String file) throws IOException {
		saveResults(file, TimeMeter.Precision.MILLI);
	}
	
	public void saveResults(String file, TimeMeter.Precision precision) throws IOException {
		if (this.responseTime != null) {
			double divider = 1.0;
			if(precision!=null)
				divider = precision.divider;

			CSVWriter writer = new CSVWriter(new FileWriter(new File(file + "-ResponseTime-Total.csv")));
			CSVWriter writer2 = new CSVWriter(new FileWriter(new File(file + "-Optimality-Total.csv")));
			CSVWriter writer3 = new CSVWriter(new FileWriter(new File(file + "-Correctness-Total.csv")));
			List<String> header = new LinkedList<String>();			

			if (this.algorithms != null)
				for (Algorithm<BasicProblem> algorithm : this.algorithms) {
					header.add(algorithm.getName());
				}

			writer.writeNext(header.toArray(new String[header.size()]));
			writer2.writeNext(header.toArray(new String[header.size()]));
			writer3.writeNext(header.toArray(new String[header.size()]));
			
			for (int r = 0; r < instances.length; r++) {
				for (int i = 0; i < instances[r].length; i++) {
					List<String> responsetime = new LinkedList<String>();
					List<String> optimality = new LinkedList<String>();
					List<String> correct = new LinkedList<String>();
	
					if (this.algorithms != null)
						for (int a = 0; a < algorithms.length; a++) {
							responsetime.add(Double.toString(this.responseTime[a][r][i] / divider));
							optimality.add(Double.toString(optimality(this.values[a][r][i],this.bestValues[r][i],this.worstValues[r][i])));
							correct.add(Boolean.toString(this.correctness[a][r][i]));
						}
					
					writer.writeNext(responsetime.toArray(new String[responsetime.size()]));
					writer2.writeNext(optimality.toArray(new String[optimality.size()]));
					writer3.writeNext(correct.toArray(new String[correct.size()]));
				}
			}
			writer.close();
			writer2.close();
			writer3.close();
		}
	}	

	public void saveResultsResponseTime(String file) throws IOException {
		saveResultsResponseTime(file, TimeMeter.Precision.MILLI);
	};
	
	public void saveResultsResponseTime(String file, TimeMeter.Precision precision) throws IOException {
		if (this.responseTime != null) {
			
			double divider = 1.0;
			if(precision!=null)
				divider = precision.divider;
		
			CSVWriter writer = new CSVWriter(new FileWriter(new File(file + "-ResponseTime-Total.csv")));
			List<String> header = new LinkedList<String>();			

			if (this.algorithms != null)
				for (Algorithm<BasicProblem> algorithm : this.algorithms) {
					header.add(algorithm.getName());
				}

			writer.writeNext(header.toArray(new String[header.size()]));
			
			for (int r = 0; r < instances.length; r++) {
				for (int i = 0; i < instances[r].length; i++) {
					List<String> responsetime = new LinkedList<String>();
					if (this.algorithms != null)
						for (int a = 0; a < algorithms.length; a++) {
							responsetime.add(Double.toString(this.responseTime[a][r][i] / divider));
						}
					writer.writeNext(responsetime.toArray(new String[responsetime.size()]));
				}
			}
			writer.close();
		}
	}

}
