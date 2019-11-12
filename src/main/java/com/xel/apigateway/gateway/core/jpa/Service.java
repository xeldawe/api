package com.xel.apigateway.gateway.core.jpa;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author xeldawe
 *
 * @param <T>
 */
public interface Service<T> {
	public List<T> findAll();
	public T find(long id);
	public T create(T obj);
	public T merge(T obj);
	public void delete(long id);
	public T update(long id, T obj);
	public List<T> findWithFilter(Map<String, String> map);
	public List<T> findWithFilter(T obj, Map<String, String> map);
	public void setTCls(Class t);
	public int getTotalCount();
}

