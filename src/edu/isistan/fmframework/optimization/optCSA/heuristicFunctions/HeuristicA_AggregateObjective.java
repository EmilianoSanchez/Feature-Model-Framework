package edu.isistan.fmframework.optimization.optCSA.heuristicFunctions;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import edu.isistan.fmframework.optimization.aggregateFunctions.AggregateFunction;
import edu.isistan.fmframework.optimization.objectiveFunctions.AggregateObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class HeuristicA_AggregateObjective implements ObjectiveFunction {

	protected Configuration conf;

	protected FeatureModel fmodel;
	protected AggregateObjective aggregateObjective;
	protected AggregateFunction optCriterion;

	public HeuristicA_AggregateObjective(FeatureModel fmodel, AggregateObjective aggregateObjective,
			AggregateFunction optCriteria) {
		this.fmodel = fmodel;
		this.aggregateObjective = aggregateObjective;
		this.optCriterion = optCriteria;
	}

	@Override
	public double evaluate(Configuration configuration) {
		this.conf = configuration;
		double result = this.recursiveSelected(FeatureModel.ROOT_ID);
		return result;
	}

	protected double recursiveSelected(int featureId) {

		double result = aggregateObjective.attributes[featureId];

		for (int subFeatureId : fmodel.getChildren(featureId)) {
			switch (conf.getFeatureState(subFeatureId)) {
			case SELECTED:
				result = this.aggregateObjective.getFunction().operate(result, this.recursiveSelected(subFeatureId));
				break;
			case UNSELECTED:
				if (fmodel.getParentTree(subFeatureId).getClass() == MandatoryFeature.class) {
					result = this.aggregateObjective.getFunction().operate(result,
							this.recursiveSelected(subFeatureId));
				} else {
					result = this.optCriterion.operate(
							this.aggregateObjective.getFunction().operate(result, this.recursiveSelected(subFeatureId)),
							result);
				}
				break;
			default:
				break;
			}
		}
		return result;
	}
}
