package com.emiliano.fm4serviceAPIs.requestBuilder;

public class AddPathAction extends RequestBuilderAction {

	private String path;

	public AddPathAction(String path) {
		this.path = path;
	}

	@Override
	public void applyRequestBuilderAction(RequestBuilder requestBuilder) {
		requestBuilder.uri.paths.add(path);
	}

}
