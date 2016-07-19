package com.emiliano.fmframework.reasoning.csa.heuristicFunctions;

import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureState;
import com.emiliano.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OptionalFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OrGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint;
import com.emiliano.fmframework.reasoning.aggregateFunctions.AdditionFunction;
import com.emiliano.fmframework.reasoning.aggregateFunctions.MaximumFunction;
import com.emiliano.fmframework.reasoning.aggregateFunctions.ProductFunction;

public class HeuristicB extends HeuristicA {

	@Override
	protected double recursiveSelected(Feature feature) {
		double result = (Double) feature.getAttribute(termObjective.getAttributeSelected());

		for (TreeConstraint treeConstraint : fmodel.getChildrenTrees(feature.getName())) {
			switch (treeConstraint.getType()) {
			case OR_GROUP:
				result = recursiveOrGroup(result, (OrGroup) treeConstraint);
			case ALTERNATIVE_GROUP:
				result = recursiveAlternativeGroup(result, (AlternativeGroup) treeConstraint);
			case MANDATORY:
				result = recursiveMandatoryFeature(result, (MandatoryFeature) treeConstraint);
			case OPTIONAL:
				result = recursiveOptionalFeature(result, (OptionalFeature) treeConstraint);
			}

		}
		return result;
	}

	private double recursiveMandatoryFeature(double result, MandatoryFeature treeConstraint) {
		Feature subFeature = fmodel.getFeature(treeConstraint.getChild());
		switch (conf.getFeatureState(subFeature)) {
		case SELECTED:
			result = this.termFunction.operate(result, this.recursiveSelected(subFeature));
			break;
		case DESELECTED:
			result = this.termFunction.operate(result, this.recursiveDeselected(subFeature));
			break;
		case UNSELECTED:
			result = this.termFunction.operate(result, this.recursiveSelected(subFeature));
			break;
		}
		return result;
	}

	private double recursiveOptionalFeature(double result, OptionalFeature treeConstraint) {
		Feature subFeature = fmodel.getFeature(treeConstraint.getChild());
		switch (conf.getFeatureState(subFeature)) {
		case SELECTED:
			result = this.termFunction.operate(result, this.recursiveSelected(subFeature));
			break;
		case DESELECTED:
			result = this.termFunction.operate(result, this.recursiveDeselected(subFeature));
			break;
		case UNSELECTED:
			result = this.termCriteria.operate(
					this.termFunction.operate(result, this.recursiveSelected(subFeature)),
					this.termFunction.operate(result, this.recursiveDeselected(subFeature)));
			break;
		}
		return result;
	}

	private double recursiveAlternativeGroup(double result, AlternativeGroup treeConstraint) {
		if(this.termFunction instanceof AdditionFunction || this.termFunction instanceof ProductFunction){
			return recursiveAlternativeGroupAddProd(result,treeConstraint);
		}else{
			if(this.termFunction.getClass().equals(this.termCriteria.getClass())){
				return recursiveAltOrGroupMMmm(result,treeConstraint);
			}else{
				if(this.termFunction instanceof MaximumFunction){
					return recursiveAlternativeGroupMaximumMin(result,treeConstraint);
				}else{
					return recursiveAlternativeGroupMinimumMax(result,treeConstraint);
				}
			}	
		}
	}

	private double recursiveOrGroup(double result, OrGroup treeConstraint) {
		if(this.termFunction instanceof AdditionFunction || this.termFunction instanceof ProductFunction){
			return recursiveOrGroupAddProd(result,treeConstraint);
		}else{
			if(this.termFunction.getClass().equals(this.termCriteria.getClass())){
				return recursiveAltOrGroupMMmm(result,treeConstraint);
			}else{
				if(this.termFunction instanceof MaximumFunction){
					return recursiveOrGroupMaximumMin(result,treeConstraint);
				}else{
					return recursiveOrGroupMinimumMax(result,treeConstraint);
				}
			}	
		}
	}
	
	private double recursiveOrGroupMinimumMax(double result, OrGroup treeConstraint) {

		double value_selected,value_deselected;
        boolean at_least_one_selected=false;
        double minimum_unselected=Double.MAX_VALUE;
        String index_max_min_selected = null;
        double max_min_selected = Double.MIN_VALUE;
        
		for(String subFeatureId: treeConstraint.getChildren()){
			Feature subFeature=this.fmodel.getFeature(subFeatureId);
			switch(conf.getFeatureState(subFeatureId)){
				case SELECTED:
					result=Math.min(result, this.recursiveSelected(subFeature));
					at_least_one_selected=true;
					break;
				case DESELECTED:
					result=Math.min(result, this.recursiveDeselected(subFeature));
					break;
				case UNSELECTED:
                    value_selected=this.recursiveSelected(subFeature);
                    value_deselected=this.recursiveDeselected(subFeature);
                    if(value_selected<value_deselected){
                        at_least_one_selected=true;
                    }else{
                        if(max_min_selected<value_selected){
                        	max_min_selected=value_selected;
                        	index_max_min_selected=subFeatureId;
                        }
                    }
                    minimum_unselected=Math.min(minimum_unselected,Math.max(value_selected,value_deselected));
			}
		}
		
		if(at_least_one_selected==false){
			for(String subFeatureId: treeConstraint.getChildren()){
				Feature subFeature=this.fmodel.getFeature(subFeatureId);
                if(conf.getFeatureState(subFeatureId)==FeatureState.UNSELECTED){
                    if(subFeatureId==index_max_min_selected)
                    	result=Math.min(result,this.recursiveSelected(subFeature));
                    else
                    	result=Math.min(result,this.recursiveDeselected(subFeature));
                }
			}
        }else
            result=Math.min(result,minimum_unselected);
		
		return result;
	}

