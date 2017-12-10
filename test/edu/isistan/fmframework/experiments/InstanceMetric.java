package edu.isistan.fmframework.experiments;


import edu.isistan.fmframework.optimization.Problem;

public interface InstanceMetric<P extends Problem<?,?>> {
	Object getInstanceMeasure(P instance);
	String getName();
}
