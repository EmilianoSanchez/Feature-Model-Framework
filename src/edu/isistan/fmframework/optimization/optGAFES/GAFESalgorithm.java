package edu.isistan.fmframework.optimization.optGAFES;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.optimization.BasicAlgorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.optGAFES.ga.Chromosome;
import edu.isistan.fmframework.optimization.optGAFES.ga.GAFESfmTransform;
import edu.isistan.fmframework.optimization.optGAFES.ga.GAFESpopulation;
import edu.isistan.fmframework.utils.RandomUtils;

public class GAFESalgorithm implements BasicAlgorithm {

	public GAFESalgorithm() {
		this(30, 100, 1.0, 0.1);
	}

	public GAFESalgorithm(int population_size, int maximum_generation) {
		this(population_size, maximum_generation, 1.0, 0.1);
	}

	public GAFESalgorithm(int population_size, int maximum_generation, double crossover_probability,
			double mutation_rate) {
		this.population_size = population_size;
		this.maximum_generation = maximum_generation;
		this.crossover_probability = crossover_probability;
		this.mutation_rate = mutation_rate;

	}

	private int population_size = 30;
	private int maximum_generation = 100;
	private double crossover_probability = 1.0;// (always applied)
	private double mutation_rate = 0.1;

	@Override
	public void preprocessInstance(BasicProblem instance) {
		if (instance.objectiveFunctions.length > 1 || instance.objectiveFunctions.length == 0)
			throw new IllegalArgumentException(
					"GAFESalgorithm only supports instances with a single additive objective function");
	}

	@Override
	public Configuration selectConfiguration(BasicProblem instance) {
		// this.instance = instance;
		GAFESfmTransform.instance = instance;
		// ChromosomeBitset[] population = initializePopulation(instance);
		GAFESpopulation population = new GAFESpopulation(population_size);
		population.initPopulation();

		// System.out.println(population);
		int generation = 0;
		while (generation < maximum_generation) {
			generation++;

			// ChromosomeBitset[] parents = selectChromosomes(population);
			Chromosome[] parents = population.selectParentChromosomes();
			Chromosome offspring = crossover(parents[0], parents[1]);
			if (RandomUtils.random() < mutation_rate)
				mutation(offspring);
			offspring.bitset = GAFESfmTransform.fmTransform(offspring.bitset);
			if (GAFESfmTransform.satisfyResourceRestrictions(offspring.bitset)) {
				// System.out.println("Parents y offspring");
				// System.out.println(ChromosomeBitset.toString(parents[0]));
				// System.out.println(ChromosomeBitset.toString(parents[1]));
				// System.out.println(ChromosomeBitset.toString(offspring)+":"+GAFESpopulation.fitness(offspring));
				population.replace(offspring, parents);
			}

			// System.out.println("Population generation " + generation);
			// System.out.println(population);
		}
		return population.getBestConfiguration();
	}

	// point mutation operation
	private void mutation(Chromosome offspring) {
		int point = RandomUtils.randomRange(1, GAFESfmTransform.instance.model.getNumFeatures());// 1+(int)Math.floor(Math.random()*(GAFESfmTransform.instance.model.getNumFeatures()-1));
		offspring.bitset[point] ^= true;
	}

	// uniform crossover operation
	private Chromosome crossover(Chromosome parent1, Chromosome parent2) {
		int numGenes = GAFESfmTransform.instance.model.getNumFeatures();
		boolean[] offspring = new boolean[numGenes];

		for (int i = 1; i < numGenes; i++) {
			if (RandomUtils.random() < 0.5) {
				offspring[i] = parent1.bitset[i];
			} else {
				offspring[i] = parent2.bitset[i];
			}
		}
		return new Chromosome(offspring);
	}

	@Override
	public String getName() {
		return "GAFES(pop:" + this.population_size + ",gen:" + this.maximum_generation + ")";
	}
}
