package edu.isistan.fmframework.optimization;

import java.util.LinkedList;
import java.util.List;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.Constraint;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagator;
import edu.isistan.fmframework.optimization.optCSA.constraintPropagator.ConstraintPropagators;
import edu.isistan.fmframework.optimization.optCSA.containers.Stack;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.StaticMostConstrained;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.TopDownUnassignedVariableSelector;
import edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic.UnassignedVariableSelector;

public class Utils {

	public static boolean isValid(FeatureModel model) {
		return isValid(new Problem(model));
	}
	
	public static boolean isValid(Problem<?,?> instance) {
		Configuration initial=ConstraintPropagators.clauseBasedConstraintPropagator.getPartialConfiguration(instance.model);

		if(initial!=null && instance.satisfyGlobalConstraints(initial)){
			UnassignedVariableSelector unassignedVariableSelector=new StaticMostConstrained();
			unassignedVariableSelector.setup(instance);
			Stack<Configuration> open=new Stack<>();
			open.push(initial);

			while (!open.isEmpty()) {
				Configuration current = open.pop();
				int unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
				if (unassignedVariable == UnassignedVariableSelector.NO_UNASSIGNED_VARIABLES) {
					return true;
				} else {
					Configuration succesor1 = (Configuration) current.clone();
					if (ConstraintPropagators.clauseBasedConstraintPropagator.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
							&& instance.satisfyGlobalConstraints(succesor1)) {
						open.push(succesor1);
					}
					Configuration succesor2 = (Configuration) current.clone();
					if (ConstraintPropagators.clauseBasedConstraintPropagator.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
							&& instance.satisfyGlobalConstraints(succesor2)) {
						open.push(succesor2);
					}
				}
			}
		}
		return false;
	}
	
	public static List<Configuration> list(FeatureModel model) {
		return list(new Problem(model));
	}
	
	public static List<Configuration> list(Problem<?,?> instance) {
		Configuration initialConfiguration =ConstraintPropagators.clauseBasedConstraintPropagator.getPartialConfiguration(instance.model);
		List<Configuration> list=new LinkedList<>();
		if(initialConfiguration!=null && instance.satisfyGlobalConstraints(initialConfiguration)){
			TopDownUnassignedVariableSelector unassignedVariableSelector=new TopDownUnassignedVariableSelector();
			unassignedVariableSelector.setup(instance);
			Stack<Configuration> open=new Stack<Configuration>();
			ConstraintPropagator cpropagator=ConstraintPropagators.clauseBasedConstraintPropagator;
			open.push(initialConfiguration);
			
			while (!open.isEmpty()) {
				Configuration current = open.pop();
				int unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
				if (unassignedVariable == UnassignedVariableSelector.NO_UNASSIGNED_VARIABLES) {
					list.add(current);
				} else {
					Configuration succesor1 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
							&& instance.satisfyGlobalConstraints(succesor1)) {
						open.push(succesor1);
					}
					Configuration succesor2 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
							&& instance.satisfyGlobalConstraints(succesor2)) {
						open.push(succesor2);
					}
				}
			}
		}
		return list;
	}

	public static long realCount(FeatureModel model) {
		return realCount(new Problem(model));
	}
	
	public static long realCount(Problem<?,?> instance) {
		Configuration initialConfiguration =ConstraintPropagators.clauseBasedConstraintPropagator.getPartialConfiguration(instance.model);
		long count=0;
		if(initialConfiguration!=null && instance.satisfyGlobalConstraints(initialConfiguration)){
			TopDownUnassignedVariableSelector unassignedVariableSelector=new TopDownUnassignedVariableSelector();
			unassignedVariableSelector.setup(instance);
			Stack<Configuration> open=new Stack<Configuration>();
			ConstraintPropagator cpropagator=ConstraintPropagators.clauseBasedConstraintPropagator;
			open.push(initialConfiguration);
			
			while (!open.isEmpty()) {
				Configuration current = open.pop();
				int unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
				if (unassignedVariable == UnassignedVariableSelector.NO_UNASSIGNED_VARIABLES) {
					count++;
				} else {
					Configuration succesor1 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
							&& instance.satisfyGlobalConstraints(succesor1)) {
						open.push(succesor1);
					}
					Configuration succesor2 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
							&& instance.satisfyGlobalConstraints(succesor2)) {
						open.push(succesor2);
					}
				}
			}
		}
		return count;
	}
	
	public static long realStatesCount(Problem<?,?> instance) {
		Configuration initialConfiguration = ConstraintPropagators.clauseBasedConstraintPropagator.getPartialConfiguration(instance.model);
		long stateCount=0;
		if(initialConfiguration!=null && instance.satisfyGlobalConstraints(initialConfiguration)){
			TopDownUnassignedVariableSelector unassignedVariableSelector=new TopDownUnassignedVariableSelector();
			unassignedVariableSelector.setup(instance);
			Stack<Configuration> open=new Stack<Configuration>();
			ConstraintPropagator cpropagator=ConstraintPropagators.cardinalityBasedConstraintPropagator;
			open.push(initialConfiguration);
			stateCount++;
			
			while (!open.isEmpty()) {
				Configuration current = open.pop();
				int unassignedVariable = unassignedVariableSelector.selectUnassignedVariable(current);
				if (unassignedVariable == UnassignedVariableSelector.NO_UNASSIGNED_VARIABLES) {
//					count++;
				} else {
					Configuration succesor1 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor1, unassignedVariable, FeatureState.SELECTED)
							&& instance.satisfyGlobalConstraints(succesor1)) {
						open.push(succesor1);
						stateCount++;
					}
					Configuration succesor2 = (Configuration) current.clone();
					if (cpropagator.assignFeature(succesor2, unassignedVariable, FeatureState.DESELECTED)
							&& instance.satisfyGlobalConstraints(succesor2)) {
						open.push(succesor2);
						stateCount++;
					}
				}
			}
		}
		return stateCount;
	}
	
	public static long aproxCount(FeatureModel model){
		return aproxCount(model,model.ROOT_ID);
	}
	
	public static long aproxCount(FeatureModel model,int featureId){
		long count=1;
		for(TreeConstraint treeConstraint:model.getChildrenTrees(featureId)){
			count *= aproxCount(model,treeConstraint);
		}
		return count;
	}
	
	private static long aproxCount(FeatureModel model, TreeConstraint treeConstraint) {
		long count=0;
		long subcounts[]=new long[treeConstraint.getChildren().length];
		for(int i=0;i<treeConstraint.getChildren().length;i++){
			subcounts[i] = aproxCount(model,treeConstraint.getChildren()[i]);
		}
		for(int i=treeConstraint.getMinCardinality();i<=treeConstraint.getMaxCardinality();i++){
			count+=aproxCount(subcounts,i,0);
		}
		return count;
	}

	private static long aproxCount(long[] groups, int number, int shift) {
		if(number==0){
			return 1;
		}else{
			long count=0;
			for(int i=shift;i<groups.length+1-number;i++){
				count+=groups[i]*aproxCount(groups,number-1,i+1);
			}
			return count;
		}
	}

	
}
