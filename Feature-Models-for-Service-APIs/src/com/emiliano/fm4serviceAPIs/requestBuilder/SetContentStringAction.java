package com.emiliano.fm4serviceAPIs.requestBuilder;

public class SetContentStringAction extends RequestBuilderAction {

	private String content;

	public SetContentStringAction(String content) {
		this.content = content;
	}

	@Override
	public void applyRequestBuilderAction(RequestBuilder requestBuilder) {
		requestBuilder.content = content;
	}

}