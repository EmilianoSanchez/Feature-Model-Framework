package com.emiliano.fm4serviceAPIs;

import java.util.HashMap;
import java.util.Map;

public class RESTResponse {
	
	public RESTResponse(){
		this.statusLine=new StatusLine();
		this.headers=new HashMap<String, String>();
	}
	
	public class StatusLine{
		public String httpVersion;
		public int statusCode;
		public String reasonPhrase;
	}
	
	public StatusLine statusLine;
	public Map<String,String> headers;
	public String content;
}
