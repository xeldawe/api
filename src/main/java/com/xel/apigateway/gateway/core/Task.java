package com.xel.apigateway.gateway.core;

import org.springframework.http.HttpMethod;

import com.xel.apigateway.gateway.core.jpa.Controller;

/**
 * 
 * @author xeldawe
 *
 * @param <T>
 */
public class Task<T> {

	public Task(String taskId, String serviceName, Controller controller, T model, HttpMethod httpMethod,
			boolean isPriority, int level, EndpointSelectorBean esb) {
		super();
		this.taskId = taskId;
		this.serviceName = serviceName;
		this.controller = controller;
		this.model = model;
		this.httpMethod = httpMethod;
		this.isPriority = isPriority;
		this.level = level;
		this.esb = esb;
	}

	private String taskId;
	private String serviceName;
	private T model;
	private Controller controller;
	private HttpMethod httpMethod;
	private boolean isPriority;
	private int level;
	private EndpointSelectorBean esb;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public boolean isPriority() {
		return isPriority;
	}

	public void setPriority(boolean isPriority) {
		this.isPriority = isPriority;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public EndpointSelectorBean getEsb() {
		return esb;
	}

	public void setEsb(EndpointSelectorBean esb) {
		this.esb = esb;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", model=" + model + ", controller=" + controller + ", httpMethod="
				+ httpMethod + ", isPriority=" + isPriority + ", level=" + level + ", esb=" + esb + "]";
	}

}
