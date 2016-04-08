package com.emiliano.fm4serviceAPIs.requestBuilder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.emiliano.fm4serviceAPIs.RESTResponse;

public abstract class RequestBuilder {

	public Method method;
	public URI uri;
	public Map<String, String> headers;
	public String content;

	public RequestBuilder() {
		this.uri = new URI();
		this.headers = new HashMap<String, String>();
	}

	public enum Method {
		GET, POST, PUT, DELETE
	}

	public String toString() {
		return method.name() + " " + uri.toString() + " " + headers.toString() + " " + content;
	}

	public class URI {
		public URI() {
			this.paths = new LinkedList<String>();
			this.queryParameters = new HashMap<String, String>();
		}

		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(schema + "://" + host);
			for (String path : paths)
				stringBuilder.append("/" + path);
			if (queryParameters.size() > 0) {
				stringBuilder.append("?");
				for (Entry<String, String> queryParam : this.queryParameters.entrySet()) {
					stringBuilder.append("&" + queryParam.getKey() + "=" + queryParam.getValue());
				}
			}
			if (fragment != null)
				stringBuilder.append("#" + fragment);
			return stringBuilder.toString();
		}

		public String schema;
		public String host;
		public List<String> paths;
		public Map<String, String> queryParameters;
		public String fragment;

	}

	public abstract RESTResponse execute();
}
