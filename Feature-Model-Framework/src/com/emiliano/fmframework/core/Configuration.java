package com.emiliano.fmframework.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The class Configuration represents an assignment of features states in a
 * feature model. The feature state can be SELECTED, DESELECTED and UNSELECTED
 */
public class Configuration implements Cloneable {

	/** The feature model. */
	private FeatureModel model;

	/** The feature states. */
	private Map<String, FeatureState> states;

	/**
	 * Instantiates a new configuration.
	 *
	 * @param fm
	 *            the feature model from where the new configuration is
	 *            instantiated
	 */
	public Configuration(FeatureModel model) {
		this.model = model;
		this.states = new HashMap<String, FeatureState>();
		for (Feature feature : model.getFeatures()) {
			this.states.put(feature.getName(), FeatureState.UNSELECTED);
		}
	}

	/**
	 * Length.
	 *
	 * @return the configuration length
	 */
	public int length() {
		return states.size();
	}

	/**
	 * Gets the feature model.
	 *
	 * @return the feature model
	 */
	public FeatureModel getModel() {
		return this.model;
	}

	/**
	 * Gets a feature given its name.
	 *
	 * @param featureName
	 *            the feature name
	 * @return the feature
	 */
	/**
	 * @param featureName
	 * @return
	 */
	public Feature getFeature(String featureName) {
		return this.model.getFeature(featureName);
	}

	/**
	 * Gets if the given feature is selected.
	 *
	 * @param featureName
	 *            the feature name
	 * @return the feature selected condition
	 */
	public FeatureState getFeatureState(String featureName) {
		return states.get(featureName);
	}
	
	public boolean isFeatureSelected(String featureName) {
		FeatureState state=this.getFeatureState(featureName);
		if(state!=null && state==FeatureState.SELECTED)
			return true;
		else
			return false;
	}

	/**
	 * Gets the feature states.
	 *
	 * @return the entry set of feature states
	 */
	public Set<Entry<String, FeatureState>> getFeatureStates() {
		return states.entrySet();
	}

	/**
	 * Sets the feature state.
	 *
	 * @param featureName
	 *            the feature name
	 * @param featureState
	 *            the feature state
	 */
	public void setFeatureState(String featureName, FeatureState featureState) {
		states.put(featureName, featureState);
	}
	
	/**
	 * Sets the feature state.
	 *
	 * @param featureName
	 *            the feature name
	 * @param featureState
	 *            the feature state as a boolean: true->SELECTED, false->DESELECTED
	 */
	public void setFeatureState(String featureName, boolean featureState) {
		if(featureState)
			states.put(featureName, FeatureState.SELECTED);
		else
			states.put(featureName, FeatureState.DESELECTED);
	}

	private Configuration() {
	}

	/**
	 * Copy.
	 *
	 * @param other
	 *            the configuration to copy
	 */
	@SuppressWarnings("unchecked")
	public void copy(Configuration other) {
		this.model = other.model;
		this.states = ((Map<String, FeatureState>) ((HashMap<String, FeatureState>) other.states).clone());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Configuration clone() {
		Configuration clone = new Configuration();
		clone.copy(this);
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder aux = new StringBuilder("[");
		for (Entry<String, FeatureState> entry : this.states.entrySet()) {
			aux.append(entry.getKey() + "=" + entry.getValue().toString() + ";");
		}
		return aux.append("]").toString();
	}

	/**
	 * Checks if it is a partial configuration, i.e. if it contains at least one
	 * unselected feature.
	 *
	 * @return true, if it is a partial configuration, or false, if it is a full
	 *         configuration
	 */
	public boolean isPartialConfiguration() {
		for (Entry<String, FeatureState> entry : this.states.entrySet()) {
			if (entry.getValue() == FeatureState.UNSELECTED)
				return true;
		}
		return false;
	}

}
