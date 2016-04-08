package com.emiliano.fm4serviceAPIs.requestBuilder;

import com.emiliano.fm4serviceAPIs.requestBuilder.RequestBuilder.Method;

public class SetMethodAction extends RequestBuilderAction {

	private Method method;

	public SetMethodAction(Method method) {
		this.method = method;
	}

	@Override
	public void applyRequestBuilderAction(RequestBuilder requestBuilder) {
		requestBuilder.method = method;
	}

}