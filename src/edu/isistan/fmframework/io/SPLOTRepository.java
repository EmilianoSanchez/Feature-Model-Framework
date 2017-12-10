package edu.isistan.fmframework.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.AssignedValue;
import edu.isistan.fmframework.core.constraints.Clause;
import edu.isistan.fmframework.core.constraints.ClauseBasedConstraint;
import fm.FeatureModelException;

public class SPLOTRepository {

	public static final String PATH = "SPLOT-models";

	private static ArrayList<FeatureModel> models;

	public static ArrayList<FeatureModel> getModels() throws FeatureModelException {
		if (models == null) {
			loadModels();
		}
		return models;
	}

	public static List<FeatureModel> getModels(int from, int to) throws FeatureModelException {
		return getModels().subList(from,to);
	}

	private static void loadModels() throws FeatureModelException {
		File folder = new File(PATH);
		File[] files = folder.listFiles();
		models = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			FeatureModel model = SXFMtoFM.parse(file.getPath());
			
			model.addCrossTreeConstraint(new AssignedValue(FeatureModel.ROOT_ID,true));
			
			
			models.add(model);
		}
		
		Collections.sort(models, new Comparator<FeatureModel>() {
			@Override
			public int compare(FeatureModel o1, FeatureModel o2) {
				return Integer.compare(o1.getNumFeatures(), o2.getNumFeatures());
			}
		});
	}
	
	public static void transformModels(List<FeatureModel> models) {
		
	}
}
