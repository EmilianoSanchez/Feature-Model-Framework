package edu.isistan.fmframework.optimization.optGAFES.ga;

public class Chromosome implements Comparable<Chromosome> {
	public boolean[] bitset;
	public double fitnessValue;
	public Boolean valid;

	public Chromosome(boolean[] bitset, double fitnessValue) {
		this(bitset,fitnessValue,null);
	}
	
	public Chromosome(boolean[] bitset, double fitnessValue,Boolean valid) {
		this.bitset = bitset;
		this.fitnessValue = fitnessValue;
		this.valid=valid;
	}

	public Chromosome(boolean[] bitset) {
		this(bitset,Double.NaN,null);
	}

	@Override
	public int compareTo(Chromosome other) {
		if (this.fitnessValue < other.fitnessValue)
			return 1;
		else if (this.fitnessValue > other.fitnessValue)
			return -1;
		else
			return 0;
	}

	@Override
	public String toString() {
		return Chromosome.toString(bitset)+":"+this.fitnessValue+":"+this.valid;
	}
	
	public static String toString(boolean[] bitset){
		StringBuilder builder=new StringBuilder();
		for (int i = 0; i < GAFESfmTransform.instance.model.getNumFeatures(); i++) {
			if (bitset[i])
				builder.append('1');
			else
				builder.append('0');
		}
		return builder.toString();
	}
}
