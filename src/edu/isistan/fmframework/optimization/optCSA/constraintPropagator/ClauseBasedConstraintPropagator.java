package edu.isistan.fmframework.optimization.optCSA.constraintPropagator;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.Clause;

public class ClauseBasedConstraintPropagator implements ConstraintPropagator {

	@Override
	public boolean assignFeature(Configuration conf, int featureId, FeatureState state) {

		if (conf.getFeatureState(featureId) == FeatureState.UNSELECTED) {
			conf.setFeatureState(featureId, state);

			for (Clause clause : conf.getModel().getClauses(featureId)) {
				int numUnselected = 0;
				int unselectedLiteral = 0;
				boolean unselectedLiteralValue = false;
				boolean atLeastOneLiteralTrue = false;

				for (int i = 0; i < clause.literalIds.length; i++) {
					if (conf.getFeatureState(clause.literalIds[i]) == FeatureState.UNSELECTED) {// isLiteralUndefinned
						numUnselected++;
						unselectedLiteral = clause.literalIds[i];
						unselectedLiteralValue = clause.literalValues[i];
					} else {
						if (conf.getFeatureState(clause.literalIds[i]).booleanValue() == clause.literalValues[i]) {// isLiteralTrue
							atLeastOneLiteralTrue = true;
							break;
						}
					}
				}

				if (atLeastOneLiteralTrue)
					continue;
				else {
					if (numUnselected == 0)
						return false;
					else
					// propagation
					if (numUnselected == 1 && !assignFeature(conf, unselectedLiteral,
							FeatureState.featureStateValue(unselectedLiteralValue)))
						return false;
				}
			}

			return true;
		} else {
			if (conf.getFeatureState(featureId) == state)
				return true;
			else
				return false;
		}
	}

}
