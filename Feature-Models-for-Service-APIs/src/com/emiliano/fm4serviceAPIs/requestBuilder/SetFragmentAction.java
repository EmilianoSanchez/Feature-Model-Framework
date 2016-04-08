package com.emiliano.fm4serviceAPIs.requestBuilder;

public class SetFragmentAction extends RequestBuilderAction {

	private String fragment;

	public SetFragmentAction(String fragment) {
		this.fragment = fragment;
	}

	@Override
	public void applyRequestBuilderAction(RequestBuilder requestBuilder) {
		requestBuilder.uri.fragment = fragment;
	}

}
