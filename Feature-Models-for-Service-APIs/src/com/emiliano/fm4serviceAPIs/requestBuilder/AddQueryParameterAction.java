package com.emiliano.fm4serviceAPIs.requestBuilder;

public class AddQueryParameterAction extends RequestBuilderAction {

	private String param, value;

	public AddQueryParameterAction(String param, String value) {
		this.param = param;
		this.value = value;
	}

	@Override
	public void applyRequestBuilderAction(RequestBuilder requestBuilder) {
		requestBuilder.uri.queryParameters.put(param, value);
	}

}