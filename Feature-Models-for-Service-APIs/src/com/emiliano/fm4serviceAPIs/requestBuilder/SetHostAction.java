package com.emiliano.fm4serviceAPIs.requestBuilder;

public class SetHostAction extends RequestBuilderAction {

	private String host;

	public SetHostAction(String host) {
		this.host = host;
	}

	@Override
	public void applyRequestBuilderAction(RequestBuilder requestBuilder) {
		requestBuilder.uri.host = host;
	}

}