package edu.isistan.fmframework.optimization.optSPLConfigOriginal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.globalConstraints.InequalityRestriction;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint;
import edu.isistan.fmframework.optimization.BasicAlgorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;
import edu.isistan.fmframework.optimization.optSPLConfigOriginal.model.FM;
import edu.isistan.fmframework.optimization.optSPLConfigOriginal.model.Feature;
import edu.isistan.fmframework.optimization.optSPLConfigOriginal.solver.GreedyHeuristic;


public class SPLConfigAlgorithmOriginal implements BasicAlgorithm {

	FM fm;
	String budgetText;
	
	@Override
	public void preprocessInstance(BasicProblem instance) {
		budgetText = String.valueOf(instance.globalConstraints[0].restrictionLimit);
		fm = createSPLConfigFM(instance.model, instance.objectiveFunctions[0], instance.globalConstraints[0]);
	}
	
	@Override
	public Configuration selectConfiguration(BasicProblem instance) {
		if (instance.globalConstraints.length != 1)
			throw new RuntimeException("Instance must include one linear restriction");

		GreedyHeuristic algorithm = new GreedyHeuristic(fm, budgetText);
		
		if(algorithm.getResult().isEmpty()){
			return null;
		}else{
			Configuration conf=new Configuration(instance.model,FeatureState.DESELECTED);
			
			Map<String,Integer> featuresByName=new HashMap<>();
			for(int i=0;i<instance.model.getNumFeatures();i++){
				featuresByName.put(instance.model.getFeature(i).getName(), i);
			}
			for (String feature : algorithm.getResult()){
				if(featuresByName.get(feature)!=null)
					conf.setFeatureState(featuresByName.get(feature), true);
			}
			return conf;
		}
	}

	private FM createSPLConfigFM(FeatureModel model, AdditionObjective benefits, InequalityRestriction costs) {

		LinkedList<Feature> features = new LinkedList<Feature>();
		for (int i = 0; i < model.getNumFeatures(); i++) {

			edu.isistan.fmframework.core.Feature featureOrigin = model.getFeature(i);

			Feature feature = new Feature();
			feature.setName(featureOrigin.getName());

			if (model.getParentTree(i) != null) {
				TreeConstraint parentTree = model.getParentTree(i);
				feature.setFather(model.getFeature(parentTree.getParent()).getName());

				switch (parentTree.getType()) {
				case OR_GROUP:
					feature.setMandatory(false);
					feature.setAlt(1);
					break;
				case ALTERNATIVE_GROUP:
					feature.setMandatory(false);
					feature.setAlt(2);
					break;
				case MANDATORY:
					feature.setMandatory(true);
					feature.setAlt(0);
					break;
				case OPTIONAL:
					feature.setMandatory(false);
					feature.setAlt(0);
					break;

				}
			}

			feature.setBenefit(benefits.attributes[i]);
			feature.setCost(costs.attributes[i]);

			features.add(feature);
		}

		FM fm = new FM();
		fm.setFeatures(features);
		fm.setFeaturesGroups();
		return fm;
	}

}
