package edu.isistan.fmframework.optimization.optCSA.heuristicFunctions;

import java.util.AbstractMap;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.aggregateFunctions.AggregateFunction;
import edu.isistan.fmframework.optimization.aggregateFunctions.MaximumFunction;
import edu.isistan.fmframework.optimization.aggregateFunctions.MinimumFunction;
import edu.isistan.fmframework.optimization.objectiveFunctions.AggregateObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.LinearWeightedObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class HeuristicA implements HeuristicFunction<Problem<?, ?>>{

	protected static final AggregateFunction CRITERIA_MAXIMISE=new MaximumFunction();
	protected static final AggregateFunction CRITERIA_MINIMISE=new MinimumFunction();

	protected Configuration conf;	
	protected FeatureModel fmodel;
	protected LinearWeightedObjective targetObjective;
	
	protected AggregateObjective termObjective;
	protected AggregateFunction termFunction;
	protected AggregateFunction termCriteria;
	
	@Override
	public void setup(Problem<?, ?> instance) {
		this.fmodel=instance.model;
		
		if(instance.objectiveFunctions[0] instanceof AggregateObjective){
			this.targetObjective=new LinearWeightedObjective(LinearWeightedObjective.MAXIMIZE,(AggregateObjective)instance.objectiveFunctions[0]);
		}else{
			if(instance.objectiveFunctions[0] instanceof LinearWeightedObjective)
				this.targetObjective=(LinearWeightedObjective) instance.objectiveFunctions[0];
			else
				throw new RuntimeException("The objective function type is not supported");
		}
	}

	@Override
	public double evaluate(Configuration configuration) {

		this.conf=configuration;
		
		double result = 0.0;
		for (AbstractMap.SimpleEntry<AggregateObjective, Double> term : targetObjective.getTerms()){
			this.termObjective=term.getKey();
			this.termFunction=this.termObjective.getFunction();
			if(term.getValue()>0.0)
				this.termCriteria=CRITERIA_MINIMISE;
			else
				this.termCriteria=CRITERIA_MAXIMISE;
			result += this.recursiveSelected(FeatureModel.ROOT_ID) * term.getValue();
		}
		return result;
	}

	protected double recursiveSelected(int featureId) {
//		Feature feature=this.fmodel.getFeature(featureId);
//		double result=feature.getAttribute(termObjective.getAttributeSelected());
		
		double result=termObjective.attributes[featureId];

		for(int subFeatureId: fmodel.getChildren(featureId)){
			switch(conf.getFeatureState(subFeatureId)){
				case SELECTED:
					result=this.termFunction.operate(result, this.recursiveSelected(subFeatureId));
					break;
				case UNSELECTED:
					if(fmodel.getParentTree(subFeatureId).getClass() == MandatoryFeature.class){
						result=this.termFunction.operate(result, this.recursiveSelected(subFeatureId));
					}else{
						result=this.termCriteria.operate(this.termFunction.operate(result, this.recursiveSelected(subFeatureId)),result);
					}
					break;
				default:
					break;
			}
		}
		return result;
	}

}
