package com.emiliano.fm4serviceAPIs.requestBuilder;

import com.emiliano.fmframework.core.FeatureAction;

public abstract class RequestBuilderAction implements FeatureAction {

	@Override
	public Object applyAction(Object softElement) {
		this.applyRequestBuilderAction((RequestBuilder) softElement);
		return softElement;
	}

	@Override
	public Object disapplyAction(Object softElement) {
		return softElement;
	}

	public abstract void applyRequestBuilderAction(RequestBuilder requestBuilder);

}
