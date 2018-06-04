package edu.isistan.fmframework.optimization.optCSA.constraintPropagator;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.Clause;

public interface ConstraintPropagator {
	
	public boolean assignFeature(Configuration conf, int featureId, FeatureState state);
	
	default public boolean assignFeature(Configuration conf, int featureId, boolean selected) {
		if (selected)
			return assignFeature(conf, featureId, FeatureState.SELECTED);
		else
			return assignFeature(conf, featureId, FeatureState.DESELECTED);
	}
	
	default public Configuration getPartialConfiguration(FeatureModel fmodel) {
		Configuration conf = new Configuration(fmodel);

		for (int i=0;i<fmodel.getNumFeatures();i++){
			for (Clause clause : fmodel.getClauses(i)) {
				if (clause.literalIds.length == 1) {
					if (!assignFeature(conf, i, clause.literalValues[0]))
						return null;
				}
			}
		}
		if (!assignFeature(conf, 0, FeatureState.SELECTED))
			return null;
		return conf;
	}
}
