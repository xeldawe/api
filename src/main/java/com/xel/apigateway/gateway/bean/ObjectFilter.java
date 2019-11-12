package com.xel.apigateway.gateway.bean;

/**
 * 
 * @author xeldawe
 *
 * @param <T>
 */
public class ObjectFilter<T> {

	private T Object;

	public T getT() {
		return Object;
	}

	public void setT(T t) {
		this.Object = t;
	}

}
