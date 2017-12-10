package edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.optimization.Problem;

public class BottomUpUnassignedVariableSelector  implements UnassignedVariableSelector<Problem<?,?>>{

	
	@Override
	public int selectUnassignedVariable(Configuration conf) {
		for(int i=conf.getNumFeatures()-1;i>=0;i--){
			if(conf.getFeatureState(i)==FeatureState.UNSELECTED)
				return i;
		}
		return NO_UNASSIGNED_VARIABLES;
	}

	@Override
	public void setup(Problem<?,?> instance) {}

}
