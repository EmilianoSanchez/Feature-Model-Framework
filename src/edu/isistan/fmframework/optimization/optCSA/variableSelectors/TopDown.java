package edu.isistan.fmframework.optimization.optCSA.variableSelectors;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.optimization.Problem;

public class TopDown implements VariableSelector<Problem<?,?>>{

	
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
