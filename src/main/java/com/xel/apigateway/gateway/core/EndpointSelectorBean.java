package com.xel.apigateway.gateway.core;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;

/**
 * 
 * @author xeldawe
 *
 */
public class EndpointSelectorBean {

	public EndpointSelectorBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EndpointSelectorBean(HttpMethod h, String proxy, HttpServletRequest request, HttpServletResponse response) {
		super();
		this.h = h;
		this.proxy = proxy;
		this.request = request;
		this.response = response;
	}

	private HttpMethod h;
	private String proxy;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, com.xel.apigateway.gateway.core.jpa.Service> service;
	private com.xel.apigateway.gateway.bean.Response resp;

	public com.xel.apigateway.gateway.bean.Response getResp() {
		return resp;
	}

	public void setResp(com.xel.apigateway.gateway.bean.Response resp) {
		this.resp = resp;
	}

	public Map<String, com.xel.apigateway.gateway.core.jpa.Service> getService() {
		return service;
	}

	public void setService(Map<String, com.xel.apigateway.gateway.core.jpa.Service> service) {
		this.service = service;
	}

	public HttpMethod getH() {
		return h;
	}

	public void setH(HttpMethod h) {
		this.h = h;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
