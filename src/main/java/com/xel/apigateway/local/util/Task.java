package com.xel.apigateway.local.util;

import org.springframework.http.HttpMethod;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;

public class Task {

	public Task(Long id, String host, String endpoint, HttpMethod httpMethod, Object obj, Long pathId) {
		super();
		this.id = id;
		this.host = host;
		this.endpoint = endpoint;
		this.httpMethod = httpMethod;
		this.obj = obj;
		this.pathId = pathId;
	}

	private Long id;
	private String host;
	private String endpoint;
	private HttpMethod httpMethod;
	private Object obj;
	private Long pathId;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Long getPathId() {
		return pathId;
	}

	public void setPathId(Long pathId) {
		this.pathId = pathId;
	}

}
