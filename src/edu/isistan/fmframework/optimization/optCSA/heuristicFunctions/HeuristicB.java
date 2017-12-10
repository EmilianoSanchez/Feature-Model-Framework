package edu.isistan.fmframework.optimization.optCSA.heuristicFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint;
import edu.isistan.fmframework.optimization.aggregateFunctions.AdditionFunction;
import edu.isistan.fmframework.optimization.aggregateFunctions.MaximumFunction;
import edu.isistan.fmframework.optimization.aggregateFunctions.ProductFunction;

public class HeuristicB extends HeuristicA {

	protected double recursiveSelected(int featureId) {
		
//		System.err.println("HB");
		
		double result=termObjective.attributes[featureId];

		for (TreeConstraint childrenTree : fmodel.getChildrenTrees(featureId)) {
			int[] counts = new int[3];
			List<Integer> unselecteds = new ArrayList<>(childrenTree.getChildren().length);
			for (int subFeatureId : childrenTree.getChildren()) {

				switch (conf.getFeatureState(subFeatureId)) {
				case DESELECTED:
					counts[0]++;
//					result = this.termFunction.operate(result, this.recursiveDeselected(subFeatureId));
					break;
				case SELECTED:
					counts[1]++;
					result = this.termFunction.operate(result, this.recursiveSelected(subFeatureId));
					break;
				case UNSELECTED:
					counts[2]++;
					unselecteds.add(subFeatureId);
					break;
				}
			}

			// Que todos los unselected deben ser deselected no se tendria que
			// cumplir nunca
			// if(childrenTree.getMaxCardinality()-counts[1]==0){
			//
			// }

			if(counts[2]>0){
				if (childrenTree.getMinCardinality() == counts[1] + counts[2]) {// Todos
																				// los
																				// unselected
																				// deben
																				// ser
																				// selected
					for (int unselectedId : unselecteds) {
						result = this.termFunction.operate(result, this.recursiveSelected(unselectedId));
					}
				} else {// childrenTree.getMinCardinality()<counts[1]+counts[2]. La
						// otra condicion
						// (childrenTree.getMinCardinality()>counts[1]+counts[2]) no
						// se deberia cumplir nunca
						// Al menos 1 puede ser deselected
	
					int minSelecteds = childrenTree.getMinCardinality() - counts[1];
					int maxSelecteds = childrenTree.getMaxCardinality() - counts[1];
	
					if (this.termFunction instanceof AdditionFunction || this.termFunction instanceof ProductFunction) {
//						double results[][] = new double[2][unselecteds.size()];
						double diff[] = new double[unselecteds.size()];
						for (int i = 0; i < unselecteds.size(); i++) {
//							results[0][i] = this.recursiveDeselected(unselecteds.get(i));
//							results[1][i] = this.recursiveSelected(unselecteds.get(i));
							diff[i] = this.recursiveSelected(unselecteds.get(i));//diff[i] = this.termFunction.inverseOperator(results[1][i], results[0][i]);
//							result = this.termFunction.operate(result, results[0][i]);
						}
	
						Arrays.sort(diff);
						if (this.termCriteria.getClass() == MaximumFunction.class) {
							ArrayUtils.reverse(diff);
						}
	
						int i = 0;
						for (; i < minSelecteds; i++)
							result = this.termFunction.operate(result, diff[i]);
						if (this.termCriteria.getClass() == MaximumFunction.class) {
							while (i < maxSelecteds && i<diff.length && diff[i] > this.termFunction.getNeutralElement()) {
								result = this.termFunction.operate(result, diff[i]);
								i++;
							}
						} else {
							while (i < maxSelecteds && i<diff.length && diff[i] < this.termFunction.getNeutralElement()) {
								result = this.termFunction.operate(result, diff[i]);
								i++;
							}
						}
	
					} else {
						throw new IllegalArgumentException(
								"Maximum and Minimum functions are not supported yet in HeuristicB extended");
					}
	
				}
			}
		}
		return result;
	}
}
