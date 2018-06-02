package edu.isistan.fmframework.optimization.optCSA.heuristicFunctions;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.optimization.aggregateFunctions.AggregateFunction;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.MultiLinearPolynomialObjective;

public class HeuristicB_MultiLinearPolynomialObjective extends HeuristicA_MultiLinearPolynomialObjective {

	public HeuristicB_MultiLinearPolynomialObjective(FeatureModel fmodel,
			MultiLinearPolynomialObjective objectiveFunction, AggregateFunction optCriterion) {
		super(fmodel, objectiveFunction, optCriterion);
		this.heuristicForLinearTerms = new HeuristicB_AggregateObjective(fmodel,
				new AdditionObjective(objectiveFunction.getFirstOrderCoefficients()), optCriterion);
	}

}
