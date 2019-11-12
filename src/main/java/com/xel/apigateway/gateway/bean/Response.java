package com.xel.apigateway.gateway.bean;

import java.sql.Timestamp;

import org.springframework.http.HttpHeaders;

/**
 * 
 * @author xeldawe
 *
 * @param <T>
 */
public class Response<T> {

	public Response() {
		super();

	}

	private T message;
	private HttpHeaders headers;
	private Timestamp requestTime;
	private Timestamp responseTime;
	private String endpoint;
	private Filter filters;
	private Auth auth;
	private String serverProcessTime;
	private String queueWaitingTime;
	private boolean priorityQueue;
	private com.xel.apigateway.gateway.core.Error error;
	private int TotalCount;

	public int getTotalCount() {
		return TotalCount;
	}

	public void setTotalCount(int totalCount) {
		TotalCount = totalCount;
	}

	public com.xel.apigateway.gateway.core.Error getError() {
		return error;
	}

	public void setError(com.xel.apigateway.gateway.core.Error error) {
		this.error = error;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T body) {
		this.message = body;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	public Timestamp getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	public Timestamp getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Timestamp responseTime) {
		this.responseTime = responseTime;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Filter getFilters() {
		return filters;
	}

	public void setFilters(Filter filters) {
		this.filters = filters;
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public String getServerProcessTime() {
		return serverProcessTime;
	}

	public void setServerProcessTime(String serverProcessTime) {
		this.serverProcessTime = serverProcessTime;
	}

	public String getQueueWaitingTime() {
		return queueWaitingTime;
	}

	public void setQueueWaitingTime(String queueWaitingTime) {
		this.queueWaitingTime = queueWaitingTime;
	}

	public boolean isPriorityQueue() {
		return priorityQueue;
	}

	public void setPriorityQueue(boolean priorityQueue) {
		this.priorityQueue = priorityQueue;
	}

	@Override
	public String toString() {
		return "Response [body=" + message + ", headers=" + headers + ", requestTime=" + requestTime + ", responseTime="
				+ responseTime + ", endpoint=" + endpoint + ", filters=" + filters + ", auth=" + auth
				+ ", serverProcessTime=" + serverProcessTime + ", queueWaitingTime=" + queueWaitingTime
				+ ", priotiryQueue=" + priorityQueue + "]";
	}

}

