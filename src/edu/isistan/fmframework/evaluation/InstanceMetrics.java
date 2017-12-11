package edu.isistan.fmframework.evaluation;

import edu.isistan.fmframework.optimization.Problem;

public class InstanceMetrics {
	public final static InstanceMetric<Problem> NUM_FEATURES=new InstanceMetric<Problem>() {
		
		@Override
		public String getName() {
			return "Num features";
		}
		
		@Override
		public Object getInstanceMeasure(Problem instance) {
			return instance.model.getNumFeatures();
		}
	};
	
	public final static InstanceMetric<Problem> FM_NAME=new InstanceMetric<Problem>() {
		
		@Override
		public String getName() {
			return "Model name";
		}
		
		@Override
		public Object getInstanceMeasure(Problem instance) {
			return instance.model.getName();
		}
	};
	
}
