package edu.isistan.fmframework.optimization.optCSA.variableOrderingHeuristic;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.optimization.Problem;

public class StaticMostConstrained   implements UnassignedVariableSelector<Problem<?,?>>{

	int [] variableorder;
	
	@Override
	public int selectUnassignedVariable(Configuration conf) {
		for(int i=0;i<variableorder.length;i++){
			if(conf.getFeatureState(variableorder[i])==FeatureState.UNSELECTED)
				return variableorder[i];
		}
		return NO_UNASSIGNED_VARIABLES;
	}

	@Override
	public void setup(Problem<?,?> instance) {
		if(instance.model!=null){
			
			java.util.PriorityQueue<Map.Entry<Integer, Integer>>  unassignedVariables=new java.util.PriorityQueue<Map.Entry<Integer, Integer>>(instance.model.getNumFeatures(),
					new Comparator<Map.Entry<Integer, Integer>>(){
						@Override
						public int compare(Entry<Integer, Integer> e1,
								Entry<Integer, Integer> e2) {
							if(e1.getValue()<e2.getValue())
								return 1;
							else
								if(e1.getValue()>e2.getValue())
									return -1;
								else
									return 0;
						}
					});
			
			
			for(int f=0;f<instance.model.getNumFeatures();f++){
				unassignedVariables.add(new AbstractMap.SimpleEntry<Integer, Integer>(f,instance.model.getClauses(f).size()));
			}
			
			variableorder=new int[instance.model.getNumFeatures()];
			int vorder=0;
			while(!unassignedVariables.isEmpty()){
				variableorder[vorder]=unassignedVariables.remove().getKey();
				vorder++;
			}
		}
	}

}