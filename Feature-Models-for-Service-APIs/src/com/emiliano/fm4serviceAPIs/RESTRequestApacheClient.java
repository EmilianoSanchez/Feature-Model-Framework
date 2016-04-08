package com.emiliano.fm4serviceAPIs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import com.emiliano.fm4serviceAPIs.requestBuilder.RequestBuilder;

public class RESTRequestApacheClient extends RequestBuilder{
	
	private String buildURI(){
		String url = null;
		
		//En Android:
//		Uri.Builder builder = new Uri.Builder();
//		builder.scheme(schema)
//		    .authority(host);
//		for(String path:paths)
//			builder.appendPath(path);
//		for(Entry<String, String> queryParameter:queryParameters.entrySet())
//			builder.appendQueryParameter(queryParameter.getKey(),queryParameter.getValue());
//		builder.fragment(fragment);
//		url = builder.build().toString();
		
		//Con Apache HttpClient
		try {
			URIBuilder uriBuilder = new URIBuilder()
			.setScheme(this.uri.schema)
			.setHost(this.uri.host);
			String resourcePath="";
			for(String path:this.uri.paths)
				resourcePath+="/"+path;
			uriBuilder.setPath(resourcePath);
			for(Entry<String, String> queryParameter:this.uri.queryParameters.entrySet())
				uriBuilder.setParameter(queryParameter.getKey(),queryParameter.getValue());
			uriBuilder.setFragment(this.uri.fragment);
			java.net.URI uri = uriBuilder.build();
			url=uri.toString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	
	private HttpUriRequest buildApacheHttpRequest(){
		String uri=this.buildURI();
		HttpUriRequest request=null;
		switch(this.method){
		case GET:
			request=new HttpGet(uri);
			break;
		case POST:
			HttpEntityEnclosingRequest requestPost=new HttpPost(uri);
			try {
				requestPost.setEntity(new StringEntity(this.content));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		case PUT:
			HttpEntityEnclosingRequest requestPut=new HttpPut(uri);
			try {
				requestPut.setEntity(new StringEntity(this.content));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		case DELETE:
			request=new HttpDelete(uri);
			break;
		}
		
		for(Entry<String, String> header:this.headers.entrySet())
			request.addHeader(header.getKey(), header.getValue());

		return request;
	}

	@Override
	public RESTResponse execute() {
		HttpUriRequest httpRequest=this.buildApacheHttpRequest();
		HttpClient httpclient = HttpClients.createDefault();
		RESTResponse restResponse=new RESTResponse();
		try {
			HttpResponse httpResponse=httpclient.execute(httpRequest);
			
			restResponse.statusLine.httpVersion=httpResponse.getStatusLine().getProtocolVersion().getProtocol();
			restResponse.statusLine.statusCode=httpResponse.getStatusLine().getStatusCode();
			restResponse.statusLine.reasonPhrase=httpResponse.getStatusLine().getReasonPhrase();
			for(Header header:httpResponse.getAllHeaders()){
				restResponse.headers.put(header.getName(),header.getValue());
			}
			restResponse.content=httpResponse.getEntity().toString();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return restResponse;
	}
}
