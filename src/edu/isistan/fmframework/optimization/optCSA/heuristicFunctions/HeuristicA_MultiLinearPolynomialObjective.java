package edu.isistan.fmframework.optimization.optCSA.heuristicFunctions;

import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang3.tuple.Pair;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.optimization.aggregateFunctions.AggregateFunction;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.MultiLinearPolynomialObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class HeuristicA_MultiLinearPolynomialObjective implements ObjectiveFunction {

	protected Configuration conf;

	protected FeatureModel fmodel;
	protected ObjectiveFunction heuristicForLinearTerms;
	protected AggregateFunction optCriterion;
	protected double independentCoefficient;
	protected List<Pair<Double, SortedSet<Integer>>> nonLinearTerms;

	public HeuristicA_MultiLinearPolynomialObjective(FeatureModel fmodel,
			MultiLinearPolynomialObjective objectiveFunction, AggregateFunction optCriterion) {
		this.fmodel = fmodel;
		this.heuristicForLinearTerms = new HeuristicA_AggregateObjective(fmodel,
				new AdditionObjective(objectiveFunction.getFirstOrderCoefficients()), optCriterion);
		this.optCriterion = optCriterion;

		this.independentCoefficient = objectiveFunction.getIndependentCoefficient();
		this.nonLinearTerms = objectiveFunction.getNonLinearTerms();

	}

	private static final int ALL_SELECTED = 0;
	private static final int AT_LEAST_ONE_DESELECTED = 1;
	private static final int ONLY_SELECTED_AND_UNSELECTED = 2;

	@Override
	public double evaluate(Configuration configuration) {
		this.fmodel = configuration.getModel();
		this.conf = configuration;

		double result = independentCoefficient;
		result += heuristicForLinearTerms.evaluate(configuration);

		for (Pair<Double, SortedSet<Integer>> term : nonLinearTerms) {
			int situation = ALL_SELECTED;
			for (int variable : term.getRight()) {
				FeatureState featureState = configuration.getFeatureState(variable);
				if (featureState == FeatureState.DESELECTED) {
					situation = AT_LEAST_ONE_DESELECTED;
					break;
				} else {
					if (featureState == FeatureState.UNSELECTED) {
						situation = ONLY_SELECTED_AND_UNSELECTED;
					}
				}
			}
			switch (situation) {
			case ALL_SELECTED:
				result += term.getKey();
				break;
			case ONLY_SELECTED_AND_UNSELECTED:
				result += this.optCriterion.operate(term.getKey(), 0.0);
				break;
			}
		}
		return result;
	}

}
