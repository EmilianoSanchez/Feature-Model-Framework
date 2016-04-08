package com.emiliano.fmframework.core;
//
//Feature instances should redefine "applyFeature" and
//* "disapplyFeature" to implement specific modifications on the provided software
public interface FeatureAction {

	/**
	 * Apply action.
	 *
	 * @param softElement
	 *            the soft element
	 * @return the soft element
	 */
	public Object applyAction(Object softElement);
//	default public SoftElement applyAction(SoftElement softElement){
//		return softElement;
//	}

	/**
	 * Disapply action.
	 *
	 * @param softElement
	 *            the soft element
	 * @return the soft element
	 */
	public Object disapplyAction(Object softElement);
//	default public SoftElement disapplyAction(SoftElement softElement){
//		return softElement;
//	}
}
