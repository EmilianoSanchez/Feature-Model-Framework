package edu.isistan.fmframework.optimization.optGAFES.ga;


import edu.isistan.fmframework.core.constraints.Clause;
import edu.isistan.fmframework.core.constraints.ClauseBasedConstraint;
import edu.isistan.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import edu.isistan.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.utils.RandomUtils;

public class GAFESfmTransform {

	public static BasicProblem instance;

	public static boolean satisfyResourceRestrictions(boolean[] bitset) {
		double r[] = new double[instance.globalConstraints.length];
		for (int rid = 0; rid < instance.globalConstraints.length; rid++) {
			r[rid] = 0.0;
			for (int i = 0; i<bitset.length; i++) {
				if(bitset[i]){
					r[rid] += instance.globalConstraints[rid].attributes[i];
				}
			}
			if(r[rid]>instance.globalConstraints[rid].restrictionLimit)
				return false;
		}
		return true;
	}
	
	// Since a feature model with cross-tree constraints can encode an arbitrary
	// satisfiability problem, it is not always possible to find a valid feature
	// selection.
	// The fmTransform algorithm uses a retry counter to limit the time spent
	// attempting to repair a feature selection.
	// In practice, we have found it rare for fmTransform to be unable to
	// generate a valid feature selection, but more research is needed to
	// identify feature model architectures for which it does not perform well.
	public static boolean[] fmTransform(boolean[] sr) {
//		return sr;
		
		boolean[] sv = new boolean[sr.length];
		boolean[] se = new boolean[sr.length];
		boolean[] sg = new boolean[sr.length];

		for (int f = 0; f < sr.length; f++) {
			if (sr[f] /* && !sv.get(f) && !se.get(f) */ ) {
				includeFeature(f, sv, se);
			}
		}

		for (int i = 0; i < sr.length; i++) {
			if (sv[i]) {
				if (instance.model.hasChildren(i)) {
					int[] children = instance.model.getChildren(i);
					boolean noChildrenInSv = true;
					for (int child : children) {
						if (sv[child]) {
							noChildrenInSv = false;
							break;
						}
					}
					if (noChildrenInSv)
						sg[i]=true;
				}
			}
		}
		// System.out.println(sg);

		for (int i = 0; i < sg.length; i++) {
			if (sg[i]) {
				int f = i;
				while (instance.model.hasChildren(f)) {
					f = randomChild(f);
					includeFeature(f, sv, se);
				}
			}
		}
		return sv;
	}

	private static boolean includeFeature(int f, boolean[] sv, boolean[] se) {
		if (se[f]) {
			return false;
		} else {
			if (sv[f]) {
				return true;
			} else {
				sv[f]=true;

				if (f != 0) {// No root
					if (!includeFeature(instance.model.getParent(f), sv, se))
						return false;
					TreeConstraint parentTree = instance.model.getParentTree(f);
					if (parentTree.getClass() == AlternativeGroup.class) {
						for (int brother : parentTree.getChildren()) {
							if (brother != f)
								excludeFeature(brother, sv, se);
						}
					}
				}

				for (TreeConstraint childTree : instance.model.getChildrenTrees(f)) {
					if (childTree.getClass() == MandatoryFeature.class)
						if (!includeFeature(childTree.getChildren()[0], sv, se))
							return false;
				}

				// System.out.println("CTC de
				// "+f+":"+instance.model.getCrossTreeConstraints(f).size());
				for (ClauseBasedConstraint constraint : instance.model.getCrossTreeConstraints(f)) {
					if (constraint.getInvolvedFeatures().size() == 2) {
						// System.out.println("binary constraint: "+constraint);

						for (Clause clause : constraint.getClauses()) {

							boolean fvalue = clause.literalValues[0];
							boolean ffvalue = clause.literalValues[1];
							int ffid = clause.literalIds[1];
							if (clause.literalIds[0] != f) {
								fvalue = clause.literalValues[1];
								ffvalue = clause.literalValues[0];
								ffid = clause.literalIds[0];
							}

							if (!fvalue) {
								if (ffvalue) {
									if (!includeFeature(ffid, sv, se))
										return false;
								} else {
									if (!excludeFeature(ffid, sv, se))
										return false;
								}
								break;
							}
						}
					}
				}

				return true;
			}
		}
	}

	private static boolean excludeFeature(int f, boolean[] sv, boolean[] se) {
		if (sv[f]) {
			return false;
		} else {
			if (se[f]) {
				return true;
			} else {
				se[f]=true;

				for (TreeConstraint childTree : instance.model.getChildrenTrees(f)) {
					for (int child : childTree.getChildren())
						excludeFeature(child, sv, se);
				}

				TreeConstraint parentTree = instance.model.getParentTree(f);
				if (parentTree.getClass() == MandatoryFeature.class) {
					excludeFeature(parentTree.getParent(), sv, se);
				}

				// System.out.println("CTC de
				// "+f+":"+instance.model.getCrossTreeConstraints(f).size());
				for (ClauseBasedConstraint constraint : instance.model.getCrossTreeConstraints(f)) {
					if (constraint.getInvolvedFeatures().size() == 2) {
						// System.out.println("binary constraint: "+constraint);

						for (Clause clause : constraint.getClauses()) {

							boolean fvalue = clause.literalValues[0];
							boolean ffvalue = clause.literalValues[1];
							int ffid = clause.literalIds[1];
							if (clause.literalIds[0] != f) {
								fvalue = clause.literalValues[1];
								ffvalue = clause.literalValues[0];
								ffid = clause.literalIds[0];
							}

							if (fvalue) {
								if (ffvalue)
									includeFeature(ffid, sv, se);
								else
									excludeFeature(ffid, sv, se);
								break;
							}
						}
					}
				}
				return true;
			}
		}
	}

	private static int randomChild(int f) {
		int[] children = instance.model.getChildren(f);
		int randomNum = RandomUtils.randomRange(0, children.length);
		return children[randomNum];
	}
}
