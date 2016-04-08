package com.emiliano.fmframework.reasoning;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.Feature;
import com.emiliano.fmframework.core.FeatureModel;
import com.emiliano.fmframework.core.FeatureState;
import com.emiliano.fmframework.core.constraints.Clause;
import com.emiliano.fmframework.core.constraints.Constraint;
import com.emiliano.fmframework.core.constraints.IConstraint;
import com.emiliano.fmframework.reasoning.csa.ConfigurationSelectionAlgorithm;
import com.emiliano.fmframework.reasoning.objectiveFunctions.ObjectiveFunction;

public class ConfOperations {

	public static void applyConfiguration(Configuration conf, Object softElement) {
		applyConfiguration(conf.getModel().getRoot(), conf, softElement);
	}

	public static void applyConfiguration(Feature feature, Configuration conf, Object softElement) {
		Object childSoftElement;
		if (conf.getFeatureState(feature.getName()) == FeatureState.SELECTED) {
			childSoftElement = feature.applyAction(softElement);
		} else {
			childSoftElement = feature.disapplyAction(softElement);
		}
		for (Feature child : conf.getModel().getChildren(feature.getName()))
			applyConfiguration(child, conf, childSoftElement);
	}

	public static void applyConfiguration(Configuration newConf, Configuration previousConf, Object softElement) {
		applyConfiguration(newConf.getModel().getRoot(), newConf, previousConf, softElement);
	}

	public static void applyConfiguration(Feature feature, Configuration newConf, Configuration previousConf,
			Object softElement) {
		Object childSoftElement = null;
		if (newConf.getFeatureState(feature.getName()) != previousConf.getFeatureState(feature.getName())) {
			if (newConf.getFeatureState(feature.getName()) == FeatureState.SELECTED) {
				childSoftElement = feature.applyAction(softElement);
			} else {
				childSoftElement = feature.disapplyAction(softElement);
			}
		}
		for (Feature child : newConf.getModel().getChildren(feature.getName()))
			applyConfiguration(child, newConf, previousConf, childSoftElement);
	}

	public static Configuration getPartialConfiguration(FeatureModel fmodel) {
		Configuration conf = new Configuration(fmodel);

		for (Feature feature : fmodel.getFeatures()) {
			String featureName = feature.getName();
			for (Clause clause : fmodel.getClauses(featureName)) {
				if (clause.literals.size() == 1) {
					if (!ConfOperations.assignFeature(conf, featureName, clause.literals.get(featureName)))
						return null;
				}
			}
		}
		return conf;
	}

	public static boolean selectFeature(Configuration conf, String featureName) {
		return assignFeature(conf, featureName, FeatureState.SELECTED);
	}

	public static boolean deselectFeature(Configuration conf, String featureName) {
		return assignFeature(conf, featureName, FeatureState.DESELECTED);
	}

	public static boolean selectFeatureForced(Configuration conf, String featureName) {
		return assignFeatureForced(conf, featureName, FeatureState.SELECTED);
	}

	public static boolean deselectFeatureForced(Configuration conf, String featureName) {
		return assignFeatureForced(conf, featureName, FeatureState.DESELECTED);
	}

	public static Configuration selectConfiguration(Configuration partialConfiguration, ObjectiveFunction function,
			IConstraint constraint) {

		ConfigurationSelectionAlgorithm algorithm = new ConfigurationSelectionAlgorithm(function, constraint);
		Configuration fullConfiguration = new Configuration(partialConfiguration.getModel());
		algorithm.searchSolution(partialConfiguration, fullConfiguration);
		return fullConfiguration;
	}

	public static boolean assignFeature(Configuration conf, String featureName, boolean selected) {
		if (selected)
			return assignFeature(conf, featureName, FeatureState.SELECTED);
		else
			return assignFeature(conf, featureName, FeatureState.DESELECTED);
	}

	public static boolean assignFeatureForced(Configuration conf, String featureName, boolean selected) {
		if (selected)
			return assignFeatureForced(conf, featureName, FeatureState.SELECTED);
		else
			return assignFeatureForced(conf, featureName, FeatureState.DESELECTED);
	}

	public static boolean assignFeature(Configuration conf, String featureName, FeatureState state) {

		if (conf.getFeatureState(featureName) == FeatureState.UNSELECTED) {
			conf.setFeatureState(featureName, state);

			for (Clause clause : conf.getModel().getClauses(featureName)) {
				int numUnselected = 0;
				Map.Entry<String, Boolean> unselectedLiteral = null;
				boolean atLeastOneLiteralTrue = false;

				for (Map.Entry<String, Boolean> literal : clause.literals.entrySet()) {
					if (conf.getFeatureState(literal.getKey()) == FeatureState.UNSELECTED) {// isLiteralUndefinned
						numUnselected++;
						unselectedLiteral = literal;
					} else {
						if (conf.getFeatureState(literal.getKey()).booleanValue() == literal.getValue()) {// isLiteralTrue
							atLeastOneLiteralTrue = true;
							break;
						}
					}
				}

				if (atLeastOneLiteralTrue)
					continue;
				else {
					if (numUnselected == 0)
						return false;
					else
					// propagation
					if (numUnselected == 1 && !assignFeature(conf, unselectedLiteral.getKey(),
							FeatureState.featureStateValue(unselectedLiteral.getValue())))
						return false;
				}
			}
			return true;
		} else {
			if (conf.getFeatureState(featureName) == state)
				return true;
			else
				return false;
		}
	}

