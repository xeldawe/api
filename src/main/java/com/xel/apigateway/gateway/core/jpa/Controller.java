package com.xel.apigateway.gateway.core.jpa;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author xeldawe
 *
 * @param <T>
 */
public interface Controller<T> {

	public List<T> get();
	
	public List<T> get(Map<String, String> map);
	
	public T get(long id);
	
	public T get(Map<String, String> map, long id);
	
	public T post(T obj);
	
	public T patch(long id, T obj);
	
	public void delete(long id);

	public void set(Service modelService);
	
	public Class getT();
	
	public void setT(Class t);
	
	public Service<T> getService();
	
	public int getTotalCount();
	
}

