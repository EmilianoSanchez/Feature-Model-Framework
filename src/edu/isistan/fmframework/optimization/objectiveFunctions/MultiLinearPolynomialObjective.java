package edu.isistan.fmframework.optimization.objectiveFunctions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.Pair;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.utils.RandomUtils;

public class MultiLinearPolynomialObjective implements ObjectiveFunction {

	private class Term {
		double coefficient;
		Term[] variables;

		Term() {
			variables = new Term[numVariables];
		}
	};

	private int numVariables;
	private Term terms;

	public MultiLinearPolynomialObjective(int numVariables) {
		this.numVariables = numVariables;
		this.terms = new Term();
	}

	public MultiLinearPolynomialObjective(int numVariables, double independentCoefficient,
			double[] firstOrderCoefficients) {
		this(numVariables);
		TreeSet<Integer> variables = new TreeSet<>();
		addTerm(independentCoefficient, variables);
		for (int i = 0; i < firstOrderCoefficients.length; i++) {
			variables.add(i);
			addTerm(firstOrderCoefficients[i], variables);
			variables.remove(i);
		}
	}

	public void addTerm(double coefficient, SortedSet<Integer> variables) {
		if (coefficient != 0.0) {
			Term termaux = this.terms;
			for (int variable : variables) {
				if (termaux.variables[variable] == null)
					termaux.variables[variable] = new Term();
				termaux = termaux.variables[variable];
			}
			termaux.coefficient = coefficient;
		}
	};

	public List<Pair<Double, SortedSet<Integer>>> getTerms() {
		List<Pair<Double, SortedSet<Integer>>> result = new LinkedList<>();
		getTerms(result, this.terms, new TreeSet<>());
		return result;
	}

	public double[] getFirstOrderCoefficients() {
		double[] result = new double[numVariables];
		for (int i = 0; i < numVariables; i++) {
			if (this.terms.variables[i] != null)
				result[i] = this.terms.variables[i].coefficient;
			else
				result[i] = 0.0;
		}
		return result;
	}

	public List<Pair<Double, SortedSet<Integer>>> getNonLinearTerms() {
		List<Pair<Double, SortedSet<Integer>>> nonLinearTerms = getTerms();
		Iterator<Pair<Double, SortedSet<Integer>>> ite = nonLinearTerms.iterator();
		while (ite.hasNext()) {
			Pair<Double, SortedSet<Integer>> next = ite.next();
			if (next.getRight().size() <= 1)
				ite.remove();
		}
		return nonLinearTerms;
	}

	private void getTerms(List<Pair<Double, SortedSet<Integer>>> result, Term term, SortedSet<Integer> variables) {

		if (term.coefficient != 0.0) {
			result.add(Pair.of(term.coefficient, new TreeSet<>(variables)));
		}

		for (int i = 0; i < term.variables.length; i++) {
			if (term.variables[i] != null) {
				variables.add(i);
				getTerms(result, term.variables[i], variables);
				variables.remove(i);
			}
		}
	}

	@Override
	public double evaluate(Configuration configuration) {
		if (terms != null)
			return evaluate(configuration, terms, new TreeSet<>());
		else
			return 0;
	}

	private double evaluate(Configuration configuration, Term term, SortedSet<Integer> variables) {

		double result = term.coefficient;

		for (int i = 0; i < term.variables.length; i++) {
			if (term.variables[i] != null) {
				variables.add(i);
				result += evaluate(configuration, term.variables[i], variables);
				variables.remove(i);
			}
		}

		for (int variable : variables) {
			if (configuration.getFeatureState(variable) != FeatureState.SELECTED) {
				result -= term.coefficient;
				break;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("y = ");
		toString(this.terms, builder, new TreeSet<>());
		return builder.toString();
	}

	private void toString(Term term, StringBuilder builder, SortedSet<Integer> variables) {

		if (term.coefficient != 0.0) {
			builder.append(" + " + term.coefficient);

			if (!variables.isEmpty()) {
				for (int variable : variables) {
					builder.append("*x").append(variable);
				}
			}
		}

		for (int i = 0; i < term.variables.length; i++) {
			if (term.variables[i] != null) {
				variables.add(i);
				toString(term.variables[i], builder, variables);
				variables.remove(i);
			}
		}

	}

	public void setIndependentCoefficient(double independentCoefficient) {
		terms.coefficient = independentCoefficient;
	}

	public double getIndependentCoefficient() {
		return terms.coefficient;
	}

	public static MultiLinearPolynomialObjective generateRandomInstance(int numFeatures, int[] numOfTerms) {
		MultiLinearPolynomialObjective result = new MultiLinearPolynomialObjective(numFeatures);
		result.setIndependentCoefficient(RandomUtils.randomDouble(-1.0, 1.0));
		for (int order = 1; order <= numOfTerms.length; order++) {
			for (int i = 0; i < numOfTerms[order - 1]; i++) {
				TreeSet<Integer> variables = new TreeSet<>();
				while (variables.size() < order) {
					variables.add(RandomUtils.randomRange(0, numFeatures));
				}
				result.addTerm(RandomUtils.randomDouble(-1.0, 1.0), variables);
			}
		}
		return result;
	}

}
