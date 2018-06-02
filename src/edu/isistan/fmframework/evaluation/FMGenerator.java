package edu.isistan.fmframework.evaluation;

import java.util.Set;
import java.util.TreeSet;

import edu.isistan.fmframework.core.Feature;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.AssignedValue;
import edu.isistan.fmframework.core.constraints.Clause;
import edu.isistan.fmframework.core.constraints.ClauseBasedConstraint;
import edu.isistan.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import edu.isistan.fmframework.core.constraints.treeConstraints.CardinalityGroup;
import edu.isistan.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import edu.isistan.fmframework.core.constraints.treeConstraints.OptionalFeature;
import edu.isistan.fmframework.core.constraints.treeConstraints.OrGroup;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint;
import edu.isistan.fmframework.utils.RandomUtils;

public class FMGenerator {

	private int numFeatures;
	private FeatureModelType fmtype;
	private int numTreeConstraints;
	private CrossTreeConstraintType ctctype;
	private int numCrossTreeConstraints;

	public FMGenerator() {
		this.numFeatures = 0;
		this.fmtype = FeatureModelType.RANDOM;
		this.numTreeConstraints = 0;
		this.ctctype = new CrossTreeConstraintType();
		this.numCrossTreeConstraints = 0;
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

	public FMGenerator setCTCType(CrossTreeConstraintType ctctype) {
		this.ctctype = ctctype;
		return this;
	}

	public FMGenerator setNumCrossTreeConstraints(int numCrossTreeConstraints) {
		this.numCrossTreeConstraints = numCrossTreeConstraints;
		return this;
	}

	public static enum FeatureModelType {
		RANDOM, // for generating any kind of feature models
		AND, // for generating feature models with only AND groups (optional and mandatory
				// features)
		OR, // for generating feature models with only OR groups
		XOR, // for generating feature models with only XOR groups
		AND_OPT, // for generating feature models with only optional features
		AND_MAND, // for generating feature models with only mandatory features
		CARDINALITY
	};

	public static class CrossTreeConstraintType {
		public int minliterals, maxliterals;

		public CrossTreeConstraintType(int minliterals, int maxliterals) {
			this.minliterals = minliterals;
			this.maxliterals = maxliterals;
		}

		public CrossTreeConstraintType(int literals) {
			this(literals, literals);
		}

		public CrossTreeConstraintType() {
			this(2);
		}

		public static final CrossTreeConstraintType BINARY_CONSTRAINT = new CrossTreeConstraintType();
		public static final CrossTreeConstraintType _3_CLAUSE_CONSTRAINT = new CrossTreeConstraintType(3);
	};

	public static enum AggregateFunctionType {
		RANDOM, ALL_ADDITION_PRODUCT, ALL_ADDITION, ALL_PRODUCT, ALL_MAXIMUM_MINIMUM, ALL_MAXIMUM, ALL_MINIMUM
	};

	public static FeatureModel generateFeatureModel(int numFeatures, double[] treeConstraintTypeDistribution,
			int numTreeConstraints, CrossTreeConstraintType ctctype, int numCrossTreeConstraints) {

		FeatureModelType fmtypes[] = new FeatureModelType[numTreeConstraints];
		for (int i = 0; i < numTreeConstraints; i++)
			fmtypes[i] = FeatureModelType.values()[RandomUtils.randomFromRoulette(treeConstraintTypeDistribution)];

		CrossTreeConstraintType ctctypes[] = new CrossTreeConstraintType[numCrossTreeConstraints];
		for (int i = 0; i < numCrossTreeConstraints; i++)
			ctctypes[i] = ctctype;

		return generateFeatureModel(numFeatures, fmtypes, ctctypes);
	}

	public static FeatureModel generateFeatureModel(int numFeatures, FeatureModelType fmtype, int numTreeConstraints,
			CrossTreeConstraintType ctctype, int numCrossTreeConstraints) {

		FeatureModelType fmtypes[] = new FeatureModelType[numTreeConstraints];
		for (int i = 0; i < numTreeConstraints; i++)
			fmtypes[i] = fmtype;

		CrossTreeConstraintType ctctypes[] = new CrossTreeConstraintType[numCrossTreeConstraints];
		for (int i = 0; i < numCrossTreeConstraints; i++)
			ctctypes[i] = ctctype;

		return generateFeatureModel(numFeatures, fmtypes, ctctypes);
	}

	public static FeatureModel generateFeatureModel(int numFeatures, FeatureModelType[] fmtypes,
			CrossTreeConstraintType[] ctctypes) {

		FeatureModel fm = generateFeatureModel(numFeatures);
		fm.addCrossTreeConstraint(new AssignedValue(0, true));
		generateTreeConstraints(fm, fmtypes);
		generateCrossTreeConstraints(fm, ctctypes);
		return fm;
	}

	public static FeatureModel generateFeatureModel(int numFeatures) {
		FeatureModel fm = new FeatureModel(numFeatures);
		for (int i = 0; i < numFeatures; i++)
			fm.addFeature(i, new Feature(String.valueOf(i)));
		return fm;
	}

	public static void generateTreeConstraints(FeatureModel fm, FeatureModelType[] fmtypes) {
		int numTreeConstraints = fmtypes.length;
		if (numTreeConstraints > 0) {
			int children_per_feature = (fm.getNumFeatures() - 1) / numTreeConstraints;// branching
																						// factor
			int children_root = children_per_feature + (fm.getNumFeatures() - 1) % numTreeConstraints;
			int index_children = 1;

			generateTreeConstraint(fm, 0, index_children, children_root, fmtypes[0]);
			index_children += children_root;

			for (int h = 1; h < numTreeConstraints; h++) {
				generateTreeConstraint(fm, h, index_children, children_per_feature, fmtypes[h]);
				index_children += children_per_feature;
			}
		}
	}

	public static boolean generateCrossTreeConstraints(FeatureModel fm, CrossTreeConstraintType[] ctctypes) {

		int first_leaf = 0;
		for (int i = 0; i < fm.getNumFeatures(); i++) {
			if (!fm.hasChildren(i)) {
				first_leaf = i;
				break;
			}
		}

		for (int i = 0; i < ctctypes.length; i++) {
			ClauseBasedConstraint constraint = generateCrossTreeConstraint(first_leaf, fm.getNumFeatures(),
					RandomUtils.randomRange(ctctypes[i].minliterals, ctctypes[i].maxliterals + 1));
			fm.addCrossTreeConstraint(constraint);
		}
		// validation of the feature model
		return true;
	}

	private static ClauseBasedConstraint generateCrossTreeConstraint(int idlower, int idupper, int numliterals) {

		// if(numliterals==2){
		// int left = RandomUtils.randomRange(idlower, idupper);//first_leaf + (int)
		// (Math.random() * (fm.getNumFeatures() - first_leaf));
		// int right;
		// do {
		// right = RandomUtils.randomRange(idlower, idupper);//first_leaf + (int)
		// (Math.random() * (fm.getNumFeatures() - first_leaf));
		// } while (right == left);
		// return new Imply(left, right);
		//
		// }else{
		Set<Integer> literals = new TreeSet<>();
		Clause constraint = new Clause();
		do {
			int literal = RandomUtils.randomRange(idlower, idupper);
			if (literals.add(literal)) {
				constraint.add(literal, RandomUtils.random.nextBoolean());
			}
		} while (literals.size() < numliterals);

		return constraint;
		// }
	}

	private static void generateTreeConstraint(FeatureModel fm, int index_parent, int index_children, int cant_children,
			FeatureModelType fmtype) {

		if (fmtype == FeatureModelType.RANDOM) {
			fmtype = FeatureModelType.values()[RandomUtils.randomRange(1, 4)];// ((int) Math.floor(Math.random() * 3.0))
																				// + 1];
		}

		int[] children = new int[cant_children];
		for (int i = 0; i < cant_children; i++)
			children[i] = index_children + i;

		TreeConstraint tc = null;
		switch (fmtype) {
		case AND:
			for (int i = 0; i < cant_children; i++) {
				if (RandomUtils.random() < 0.5) {
					fm.addTreeConstraint(new OptionalFeature(index_parent, children[i]));
				} else {
					fm.addTreeConstraint(new MandatoryFeature(index_parent, children[i]));
				}
			}
			break;
		case AND_OPT:
			for (int i = 0; i < cant_children; i++)
				fm.addTreeConstraint(new OptionalFeature(index_parent, children[i]));
			break;
		case AND_MAND:
			for (int i = 0; i < cant_children; i++)
				fm.addTreeConstraint(new MandatoryFeature(index_parent, children[i]));
			break;
		case OR:
			tc = new OrGroup(index_parent, children);
			fm.addTreeConstraint(tc);
			break;
		case XOR:
			tc = new AlternativeGroup(index_parent, children);
			fm.addTreeConstraint(tc);
			break;
		case CARDINALITY:
			int random1 = RandomUtils.randomRange(0, children.length);
			int random2 = RandomUtils.randomRange(1, children.length);

			int minCardinality = Math.min(random1, random2);
			int maxCardinality = Math.max(random1, random2);
			tc = new CardinalityGroup(minCardinality, maxCardinality, index_parent, children);
			fm.addTreeConstraint(tc);
			break;
		}

	}

	int aux = 0;

	public FeatureModel generateFeatureModel() {
		return FMGenerator.generateFeatureModel(numFeatures, fmtype, numTreeConstraints, ctctype,
				numCrossTreeConstraints);
	}

}
