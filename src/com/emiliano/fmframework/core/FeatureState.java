package com.emiliano.fmframework.core;

// TODO: Auto-generated Javadoc
/**
 * The Enum FeatureState that represents the state of a feature in a configuration.
 */
public enum FeatureState {

	/** The deselected. */
	DESELECTED,
	/** The selected. */
	SELECTED,
	/** The unselected. */
	UNSELECTED;

	/**
	 * Boolean value.
	 *
	 * @return true, if successful
	 */
	public boolean booleanValue() {
		if (this == SELECTED) {
			return true;
		}
		return false;
	}

	/**
	 * Feature state value.
	 *
	 * @param value
	 *            the value
	 * @return the feature state
	 */
	public static FeatureState featureStateValue(boolean value) {
		if (value)
			return SELECTED;
		else
			return DESELECTED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		switch (this) {
		case SELECTED:
			return "S";
		case DESELECTED:
			return "D";
		default:// UNSELECTED
			return "U";
		}
	}
}