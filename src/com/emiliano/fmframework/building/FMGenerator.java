package com.emiliano.fmframework.building;

import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.FeatureModelImpl;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.core.constraints.crossTreeConstraints.BinaryConstraint;
import com.emiliano.fmframework.core.constraints.crossTreeConstraints.Imply;
import com.emiliano.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OptionalFeature;
import com.emiliano.fmframework.core.constraints.treeConstraints.OrGroup;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint;
import com.emiliano.fmframework.optimization.ConfigurationSelectionInstance;
import com.emiliano.fmframework.optimization.inequalityRestrictions.MultipleInequalityRestriction;
import com.emiliano.fmframework.optimization.inequalityRestrictions.SingleInequalityRestriction;
import com.emiliano.fmframework.optimization.objectiveFunctions.AdditionObjective;
import com.emiliano.fmframework.optimization.objectiveFunctions.LinearWeightedObjective;
import com.emiliano.fmframework.optimization.objectiveFunctions.ObjectiveFunction;

public class FMGenerator {

	private int numFeatures;
	private FeatureModelType fmtype;
	private int numTreeConstraints;
	private int numCrossTreeConstraints;
	private int numResourceRestrictions;
	private IConstraint resourceRestrictions;
	private int numObjectives;
	private ObjectiveFunction objective;

	public FMGenerator() {
		this.numFeatures = 0;
		this.fmtype = FeatureModelType.RANDOM;
		this.numTreeConstraints = 0;
		this.numCrossTreeConstraints = 0;
		this.numResourceRestrictions = 0;
		this.resourceRestrictions = null;
		this.numObjectives = 0;
		this.objective = null;
	};

	public FMGenerator setNumFeatures(int numFeatures) {
		this.numFeatures = numFeatures;
		return this;
	}

	public FMGenerator setFMType(FeatureModelType fmtype) {
		this.fmtype = fmtype;
		return this;
	}

	public FMGenerator setNumTreeConstraints(int numTreeConstraints) {
		this.numTreeConstraints = numTreeConstraints;
		return this;
	}

	public FMGenerator setNumCrossTreeConstraints(int numCrossTreeConstraints) {
		this.numCrossTreeConstraints = numCrossTreeConstraints;
		return this;
	}

	public FMGenerator setNumResourceRestrictions(int numResourceRestrictions) {
		this.numResourceRestrictions = numResourceRestrictions;
		return this;
	}

	public FMGenerator setResourceRestrictions(IConstraint resourceRestrictions) {
		this.resourceRestrictions = resourceRestrictions;
		return this;
	}

	public FMGenerator setNumObjectives(int numObjectives) {
		this.numObjectives = numObjectives;
		return this;
	}

	public FMGenerator setObjective(ObjectiveFunction objective) {
		this.objective = objective;
		return this;
	}

	public static enum FeatureModelType {
		RANDOM, // for generating any kind of feature models
		ALL_AND, // for generating feature models with only AND groups
		ALL_OR, // for generating feature models with only OR groups
		ALL_XOR, // for generating feature models with only XOR groups
		ALL_AND_OPT// for generating feature models with only optional features
	};

	public static FeatureModel generateFeatureModel(int numFeatures, FeatureModelType fmtype, int numTreeConstraints,
			int numCrossTreeConstraints) {
		FeatureModel fm = generateFeatureModel(numFeatures);
		// fm.addAssignedValue(new AssignedValue(FMProblem.ROOT_ID,true));
		generateTreeConstraints(fm, fmtype, numTreeConstraints);
		generateCrossTreeConstraints(fm, numCrossTreeConstraints);
		return fm;
	}

	public static FeatureModel generateFeatureModel(int numFeatures) {
		FeatureModel fm = new FeatureModelImpl();
		for (int i = 0; i < numFeatures; i++)
			fm.addFeature(new Feature(String.valueOf(i)));
		return fm;
	}

	public static void generateTreeConstraints(FeatureModel fm, FeatureModelType fmtype, int numTreeConstraints) {
		if (numTreeConstraints > 0) {
			int children_per_feature = (fm.getNumFeatures() - 1) / numTreeConstraints;// branching
																						// factor
			int children_root = children_per_feature + (fm.getNumFeatures() - 1) % numTreeConstraints;
			int index_children = 1;

			generateTreeConstraint(fm, 0, index_children, children_root, fmtype);
			index_children += children_root;

			for (int h = 1; h < numTreeConstraints; h++) {
				generateTreeConstraint(fm, h, index_children, children_per_feature, fmtype);
				index_children += children_per_feature;
			}
		}
	}