	private double recursiveOrGroupMaximumMin(double result, OrGroup treeConstraint) {
		
		double value_selected,value_deselected;
        boolean at_least_one_selected=false;
        double maximum_unselected=Double.MIN_VALUE;
        String index_min_max_selected = null;
        double min_max_selected = Double.MAX_VALUE;
        
		for(String subFeatureId: treeConstraint.getChildren()){
			Feature subFeature=this.fmodel.getFeature(subFeatureId);
			switch(conf.getFeatureState(subFeatureId)){
				case SELECTED:
					result=Math.max(result, this.recursiveSelected(subFeature));
					at_least_one_selected=true;
					break;
				case DESELECTED:
					result=Math.max(result, this.recursiveDeselected(subFeature));
					break;
				case UNSELECTED:
                    value_selected=this.recursiveSelected(subFeature);
                    value_deselected=this.recursiveDeselected(subFeature);
                    if(value_selected<value_deselected){
                        at_least_one_selected=true;
                    }else{
                        if(min_max_selected>value_selected){
                            min_max_selected=value_selected;
                            index_min_max_selected=subFeatureId;
                        }
                    }
                    maximum_unselected=Math.max(maximum_unselected,Math.min(value_selected,value_deselected));
			}
		}
		
		if(at_least_one_selected==false){
			for(String subFeatureId: treeConstraint.getChildren()){
				Feature subFeature=this.fmodel.getFeature(subFeatureId);
                if(conf.getFeatureState(subFeatureId)==FeatureState.UNSELECTED){
                    if(subFeatureId==index_min_max_selected)
                    	result=Math.max(result,this.recursiveSelected(subFeature));
                    else
                    	result=Math.max(result,this.recursiveDeselected(subFeature));
                }
			}
        }else
            result=Math.max(result,maximum_unselected);
		
		return result;
	}

	private double recursiveAlternativeGroupMinimumMax(double result, AlternativeGroup treeConstraint) {
		
		double value_selected,value_deselected;
        boolean at_least_one_selected=false;
        String index_max_min_selected = null;
        double max_min_selected = Double.MIN_VALUE;
        String index_min_min_deselected = null;
        double min_min_deselected = Double.MAX_VALUE;

		for(String subFeatureId: treeConstraint.getChildren()){
			Feature subFeature=this.fmodel.getFeature(subFeatureId);
			switch(conf.getFeatureState(subFeatureId)){
				case SELECTED:
					result=Math.min(result, this.recursiveSelected(subFeature));
					at_least_one_selected=true;
					break;
				case DESELECTED:
					result=Math.min(result, this.recursiveDeselected(subFeature));
					break;
				case UNSELECTED:
                    value_selected=this.recursiveSelected(subFeature);
                    value_deselected=this.recursiveDeselected(subFeature);
                    if(value_selected<value_deselected){
                        if(max_min_selected<value_selected){
                        	max_min_selected=value_selected;
                        	index_max_min_selected=subFeatureId;
                        }
                    }else{
                        if(min_min_deselected>value_deselected){
                        	min_min_deselected=value_deselected;
                        	index_min_min_deselected=subFeatureId;
                        }
                    }
			}
		}
		
        if(at_least_one_selected==false){
        	String index_to_select;
            if(index_min_min_deselected== null)
                index_to_select=index_max_min_selected;
            else
                index_to_select=index_min_min_deselected;
    		for(String subFeatureId: treeConstraint.getChildren()){
    			Feature subFeature=this.fmodel.getFeature(subFeatureId);
            	if(conf.getFeatureState(subFeatureId)==FeatureState.UNSELECTED){
            		if(subFeatureId==index_to_select)
                        result=Math.min(result,this.recursiveSelected(subFeature));
                    else
                    	result=Math.min(result,this.recursiveDeselected(subFeature));
                }
            }
        }
        
		return result;
	}

