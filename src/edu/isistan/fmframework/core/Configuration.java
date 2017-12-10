package edu.isistan.fmframework.core;

import java.util.Arrays;
import java.util.BitSet;

// TODO: Auto-generated Javadoc
/**
 * The class Configuration represents an assignment of features states in a
 * feature model. The feature state can be SELECTED, DESELECTED and UNSELECTED
 */
public class Configuration implements Cloneable {

	/** The feature model. */
	private FeatureModel model;

	/** The feature states. */
	private FeatureState[] states;

	/**
	 * Instantiates a new configuration.
	 *
	 * @param fm
	 *            the feature model from where the new configuration is
	 *            instantiated
	 */
	public Configuration(FeatureModel model) {
		this(model,FeatureState.UNSELECTED);
	}
	
	public Configuration(FeatureModel model,FeatureState state) {
		this.model = model;
		this.states = new FeatureState[model.getNumFeatures()];
		for (int i=0; i<states.length;i++) {
			this.states[i]= state;
		}
	}
	
	public Configuration(FeatureModel model,FeatureState[] states) {
		this.model = model;
		this.states = states;
	}

	/**
	 * Length.
	 *
	 * @return the configuration length
	 */
	public int length() {
		return states.length;
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
	public Feature getFeature(int featureId) {
		return this.model.getFeature(featureId);
	}

	/**
	 * Gets if the given feature is selected.
	 *
	 * @param featureName
	 *            the feature name
	 * @return the feature selected condition
	 */
	public FeatureState getFeatureState(int featureId) {
		return states[featureId];
	}

	public boolean isFeatureSelected(int featureId) {
		FeatureState state=this.getFeatureState(featureId);
		if(state!=null && state==FeatureState.SELECTED)
			return true;
		else
			return false;
	}
	
	public boolean isFeatureDeselected(int featureId) {
		FeatureState state=this.getFeatureState(featureId);
		if(state!=null && state==FeatureState.DESELECTED)
			return true;
		else
			return false;
	}
	
	public boolean isFeatureUnselected(int featureId) {
		FeatureState state=this.getFeatureState(featureId);
		if(state!=null && state==FeatureState.UNSELECTED)
			return true;
		else
			return false;
	}

	/**
	 * Gets the feature states.
	 *
	 * @return the entry set of feature states
	 */
	public FeatureState[] getFeatureStates() {
		return states;
	}

	/**
	 * Sets the feature state.
	 *
	 * @param featureName
	 *            the feature name
	 * @param featureState
	 *            the feature state
	 */
	public void setFeatureState(int featureId, FeatureState featureState) {
		states[featureId]=featureState;
	}
	
	/**
	 * Sets the feature state.
	 *
	 * @param featureName
	 *            the feature name
	 * @param featureState
	 *            the feature state as a boolean: true->SELECTED, false->DESELECTED
	 */
	public void setFeatureState(int featureId, boolean featureState) {
		if(featureState)
			states[featureId]=FeatureState.SELECTED;
		else
			states[featureId]=FeatureState.DESELECTED;
	}

	private Configuration() {
	}

	/**
	 * Copy.
	 *
	 * @param other
	 *            the configuration to copy
	 */
	public void copy(Configuration other) {
		this.model = other.model;
		this.states = other.states.clone();
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
		for (int i=0;i<this.states.length;i++) {
			aux.append(i + "=" + this.states[i].toString() + ";");
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
		for (int i=0;i<this.states.length;i++) {
			if (this.states[i] == FeatureState.UNSELECTED)
				return true;
		}
		return false;
	}

	public int getNumFeatures(){
		return this.states.length;
	}
	
	public BitSet getBitSet(){
		BitSet bitset=new BitSet();
		for(int i=0;i<this.states.length;i++)
			if(this.states[i]==FeatureState.SELECTED)
				bitset.set(i);
		return bitset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + Arrays.hashCode(states);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		if (model != other.model) {
				return false;
		} 
		if (!Arrays.equals(states, other.states))
			return false;
		return true;
	}
	
	
}
