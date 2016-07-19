package com.emiliano.fmframework.reasoning.csa.heuristicFunctions;

import java.util.AbstractMap;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint.TreeConstraintType;
import com.emiliano.fmframework.reasoning.aggregateFunctions.AggregateFunction;
import com.emiliano.fmframework.reasoning.aggregateFunctions.MaximumFunction;
import com.emiliano.fmframework.reasoning.aggregateFunctions.MinimumFunction;
import com.emiliano.fmframework.reasoning.objectiveFunctions.AggregateObjective;
import com.emiliano.fmframework.reasoning.objectiveFunctions.LinearWeightedObjective;
import com.emiliano.fmframework.reasoning.objectiveFunctions.ObjectiveFunction;

public class HeuristicA implements HeuristicFunction{

	protected static final AggregateFunction CRITERIA_MAXIMISE=new MaximumFunction();
	protected static final AggregateFunction CRITERIA_MINIMISE=new MinimumFunction();

	protected Configuration conf;	
	protected FeatureModel fmodel;
	protected LinearWeightedObjective targetObjective;
	
	protected AggregateObjective termObjective;
	protected AggregateFunction termFunction;
	protected AggregateFunction termCriteria;

	@Override
	public double evaluate(Configuration configuration, ObjectiveFunction objective) {

		this.conf=configuration;
		this.fmodel=conf.getModel();
		
		if(objective instanceof AggregateObjective){
			this.targetObjective=new LinearWeightedObjective((AggregateObjective)objective);
		}else{
			if(objective instanceof LinearWeightedObjective)
				this.targetObjective=(LinearWeightedObjective) objective;
			else
				throw new RuntimeException("The objective function type is not supported");
		}
		
		double result = 0.0;
		for (AbstractMap.SimpleEntry<AggregateObjective, Double> term : targetObjective.getTerms()){
			this.termObjective=term.getKey();
			this.termFunction=this.termObjective.getFunction();
			if(term.getValue()<0.0)
				this.termCriteria=CRITERIA_MINIMISE;
			else
				this.termCriteria=CRITERIA_MAXIMISE;
			result += this.recursiveSelected(conf.getModel().getRoot()) * term.getValue();
		}
		return result;
	}

	protected double recursiveSelected(Feature feature) {
		double result=(Double) feature.getAttribute(termObjective.getAttributeSelected());

		for(Feature subFeature: fmodel.getChildren(feature.getName())){
			switch(conf.getFeatureState(subFeature)){
				case SELECTED:
					result=this.termFunction.operate(result, this.recursiveSelected(subFeature));
					break;
				case DESELECTED:
					result=this.termFunction.operate(result, this.recursiveDeselected(subFeature));
					break;
				case UNSELECTED:
					if(fmodel.getParentTree(subFeature.getName()).getType()==TreeConstraintType.MANDATORY)
						result=this.termFunction.operate(result, this.recursiveSelected(subFeature));
					else
						result=this.termCriteria.operate(this.termFunction.operate(result, this.recursiveSelected(subFeature)),this.termFunction.operate(result, this.recursiveDeselected(subFeature)));
			}
		}
		return result;
	}
	
	protected double recursiveDeselected(Feature feature) {
		double result=(Double) feature.getAttribute(termObjective.getAttributeDeselected());
		for(Feature subFeature: fmodel.getChildren(feature.getName()))
			result=this.termFunction.operate(result, this.recursiveDeselected(subFeature));
		return result;
	}

}
