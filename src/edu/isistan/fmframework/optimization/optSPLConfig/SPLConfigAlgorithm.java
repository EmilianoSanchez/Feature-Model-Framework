package edu.isistan.fmframework.optimization.optSPLConfig;

import java.util.LinkedList;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import edu.isistan.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import edu.isistan.fmframework.core.constraints.treeConstraints.OptionalFeature;
import edu.isistan.fmframework.core.constraints.treeConstraints.OrGroup;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint;
import edu.isistan.fmframework.optimization.BasicAlgorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.optSPLConfig.model.FM;
import edu.isistan.fmframework.optimization.optSPLConfig.solver.GreedyHeuristic;

public class SPLConfigAlgorithm implements BasicAlgorithm {

	FM fm;
	String budgetText;
	
	@Override
	public void preprocessInstance(BasicProblem instance) {
		if (instance.objectiveFunctions.length > 1 || instance.objectiveFunctions.length == 0 || 
				instance.globalConstraints.length > 1 || instance.objectiveFunctions.length == 0)
			throw new IllegalArgumentException(
					"SPLConfigAlgorithm only supports instances with a single additive objective function and a single linear inequality restriction");
		
		budgetText = String.valueOf(instance.globalConstraints[0].restrictionLimit);
		fm = createSPLConfigFM(instance);
	}
	
	@Override
	public Configuration selectConfiguration(BasicProblem instance) {
//		if (instance.globalConstraints.length != 1)
//			throw new RuntimeException("instance must include one linear restriction");

		GreedyHeuristic algorithm = new GreedyHeuristic(fm, budgetText);
		
		if(algorithm.getResult().isEmpty()){
			return null;
		}else{
			Configuration conf=new Configuration(instance.model,FeatureState.DESELECTED);
			
//			Map<String,Integer> featuresByName=new HashMap<>();
//			for(int i=0;i<instance.model.getNumFeatures();i++){
//				featuresByName.put(instance.model.getFeature(i).toString(), i);
//			}
//			for (int feature : algorithm.getResult()){
//				if(featuresByName.get(feature)!=null)
//					conf.setFeatureState(featuresByName.get(feature), true);
//			}
			for (int feature : algorithm.getResult()){
				conf.setFeatureState(feature, true);
			}
			return conf;
		}
	}

	private FM createSPLConfigFM(BasicProblem instance) {

		LinkedList<edu.isistan.fmframework.optimization.optSPLConfig.model.Feature> features = new LinkedList<edu.isistan.fmframework.optimization.optSPLConfig.model.Feature>();
		for (int i = 0; i < instance.model.getNumFeatures(); i++) {

//			Object featureOrigin = instance.model.getFeature(i);

			edu.isistan.fmframework.optimization.optSPLConfig.model.Feature feature = new edu.isistan.fmframework.optimization.optSPLConfig.model.Feature();
			feature.setName(i);

			if (instance.model.getParentTree(i) != null) {
				TreeConstraint parentTree = instance.model.getParentTree(i);
				feature.setFather(parentTree.getParent());

				
				if (parentTree.getClass() == OrGroup.class) {
					feature.setMandatory(false);
					feature.setAlt(1);
				}else{
					if (parentTree.getClass() == AlternativeGroup.class) {
						feature.setMandatory(false);
						feature.setAlt(2);
					}else{
						if (parentTree.getClass() == MandatoryFeature.class) {
							feature.setMandatory(true);
							feature.setAlt(0);
						}else{
							if (parentTree.getClass() == OptionalFeature.class) {
								feature.setMandatory(false);
								feature.setAlt(0);
							}
						}
						
					}
				}
			}

			feature.setBenefit(instance.objectiveFunctions[0].attributes[i]);
			feature.setCost(instance.globalConstraints[0].attributes[i]);

			features.add(feature);
		}

		FM fm = new FM();
		fm.setFeatures(features);
		fm.setFeaturesGroups();
		return fm;
	}

}
