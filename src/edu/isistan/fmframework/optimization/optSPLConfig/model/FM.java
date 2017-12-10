package edu.isistan.fmframework.optimization.optSPLConfig.model;

import java.util.LinkedList;

public class FM extends Feature{

	private LinkedList<Feature> features = new LinkedList<Feature>();
	
	public FM(){
	}
	
	public LinkedList<Feature> getFeatures(){
		return features;
	}
	
	public LinkedList<Feature> getNonMandatoryFeatures(){
		LinkedList<Feature> result = new LinkedList<Feature>();
		for (Feature f : features)
			if (!f.isMandatory())
				result.add(f);
		return result;
	}
	
	public int getRootIndex(){
		int i;
		for (i=0;i<features.size();i++)
			if (features.get(i).getFather() == 0) break;
		
		return i;
	}
	
	public void setFeatures(LinkedList<Feature> features){
		this.features = features;
	}
	
	public void setFeaturesGroups(){
		for(int i=0;i<features.size();i++){
			int father = features.get(i).getFather();
			LinkedList<Integer> group = new LinkedList<Integer>();
			if(features.get(i).getAlt() > 0)
				for(int j=1;j<features.size();j++)
					if(features.get(j).getFather()==father && i!=j)
						group.add(features.get(j).getName());
			features.get(i).setGroup(group);
		}
	}
}
