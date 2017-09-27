package com.emiliano.fmframework.optimization;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.operations.ConfOperations;
import com.emiliano.fmframework.optimization.inequalityRestrictions.NoRestriction;
import com.emiliano.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class ConfigurationSelectionInstance {
	
	public ConfigurationSelectionInstance(FeatureModel model){
		this(ConfOperations.getPartialConfiguration(model));
	};
	
	public ConfigurationSelectionInstance(FeatureModel model,ObjectiveFunction objective){
		this(ConfOperations.getPartialConfiguration(model),objective);
	};
	
	public ConfigurationSelectionInstance(FeatureModel model,ObjectiveFunction objective,IConstraint restriction){
		this(ConfOperations.getPartialConfiguration(model),objective,restriction);
	};
	
	public ConfigurationSelectionInstance(Configuration configuration){
		this(configuration,null,null);
	};
	
	public ConfigurationSelectionInstance(Configuration configuration,ObjectiveFunction objective){
		this(configuration,objective,null);
	};
	
	public ConfigurationSelectionInstance(Configuration initialConfiguration,ObjectiveFunction objective,IConstraint restriction){
		this.initialConfiguration=initialConfiguration;
		if(objective==null){
			this.objective=new ObjectiveFunction() {
				@Override
				public double evaluate(Configuration configuration) {
					return 0;
				}
			};
		}else{
			this.objective=objective;
		}
		if(restriction==null)
			this.restriction=new NoRestriction();
		else
			this.restriction=restriction;
	};
	
	public Configuration initialConfiguration;
	public ObjectiveFunction objective;
	public IConstraint restriction;
	
//	@Override
//	public String toString() {
//		StringBuilder builder=new StringBuilder();
//		builder.append(this.initialConfiguration.getModel().toString())
//		return super.toString();
//	}
}
