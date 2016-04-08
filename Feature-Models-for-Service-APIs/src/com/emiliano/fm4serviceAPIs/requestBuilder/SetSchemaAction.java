package com.emiliano.fm4serviceAPIs.requestBuilder;

public class SetSchemaAction extends RequestBuilderAction {

	private String schema;

	public SetSchemaAction(String schema) {
		this.schema = schema;
	}

	@Override
	public void applyRequestBuilderAction(RequestBuilder requestBuilder) {
		requestBuilder.uri.schema = schema;
	}

}
