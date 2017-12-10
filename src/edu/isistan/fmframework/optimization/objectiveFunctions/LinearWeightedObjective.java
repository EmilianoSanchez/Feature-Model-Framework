package edu.isistan.fmframework.optimization.objectiveFunctions;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;

import edu.isistan.fmframework.core.Configuration;

public class LinearWeightedObjective implements ObjectiveFunction {

	public static final double MINIMIZE = 1.0;// !If a positive value is used as
												// weight (w), the function is
												// minimized
	public static final double MAXIMIZE = -1.0;// !If a negative value is used
												// as weight (w), the function
												// is maximized

	private List<SimpleEntry<AggregateObjective, Double>> terms;

	public LinearWeightedObjective() {
		this.terms = new LinkedList<AbstractMap.SimpleEntry<AggregateObjective, Double>>();
	}

	public LinearWeightedObjective(AggregateObjective ...terms) {
		this();
		for(AggregateObjective term: terms){
			this.addTerm(term);
		}
	}
	
	public LinearWeightedObjective(double weight, AggregateObjective ...terms) {
		this();
		for(AggregateObjective term: terms){
			this.addTerm(term,weight);
		}
	}

	public void addTerm(AggregateObjective term, double weight) {
		this.terms.add(new AbstractMap.SimpleEntry<AggregateObjective, Double>(term, weight));
	}

	public void addTerm(AggregateObjective cof) {
		this.addTerm(cof, MINIMIZE);
	}

	public void clear() {
		this.terms.clear();
	};

	@Override
	public double evaluate(Configuration conf) {
		double result = 0.0;
		for (AbstractMap.SimpleEntry<AggregateObjective, Double> cof : terms)
			result += cof.getKey().evaluate(conf) * cof.getValue();
		return result;
	}

	public List<SimpleEntry<AggregateObjective, Double>> getTerms() {
		return terms;
	}

	public void setTerms(List<SimpleEntry<AggregateObjective, Double>> terms) {
		this.terms = terms;
	}

}
