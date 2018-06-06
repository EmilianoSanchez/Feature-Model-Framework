package edu.isistan.fmframework.optimization.optCSA.heuristicFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint;
import edu.isistan.fmframework.optimization.aggregateFunctions.AdditionFunction;
import edu.isistan.fmframework.optimization.aggregateFunctions.AggregateFunction;
import edu.isistan.fmframework.optimization.aggregateFunctions.AggregateFunction.AggregateFunctionType;
import edu.isistan.fmframework.optimization.aggregateFunctions.ProductFunction;
import edu.isistan.fmframework.optimization.objectiveFunctions.AggregateObjective;

public class HeuristicTC_AggregateObjective extends HeuristicMO_AggregateObjective{

	public HeuristicTC_AggregateObjective(FeatureModel fmodel, AggregateObjective aggregateObjective,
			AggregateFunction optCriteria) {
		super(fmodel, aggregateObjective, optCriteria);
	}

	static final int DESELECTED=0;
	static final int SELECTED=1;
	static final int UNSELECTED=2;
	
	protected double recursiveSelected(int featureId) {
		
		double result = aggregateObjective.attributes[featureId];

		for (TreeConstraint childrenTree : fmodel.getChildrenTrees(featureId)) {
			int[] counts = new int[3];
			List<Integer> unselecteds = new ArrayList<>(childrenTree.getChildren().length);
			for (int subFeatureId : childrenTree.getChildren()) {

				switch (conf.getFeatureState(subFeatureId)) {
				case DESELECTED:
					counts[DESELECTED]++;
					break;
				case SELECTED:
					counts[SELECTED]++;
					result = this.aggregateObjective.getFunction().operate(result, this.recursiveSelected(subFeatureId));
					break;
				case UNSELECTED:
					counts[UNSELECTED]++;
					unselecteds.add(subFeatureId);
					break;
				}
			}

			if(counts[UNSELECTED]>0){
				if (childrenTree.getMinCardinality() == counts[SELECTED] + counts[UNSELECTED]) {
					for (int unselectedId : unselecteds) {
						result = this.aggregateObjective.getFunction().operate(result, this.recursiveSelected(unselectedId));
					}
				} else {
					int minSelecteds = childrenTree.getMinCardinality() - counts[SELECTED];
					int maxSelecteds = childrenTree.getMaxCardinality() - counts[SELECTED];
					
					if(minSelecteds>counts[UNSELECTED])
						throw new RuntimeException("Constraint propagation lead to an invalid configuration");
	
					if (this.aggregateObjective.getFunction() instanceof AdditionFunction || this.aggregateObjective.getFunction() instanceof ProductFunction) {
						double diff[] = new double[unselecteds.size()];
						for (int i = 0; i < unselecteds.size(); i++) {
							diff[i] = this.recursiveSelected(unselecteds.get(i));
						}
	
						Arrays.sort(diff);
						if (this.optCriterion.getType() == AggregateFunctionType.MAXIMUM) {
							ArrayUtils.reverse(diff);
						}
	
						int i = 0;
						for (; i < minSelecteds; i++)
							result = this.aggregateObjective.getFunction().operate(result, diff[i]);
						if (this.optCriterion.getType() == AggregateFunctionType.MAXIMUM) {
							while (i < maxSelecteds && i<diff.length && diff[i] > this.aggregateObjective.getFunction().getNeutralElement()) {
								result = this.aggregateObjective.getFunction().operate(result, diff[i]);
								i++;
							}
						} else {
							while (i < maxSelecteds && i<diff.length && diff[i] < this.aggregateObjective.getFunction().getNeutralElement()) {
								result = this.aggregateObjective.getFunction().operate(result, diff[i]);
								i++;
							}
						}
	
					} else {
						throw new IllegalArgumentException(
								"Maximum and Minimum functions are not supported yet in HeuristicTC extended");
					}
	
				}
			}
		}
		return result;
	}
}
