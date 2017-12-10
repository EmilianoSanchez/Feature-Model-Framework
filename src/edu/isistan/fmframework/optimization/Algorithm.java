package edu.isistan.fmframework.optimization;

import edu.isistan.fmframework.core.Configuration;

public interface Algorithm<P extends Problem<?,?>> {

	default void preprocessInstance(P instance) {};

	Configuration selectConfiguration(P instance);
	
	default String getName() {
		return this.getClass().getSimpleName();
	};
}