	private double recursiveAlternativeGroupMaximumMin(double result, AlternativeGroup treeConstraint) {

		double value_selected,value_deselected;
        boolean at_least_one_selected=false;
        String index_min_max_selected = null;
        double min_max_selected = Double.MAX_VALUE;
        String index_max_max_deselected = null;
        double max_max_deselected = Double.MIN_VALUE;
        
		for(String subFeatureName: treeConstraint.getChildren()){
			Feature subFeature=this.fmodel.getFeature(subFeatureName);
			switch(conf.getFeatureState(subFeatureName)){
				case SELECTED:
					result=Math.max(result, this.recursiveSelected(subFeature));
					at_least_one_selected=true;
					break;
				case DESELECTED:
					result=Math.max(result, this.recursiveDeselected(subFeature));
					break;
				case UNSELECTED:
                    value_selected=this.recursiveSelected(subFeature);
                    value_deselected=this.recursiveDeselected(subFeature);
                    if(value_selected>value_deselected){
                        if(min_max_selected>value_selected){
                            min_max_selected=value_selected;
                            index_min_max_selected=subFeatureName;
                        }
                    }else{
                        if(max_max_deselected<value_deselected){
                            max_max_deselected=value_deselected;
                            index_max_max_deselected=subFeatureName;
                        }
                    }
			}
		}
		
        if(at_least_one_selected==false){
            String index_to_select;
            if(index_max_max_deselected== null)
                index_to_select=index_min_max_selected;
            else
                index_to_select=index_max_max_deselected;
            for(String subFeatureName: treeConstraint.getChildren()){
            	if(conf.getFeatureState(subFeatureName)==FeatureState.UNSELECTED){
            		Feature subFeature=this.fmodel.getFeature(subFeatureName);
            		if(subFeatureName==index_to_select)
                        result=Math.max(result,this.recursiveSelected(subFeature));
                    else
                    	result=Math.max(result,this.recursiveDeselected(subFeature));
                }
            }
        }
        
		return result;
	}
	
	private double recursiveAltOrGroupMMmm(double result, TreeConstraint treeConstraint) {
		for(String subFeatureName: treeConstraint.getChildren()){
			Feature subFeature=this.fmodel.getFeature(subFeatureName);
			switch(conf.getFeatureState(subFeatureName)){
				case SELECTED:
					result=this.termFunction.operate(result, this.recursiveSelected(subFeature));
					break;
				case DESELECTED:
					result=this.termFunction.operate(result, this.recursiveDeselected(subFeature));
					break;
				case UNSELECTED:
					result=this.termCriteria.operate(result,this.recursiveSelected(subFeature));
					result=this.termCriteria.operate(result,this.recursiveDeselected(subFeature));
			}
		}
		return result;
	}

	private double recursiveAlternativeGroupAddProd(double result, AlternativeGroup treeConstraint) {
		boolean at_least_one_selected=false;
        double difference=this.termCriteria.getNeutralElement();
        
		for(String subFeatureName: treeConstraint.getChildren()){
			Feature subFeature=this.fmodel.getFeature(subFeatureName);
			switch(conf.getFeatureState(subFeatureName)){
				case SELECTED:
					result=this.termFunction.operate(result, this.recursiveSelected(subFeature));
					at_least_one_selected=true;
					break;
				case DESELECTED:
					result=this.termFunction.operate(result, this.recursiveDeselected(subFeature));
					break;
				case UNSELECTED:
					double value_selected=this.recursiveSelected(subFeature);
					double value_deselected=this.recursiveDeselected(subFeature);
					difference=this.termCriteria.operate(difference, this.termFunction.inverseOperator(value_selected, value_deselected));
					result=this.termFunction.operate(result, value_deselected);   
			}
		}
		if(at_least_one_selected==false){
			result=this.termFunction.operate(result, difference);
        }
		return result;
	}

	private double recursiveOrGroupAddProd(double result, OrGroup treeConstraint) {
		
		boolean at_least_one_selected=false;
        double difference=this.termCriteria.getNeutralElement();
        
        for(String subFeatureName: treeConstraint.getChildren()){
			Feature subFeature=this.fmodel.getFeature(subFeatureName);
			switch(conf.getFeatureState(subFeatureName)){
				case SELECTED:
					result=this.termFunction.operate(result, this.recursiveSelected(subFeature));
					at_least_one_selected=true;
					break;
				case DESELECTED:
					result=this.termFunction.operate(result, this.recursiveDeselected(subFeature));
					break;
				case UNSELECTED:
					double value_selected=this.recursiveSelected(subFeature);
					double value_deselected=this.recursiveDeselected(subFeature);
					difference=this.termCriteria.operate(difference, this.termFunction.inverseOperator(value_selected, value_deselected));
                    if(value_selected== this.termCriteria.operate(value_selected,value_deselected))
                        at_least_one_selected=true;
                    result=this.termFunction.operate(result, this.termCriteria.operate(value_selected,value_deselected));   
			}
		}
		if(at_least_one_selected==false){
            result=this.termFunction.operate(result, difference);
        }
		return result;
	}

}
