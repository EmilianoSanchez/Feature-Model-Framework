package edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.optimization.Problem;

public class TopDownUnassignedVariableSelector implements UnassignedVariableSelector<Problem<?,?>>{

	
	@Override
	public int selectUnassignedVariable(Configuration conf) {
		for(int i=0;i<conf.getNumFeatures();i++){
			if(conf.getFeatureState(i)==FeatureState.UNSELECTED)
				return i;
		}
		return NO_UNASSIGNED_VARIABLES;
	}

	@Override
	public void setup(Problem<?,?> instance) {}

}
