package com.emiliano.fmframework.reasoning.csa;

import com.emiliano.fmframework.core.Configuration;

public class State implements Comparable<State> {
	public Configuration conf;
	public Double value;

	public State(Configuration conf, Double value) {
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
