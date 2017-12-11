package edu.isistan.fmframework.evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.optimization.BasicProblem;
import fm.FeatureModelException;

public class SPLOTModels {

	public static List<FeatureModel> getModels() throws FeatureModelException {
		return loadModels();
	}
	
	public static List<FeatureModel> getModels(int from, int to) throws FeatureModelException {
		return loadModels().subList(from, to);
	}
	
	public static BasicProblem[] generateBasicProblems(List<FeatureModel> models, int inequalityRestrictions) throws FeatureModelException{
		
		BasicProblem[] instances = new BasicProblem[models.size()];
		for (int i = 0; i < models.size(); i++) {
			instances[i] = BasicProblemGenerator
					.generateBasicProblemInstance(models.get(i), inequalityRestrictions);
		}

		return instances;
	}
	
	public static BasicProblem[][] generateValidBasicProblems(int rounds,
			List<FeatureModel> models, double[] restrictionRatios) throws FeatureModelException {
		BasicProblem[][] instances = new BasicProblem[rounds][];
		for (int r = 0; r < rounds; r++) {
			System.out.println("Generating ... round "+r);
			instances[r] = generateValidBasicProblems(models, restrictionRatios);
		}

		return instances;
	}
	
	public static BasicProblem[] generateValidBasicProblems(List<FeatureModel> models, double[] restrictionRatios) throws FeatureModelException{
		
		BasicProblem[] instances = new BasicProblem[models.size()];
		for (int i = 0; i < models.size(); i++) {
			System.out.println("Generating ... instance "+ i);
			instances[i] = BasicProblemGenerator
				.generateValidBasicProblemInstance(models.get(i), restrictionRatios);
		}

		return instances;
	}
	
	public static BasicProblem[] generateValidBasicProblems(List<FeatureModel> models, int inequalityRestrictions) throws FeatureModelException{
		
		BasicProblem[] instances = new BasicProblem[models.size()];
		for (int i = 0; i < models.size(); i++) {
			System.out.println("Generating ... instance "+ i);
			instances[i] = BasicProblemGenerator
				.generateValidBasicProblemInstance(models.get(i), inequalityRestrictions);
		}

		return instances;
	}
	
	
	public static BasicProblem[][] generateBasicProblems(int rounds,List<FeatureModel> models, int inequalityRestrictions) throws FeatureModelException{
		
		BasicProblem[][] instances = new BasicProblem[rounds][];
		for (int r = 0; r < rounds; r++) {
			instances[r] = generateBasicProblems(models, inequalityRestrictions);
		}

		return instances;
	}
	
	public static BasicProblem[][] generateValidBasicProblems(int rounds, List<FeatureModel> models,
			int inequalityRestrictions) throws FeatureModelException {
		BasicProblem[][] instances = new BasicProblem[rounds][];
		for (int r = 0; r < rounds; r++) {
			System.out.println("Generating ... round "+r);
			instances[r] = generateValidBasicProblems(models, inequalityRestrictions);
		}

		return instances;
	}

	private static List<FeatureModel> models;
	
	private static List<FeatureModel> loadModels() throws FeatureModelException {
		
		if(models==null){
			
			File folder = new File("SPLOT-models");
			File[] files = folder.listFiles();
			models = new ArrayList<>();
			for (int i = 0; i < files.length; i++) {
	
				File file = files[i];
				FeatureModel model = SXFMtoFM.parse(file.getPath(), true);
				models.add(model);
			}
			
			Collections.sort(models, new Comparator<FeatureModel>() {
				@Override
				public int compare(FeatureModel o1, FeatureModel o2) {
					return Integer.compare(o1.getNumFeatures(), o2.getNumFeatures());
				}
			});
			
		}
		return models;
	}

}
