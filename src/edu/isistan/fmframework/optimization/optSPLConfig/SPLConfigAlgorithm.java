package edu.isistan.fmframework.optimization.optSPLConfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint;
import edu.isistan.fmframework.optimization.BasicAlgorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.objectiveFunctions.AdditionObjective;
import edu.isistan.fmframework.optimization.objectiveFunctions.LinearWeightedObjective;
import edu.isistan.fmframework.optimization.optSPLConfig.model.FM;
import edu.isistan.fmframework.optimization.optSPLConfig.model.Feature;
import edu.isistan.fmframework.optimization.optSPLConfig.solver.GreedyHeuristic;

public class SPLConfigAlgorithm implements BasicAlgorithm {

	FM fm;
	String budgetText;
	
	@Override
	public void preprocessInstance(BasicProblem instance) {
		budgetText = String.valueOf(instance.globalConstraints[0].restrictionLimit);
		
		double[] attributes;
		
		if(instance.objectiveFunctions[0] instanceof AdditionObjective){
			attributes=((AdditionObjective)instance.objectiveFunctions[0]).attributes;
		}else{
//			if(instance.objectiveFunctions[0] instanceof LinearWeightedObjective){
//				attributes=((AdditionObjective) ((LinearWeightedObjective) instance.objectiveFunctions[0]).getTerms().get(0).getKey()).attributes;
//			}else{
				throw new UnsupportedOperationException("Objective function "+instance.objectiveFunctions[0]+" is not supported");
//			}
		}
		
		fm = createSPLConfigFM(instance.model, attributes, instance.globalConstraints[0].attributes);
	}
	
	@Override
	public Configuration selectConfiguration(BasicProblem instance) {
		if (instance.globalConstraints.length != 1)
			throw new RuntimeException("instance must include one linear restriction");

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

	private FM createSPLConfigFM(FeatureModel model, double[] benefits, double[] costs) {

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

			feature.setBenefit(benefits[i]);
			feature.setCost(costs[i]);
//			feature.setBenefit(featureOrigin.getAttribute(idBenefit));
//			feature.setCost(featureOrigin.getAttribute(idCost));

			features.add(feature);
		}

		FM fm = new FM();
		fm.setFeatures(features);
		fm.setFeaturesGroups();
		return fm;
	}

}
