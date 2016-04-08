package com.emiliano.fm4serviceAPIs.requestBuilder;

public class AddHeaderAction extends RequestBuilderAction {

	private String name, value;

	public AddHeaderAction(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void applyRequestBuilderAction(RequestBuilder requestBuilder) {
		requestBuilder.headers.put(name, value);
	}

}
