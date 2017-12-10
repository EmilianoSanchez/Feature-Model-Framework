package edu.isistan.fmframework.optimization.optCSA.searchStrategies;

import edu.isistan.fmframework.core.Configuration;

public class State implements Comparable<State> {
	public Configuration conf;
	public double value;

	public State(Configuration conf, double value) {
		this.conf = conf;
		this.value = value;
	}

	@Override
	public int compareTo(State other) {
		if (this.value < other.value)
			return -1;
		else if (this.value > other.value)
			return 1;
		else
			return 0;
	}
}