	public static boolean generateCrossTreeConstraints(FeatureModel fm, int numCrossTreeConstraints) {

		BinaryConstraint constraint;

		int first_leaf = 0;
		for (int i = 0; i < fm.getNumFeatures(); i++) {
			if (!fm.hasChildren(String.valueOf(i))) {
				first_leaf = i;
				break;
			}
		}

		for (int i = 0; i < numCrossTreeConstraints; i++) {
			int left = first_leaf + (int) (Math.random() * (fm.getNumFeatures() - first_leaf));
			int right;
			do {
				right = first_leaf + (int) (Math.random() * (fm.getNumFeatures() - first_leaf));
			} while (right == left);
			constraint = new Imply(String.valueOf(left), String.valueOf(right));

			fm.addCrossTreeConstraint(constraint);
		}
		// validation of the feature model
		return true;
	}

	private static void generateTreeConstraint(FeatureModel fm, int index_parent, int index_children, int cant_children,
			FeatureModelType fmtype) {

		if (fmtype == FeatureModelType.RANDOM) {
			fmtype = FeatureModelType.values()[((int) Math.floor(Math.random() * 3.0)) + 1];
		}

		String parent = String.valueOf(index_parent);
		String[] children = new String[cant_children];
		for (int i = 0; i < cant_children; i++)
			children[i] = String.valueOf(index_children + i);

		TreeConstraint tc = null;
		switch (fmtype) {
		case ALL_AND:
			for (int i = 0; i < cant_children; i++) {
				if (Math.random() < 0.5) {
					fm.addTreeConstraint(new OptionalFeature(parent, children[i]));
				} else {
					fm.addTreeConstraint(new MandatoryFeature(parent, children[i]));
				}
			}
			break;
		case ALL_AND_OPT:
			for (int i = 0; i < cant_children; i++)
				fm.addTreeConstraint(new OptionalFeature(parent, children[i]));
			break;
		case ALL_OR:
			tc = new OrGroup(parent, children);
			fm.addTreeConstraint(tc);
			break;
		case ALL_XOR:
			tc = new AlternativeGroup(parent, children);
			fm.addTreeConstraint(tc);
			break;
		}

	}
	
	int aux=0;
	
	private String generateFeatureAttributes(FeatureModel fm){
		String attName=String.valueOf(aux);
		for(Feature feature:fm.getFeatures()){
			double randomValue=Math.random();
			feature.addAttribute(attName, randomValue);
		}
		aux++;
		return attName;
	}
	
	public FeatureModel generateFeatureModel(){
		return FMGenerator.generateFeatureModel(numFeatures, fmtype, numTreeConstraints, numCrossTreeConstraints);
	}

	public ConfigurationSelectionInstance generateConfigurationSelectionInstance() {
		FeatureModel fm = generateFeatureModel(this.numFeatures, this.fmtype, this.numTreeConstraints,
				this.numCrossTreeConstraints);

		if (resourceRestrictions == null && numResourceRestrictions>0)
			resourceRestrictions = generateMultipleInequalityRestriction(fm,
					this.numResourceRestrictions);
		if (objective == null && numObjectives>0)
			objective = generateLinearWeightedObjective(fm, this.numObjectives);

		return new ConfigurationSelectionInstance(fm,objective,resourceRestrictions);
	}
	
	public IConstraint generateMultipleInequalityRestriction(FeatureModel fm, int numRestrictions) {
		MultipleInequalityRestriction restriction=new MultipleInequalityRestriction();
		int numFeatures=fm.getNumFeatures();
		
		for (int i = 0; i < numRestrictions; i++) {
			double limit = numFeatures * 0.75;
			restriction.addRestriction(new SingleInequalityRestriction(generateFeatureAttributes(fm), generateFeatureAttributes(fm), limit));
			
		}
		return restriction;
	}

	public LinearWeightedObjective generateLinearWeightedObjective(FeatureModel fm, int numObjectives) {
		LinearWeightedObjective objective = new LinearWeightedObjective();
		
		for (int i = 0; i < numObjectives; i++) {
			double weight = Math.random();
			objective.addTerm(new AdditionObjective(generateFeatureAttributes(fm), generateFeatureAttributes(fm)), weight);
			
		}
		
		return objective;
	}

}
