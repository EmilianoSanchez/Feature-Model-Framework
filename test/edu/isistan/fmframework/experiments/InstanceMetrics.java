package edu.isistan.fmframework.experiments;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.Utils;
import edu.isistan.fmframework.optimization.optCSA.CSAalgorithm;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagators;

public class InstanceMetrics {
	public final static InstanceMetric<Problem<?, ?>> NUM_FEATURES = new InstanceMetric<Problem<?, ?>>() {

		@Override
		public String getName() {
			return "Num features";
		}

		@Override
		public Object getInstanceMeasure(Problem<?, ?> instance) {
			return instance.model.getNumFeatures();
		}
	};

	public final static InstanceMetric<Problem<?, ?>> FM_NAME = new InstanceMetric<Problem<?, ?>>() {

		@Override
		public String getName() {
			return "Model name";
		}

		@Override
		public Object getInstanceMeasure(Problem<?, ?> instance) {
			return instance.model.getName();
		}
	};

	public final static InstanceMetric<Problem<?, ?>> NUM_VALID_PRODUCTS = new InstanceMetric<Problem<?, ?>>() {

		@Override
		public String getName() {
			return "Num valid products";
		}

		@Override
		public Object getInstanceMeasure(Problem<?, ?> instance) {

			return Utils.realCount(instance);

		}
	};

	public final static InstanceMetric<Problem<?, ?>> NUM_VALID_STATES = new InstanceMetric<Problem<?, ?>>() {

		@Override
		public String getName() {
			return "Num valid states";
		}

		@Override
		public Object getInstanceMeasure(Problem<?, ?> instance) {

			return Utils.realStatesCount(instance);

		}
	};

	public final static InstanceMetric<Problem<?, ?>> NUM_INITIAL_UNASSIGNED_FEATURES = new InstanceMetric<Problem<?, ?>>() {

		@Override
		public String getName() {
			return "Num initial unassigned features";
		}

		@Override
		public Object getInstanceMeasure(Problem<?, ?> instance) {
			Configuration conf = ConstraintPropagators.clauseBasedConstraintPropagator.getPartialConfiguration(instance.model);
//			ConfOperations.assignFeature(conf, 0, true);
			int unassigned = 0;
			for (int i = 0; i < conf.getNumFeatures(); i++)
				if (conf.isFeatureUnselected(i))
					unassigned++;
			return unassigned;
		}
	};
}
