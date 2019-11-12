package com.xel.apigateway.gateway.core;

import java.time.LocalDateTime;

import org.springframework.http.HttpMethod;

/**
 * 
 * @author xeldawe
 *
 */
public class Cache implements Cloneable{

	public Cache(HttpMethod hm, Object lastObject, LocalDateTime startTime) {
		super();
		this.hm = hm;
		this.lastObject = lastObject;
		this.startTime = startTime;
	}

	public Cache() {
		super();
		// TODO Auto-generated constructor stub
	}

	private HttpMethod hm;
	private Object lastObject;
	private LocalDateTime startTime;

	@Override
	public Object clone() {
	    try {
	        return (Cache) super.clone();
	    } catch (CloneNotSupportedException e) {
	        return new Cache(this.hm, this.lastObject, this.startTime);
	    }
	}
	
	public HttpMethod getHm() {
		return hm;
	}

	public void setHm(HttpMethod hm) {
		this.hm = hm;
	}

	public Object getLastObject() {
		return lastObject;
	}

	public void setLastObject(Object lastObject) {
		this.lastObject = lastObject;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

}