	public static boolean assignFeatureForced(Configuration conf, String featureName, FeatureState state) {
		return assignFeatureForced(conf, featureName, state, new HashSet<String>());
	}

	private static boolean assignFeatureForced(Configuration conf, String featureName, FeatureState state,
			Set<String> visited) {

		if (conf.getFeatureState(featureName) == FeatureState.UNSELECTED) {
			conf.setFeatureState(featureName, state);
			visited.add(featureName);

			for (Clause clause : conf.getModel().getClauses(featureName)) {
				int numUnselected = 0;
				Map.Entry<String, Boolean> unselectedLiteral = null;
				boolean atLeastOneLiteralTrue = false;

				for (Map.Entry<String, Boolean> literal : clause.literals.entrySet()) {
					if (conf.getFeatureState(literal.getKey()) == FeatureState.UNSELECTED) {// isLiteralUndefinned
						numUnselected++;
						unselectedLiteral = literal;
					} else {
						if (conf.getFeatureState(literal.getKey()).booleanValue() == literal.getValue()) {// isLiteralTrue
							atLeastOneLiteralTrue = true;
							break;
						}
					}
				}

				if (atLeastOneLiteralTrue)
					continue;
				else {
					if (numUnselected == 0)
						return false;
					else
					// propagation
					if (numUnselected == 1 && !assignFeatureForced(conf, unselectedLiteral.getKey(),
							FeatureState.featureStateValue(unselectedLiteral.getValue()), visited))
						return false;
				}
			}
			return true;
		} else {
			if (conf.getFeatureState(featureName) == state) {
				return true;
			} else {
				if (visited.contains(featureName))
					return false;
				else {
					conf.setFeatureState(featureName, FeatureState.UNSELECTED);
					return assignFeatureForced(conf, featureName, state, visited);
				}
			}
		}
	}

	public static Configuration selectConfiguration(Configuration partialConfiguration, ObjectiveFunction function) {
		return selectConfiguration(partialConfiguration, function, new IConstraint() {
			@Override
			public boolean isSatisfied(Configuration configuration) {
				return true;
			}
		});
	}

	public static Configuration selectConfiguration(Configuration partialConfiguration) {
		return selectConfiguration(partialConfiguration, new ObjectiveFunction() {
			@Override
			public double evaluate(Configuration configuration) {
				return 0;
			}
		}, new IConstraint() {
			@Override
			public boolean isSatisfied(Configuration configuration) {
				return true;
			}
		});
	}

	public static Configuration selectConfiguration(FeatureModel featureModel, ObjectiveFunction function,
			IConstraint constraint) {
		Configuration partialConfiguration = ConfOperations.getPartialConfiguration(featureModel);
		if (partialConfiguration != null)
			return ConfOperations.selectConfiguration(featureModel, function, constraint);
		else
			return null;
	}

	public static Configuration selectConfiguration(FeatureModel featureModel, ObjectiveFunction function) {
		return selectConfiguration(featureModel, function, new IConstraint() {
			@Override
			public boolean isSatisfied(Configuration configuration) {
				return true;
			}
		});
	}

	public static Configuration selectConfiguration(FeatureModel featureModel) {
		return selectConfiguration(featureModel, new ObjectiveFunction() {
			@Override
			public double evaluate(Configuration configuration) {
				return 0;
			}
		}, new IConstraint() {
			@Override
			public boolean isSatisfied(Configuration configuration) {
				return true;
			}
		});
	}

	public static boolean assignFeaturesForced(Configuration targetConfiguration, Configuration sourceConfiguration) {
		for (Entry<String, FeatureState> assignment : sourceConfiguration.getFeatureStates()) {
			if (assignment.getValue() == FeatureState.UNSELECTED)
				continue;
			if (!ConfOperations.assignFeatureForced(targetConfiguration, assignment.getKey(), assignment.getValue()))
				return false;
		}
		return true;
	}

	public static boolean assignFeatures(Configuration targetConfiguration, Configuration sourceConfiguration) {
		for (Entry<String, FeatureState> assignment : sourceConfiguration.getFeatureStates()) {
			if (assignment.getValue() == FeatureState.UNSELECTED)
				continue;
			if (!ConfOperations.assignFeature(targetConfiguration, assignment.getKey(), assignment.getValue()))
				return false;
		}
		return true;
	}
}
