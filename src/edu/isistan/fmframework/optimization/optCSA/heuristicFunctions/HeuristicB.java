package edu.isistan.fmframework.optimization.optCSA.heuristicFunctions;

import java.util.AbstractMap.SimpleEntry;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.optimization.aggregateFunctions.AggregateFunction;
import edu.isistan.fmframework.optimization.objectiveFunctions.AggregateObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.LinearWeightedObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.MultiLinearPolynomialObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class HeuristicB extends HeuristicA {

	protected ObjectiveFunction buildHeuristicFunction(FeatureModel fmodel, ObjectiveFunction objectiveFunction,
			AggregateFunction optCriterion) {
		ObjectiveFunction heuristicFunction;

		if (objectiveFunction instanceof AggregateObjective) {
			heuristicFunction = new HeuristicB_AggregateObjective(fmodel, (AggregateObjective) objectiveFunction,
					optCriterion);
		} else {
			if (objectiveFunction instanceof MultiLinearPolynomialObjective)
				heuristicFunction = new HeuristicB_MultiLinearPolynomialObjective(fmodel,
						(MultiLinearPolynomialObjective) objectiveFunction, optCriterion);
			else {
				if (objectiveFunction instanceof LinearWeightedObjective) {
					LinearWeightedObjective heuristicFunctionAux = new LinearWeightedObjective();
					for (SimpleEntry<ObjectiveFunction, Double> term : ((LinearWeightedObjective) objectiveFunction)
							.getTerms()) {
						if (term.getValue() > 0.0)
							heuristicFunctionAux.addTerm(
									buildHeuristicFunction(fmodel, term.getKey(), CRITERIA_MINIMISE), term.getValue());
						else
							heuristicFunctionAux.addTerm(
									buildHeuristicFunction(fmodel, term.getKey(), CRITERIA_MAXIMISE), term.getValue());
					}
					heuristicFunction = heuristicFunctionAux;
				} else {
					throw new RuntimeException("The objective function type is not supported");
				}
			}
		}

		return heuristicFunction;
	}

}
