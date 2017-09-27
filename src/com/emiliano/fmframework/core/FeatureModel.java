package com.emiliano.fmframework.core;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.emiliano.fmframework.core.constraints.Clause;
import com.emiliano.fmframework.core.constraints.Constraint;
import com.emiliano.fmframework.core.constraints.treeConstraints.TreeConstraint;

// TODO: Auto-generated Javadoc
/**
 * The Interface FeatureModel defines the basic operations for building a
 * feature model and accessing its elements (features, tree and cross-tree
 * constraints).
 * 
 * @param <SoftElement>
 *            the generic type
 */
public interface FeatureModel {

	/**
	 * Gets the root feature of the model.
	 *
	 * @return the root feature
	 */
	public Feature getRoot();

	/**
	 * Gets the feature given its name.
	 *
	 * @param name
	 *            of the feature
	 * @return the feature or null if the feature with the given name was not
	 *         previously added
	 */
	public Feature getFeature(String name);

	/**
	 * Gets if a feature has children or is a leaf feature
	 * 
	 * @param name
	 *            of the feature
	 * @return true if the feature has children, or false if it is a leaf
	 *         feature
	 */
	public boolean hasChildren(String name);

	/**
	 * Gets the set of children trees.
	 *
	 * @param name
	 *            of the parent feature
	 * @return the set of children trees or null if the feature name do not
	 *         exist
	 */
	public Vector<TreeConstraint> getChildrenTrees(String name);

	/**
	 * Gets the children of a feature.
	 *
	 * @param name
	 *            of the parent feature
	 * @return the set of children or null if the feature name do not exist
	 */
	public Vector<Feature> getChildren(String name);

	/**
	 * Gets the parent tree of a feature.
	 *
	 * @param name
	 *            of the child feature
	 * @return the parent tree or null if the feature name do not exist
	 */
	public TreeConstraint getParentTree(String name);

	/**
	 * Gets the parent.
	 *
	 * @param name
	 *            of the child feature
	 * @return the parent feature or null if the feature name do not exist
	 */
	public Feature getParent(String name);

	/**
	 * Gets the number of features.
	 *
	 * @return the number of features
	 */
	public int getNumFeatures();

	/**
	 * Adds the given feature to the model.
	 *
	 * @param feature
	 *            to add
	 * @return the previous feature associated with the name, or null if there
	 *         was no a feature with that name
	 */
	public Feature addFeature(Feature feature);

	/**
	 * Gets the set of features.
	 *
	 * @return the set of features
	 */
	public Vector<Feature> getFeatures();

	/**
	 * Adds the tree constraint.
	 *
	 * @param tree
	 *            constraint to add
	 * @return false, if any of the children features already belongs to a tree
	 *         constraint
	 */
	public boolean addTreeConstraint(TreeConstraint constraint);

	/**
	 * Gets the set of tree constraints.
	 *
	 * @return the set of tree constraints
	 */
	public Vector<TreeConstraint> getTreeConstraints();

	/**
	 * Adds the cross-tree constraint.
	 *
	 * @param cross
	 *            -tree constraint to add
	 * @return false, if the constraint contains feature names that are not in
	 *         the model
	 */
	public boolean addCrossTreeConstraint(Constraint constraint);

	/**
	 * Gets the set of cross tree constraints.
	 *
	 * @return the set of cross tree constraints
	 */
	public Vector<Constraint> getCrossTreeConstraints();

	public Set<Clause> getClauses(String featureName);
}
