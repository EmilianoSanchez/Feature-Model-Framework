package edu.isistan.fmframework.optimization.optGAFES.ga;

import java.util.Arrays;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.utils.RandomUtils;

public class GAFESpopulation {

	public Chromosome[] population;

	public GAFESpopulation(int population_size) {
		this(population_size, 0.5, 2 * population_size);
	}

	int population_size;
	double ratioOfSelectedFeatures;
	int max_retries;
	int numGenes;
	
	public GAFESpopulation(int population_size, double ratioOfSelectedFeatures, int max_retries) {
		this.population_size = population_size;
		this.ratioOfSelectedFeatures = ratioOfSelectedFeatures;
		this.max_retries = max_retries;
		this.numGenes = 0;
	}

	public void initPopulation() {
		this.population = new Chromosome[population_size];

		this.numGenes = GAFESfmTransform.instance.model.getNumFeatures();
		int i = 0;
//		int times=0;
		do {
			boolean[] bitset = new boolean[numGenes];
			bitset[0]=true;
			int retries = 0;
			while (retries < max_retries) {
				for (int g = 1; g < numGenes; g++) {
					if (RandomUtils.random() < ratioOfSelectedFeatures)
						bitset[g]= true;
					else
						bitset[g]= false;
				}
				bitset = GAFESfmTransform.fmTransform(bitset);
//				times++;
				if (GAFESfmTransform.satisfyResourceRestrictions(bitset)) {
					break;
				} else
					retries++;
			}
			this.population[i] = new Chromosome(bitset, fitness(bitset));
			i++;

		} while (i < population_size);

		Arrays.sort(population);
		
//		System.out.println("times:"+times);
//		checkPopulationSatisfaction(this);
	}

	private int indexParent1, indexParent2;

	public Chromosome[] selectParentChromosomes() {
		indexParent1 = RandomUtils.randomRange(0, population_size);//(int) Math.floor(Math.random() * this.population_size);
		indexParent2 = RandomUtils.randomRange(0, population_size);//(int) Math.floor(Math.random() * this.population_size);

//		System.out.println(indexParent1+" "+indexParent2);
		Chromosome[] result = new Chromosome[] { population[indexParent1], population[indexParent2] };
		return result;
	}

	public void replace(Chromosome offspring, Chromosome[] parents) {
		offspring.fitnessValue=fitness(offspring.bitset);
		
//		checkChromosomeSatisfaction(child);
		
		if (offspring.fitnessValue < population[indexParent1].fitnessValue) {
			if (offspring.fitnessValue < population[indexParent2].fitnessValue) {
//				insertInOrder(child);
				insertAndRemove(offspring,population_size-1);
			} else {
//				insertInOrder(child);
//				remove(indexParent2 + 1);
				insertAndRemove(offspring,indexParent2);
			}
		} else {
			if (offspring.fitnessValue < population[indexParent2].fitnessValue) {
//				insertInOrder(child);
//				remove(indexParent1 + 1);
				insertAndRemove(offspring,indexParent1);
			} else {
//				insertInOrder(child);
				if (population[indexParent1].fitnessValue > population[indexParent2].fitnessValue)
//					remove(indexParent2 + 1);
					insertAndRemove(offspring,indexParent1);
				else
//					remove(indexParent1 + 1);
					insertAndRemove(offspring,indexParent2);
			}
		}
	}
	
	private void insertAndRemove(Chromosome child,int indexRemove) {
		int indexInsert = orderedPosition(child);
//		System.out.println("indexInsert "+indexInsert);
		if(indexRemove>=population_size){
			indexRemove=population.length - 1;
		}
		for (int i = indexRemove; i > indexInsert; i--)
			population[i] = population[i - 1];
		population[indexInsert] = child;
	}

	private int orderedPosition(Chromosome child) {
        int lo = 0;
        int hi = population_size - 1;
        int mid = lo + (hi - lo) / 2;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            mid = lo + (hi - lo) / 2;
            if(child.fitnessValue > population[mid].fitnessValue){
            	hi = mid - 1;
            }else{
            	if (child.fitnessValue < population[mid].fitnessValue){
            		lo = mid + 1;
            	}else{
            		return mid;
            	}
            }
            
        }
        return mid;
	}

//	private boolean remove(int index) {
//		if(index>=population_size)
//			return false;
//		else{
//			
//		}
//	}
//
//	private int insertInOrder(ChromosomeBitset child) {
//		int position = orderedPosition(population, child);
//		for (int i = population.length - 1; i > position; i--)
//			population[i] = population[i - 1];
//		population[position] = offspring;
//	}

	public boolean[] getBestChromosome() {
		return population[0].bitset;
	}
	public Configuration getBestConfiguration() {
		return bitsetToConfiguration(this.getBestChromosome());
	}

	public static double fitness(boolean[] bitset) {

		double v = 0.0;
		double r = 0.0;

		double[] attributes = GAFESfmTransform.instance.objectiveFunctions[0].attributes;
		for (int i = 0; i < bitset.length; i ++) {	
			if(bitset[i]){
				v += attributes[i];
				if(GAFESfmTransform.instance.globalConstraints.length==0){
					r=1.0;
				}else{
					for (int rid = 0; rid < GAFESfmTransform.instance.globalConstraints.length; rid++)
						r += GAFESfmTransform.instance.globalConstraints[rid].attributes[i];
				}
			}
		}
		return v / r;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		for(Chromosome chromosome:population){
			builder.append(chromosome);
			builder.append('\n');
//			for(int i=0;i<this.numGenes;i++){
//				if(chromosome.bitset.get(i))
//					builder.append('1');
//				else
//					builder.append('0');
//			}
//			builder.append('\n');
		}
		return builder.toString();
	}
	
	
//	public static void checkPopulationSatisfaction(GAFESpopulation population) {
//		for (ChromosomeBitset chromosome : population.population) {
//			checkChromosomeSatisfaction(chromosome);
//		}
//	}
//
//	public static void checkChromosomeSatisfaction(ChromosomeBitset chromosome) {
//		Configuration conf = bitsetToConfiguration(chromosome.bitset);
//		chromosome.valid = conf.getModel().isSatisfied(conf);
//	}

	public static Configuration bitsetToConfiguration(boolean[] bitset) {
		Configuration conf = new Configuration(GAFESfmTransform.instance.model);
		for (int i = 0; i < conf.getNumFeatures(); i++) {
			if (bitset[i])
				conf.setFeatureState(i, true);
			else
				conf.setFeatureState(i, false);
		}
		return conf;
	}

}
