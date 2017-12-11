package edu.isistan.fmframework.optimization;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.Constraint;
import edu.isistan.fmframework.core.constraints.globalConstraints.InequalityRestriction;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class Problem<C extends Constraint, O extends ObjectiveFunction> {

	public FeatureModel model;

	public C[] globalConstraints;

	public O[] objectiveFunctions;

	public Problem() {
	}

	public Problem(FeatureModel model) {
		this.model = model;
	}
	
	public Problem(FeatureModel model, O objective,
			C[] restrictions) {
		this.model = model;
		this.objectiveFunctions = (O[]) new Object[] { objective };
		this.globalConstraints = restrictions;
	}

	public boolean isValid() {
		return Utils.isValid(this);
	}
	
	public boolean isSatisfied(Configuration conf) {
		return this.model.isSatisfied(conf) && satisfyGlobalConstraints(conf);
	}

	public boolean satisfyGlobalConstraints(Configuration conf) {
		if(this.globalConstraints!=null){
			for (C globalConstraint : this.globalConstraints) {
				if (!globalConstraint.isSatisfied(conf))
					return false;
			}
		}
		return true;
	}

	public double[] evaluateObjectives(Configuration conf) {
		double[] results = new double[this.objectiveFunctions.length];
		for (int i = 0; i < this.objectiveFunctions.length; i++) {
			results[i] = this.objectiveFunctions[i].evaluate(conf);
		}
		return results;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.model.toString()).append("\n");
		for(int i=0;i<this.objectiveFunctions.length;i++)
			stringBuilder.append(this.objectiveFunctions[i].toString()).append("\n");
		for(int i=0;i<this.globalConstraints.length;i++)
			stringBuilder.append(this.globalConstraints[i].toString()).append("\n");
		return stringBuilder.toString();
	}
}
