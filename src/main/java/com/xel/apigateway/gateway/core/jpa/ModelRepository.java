package com.xel.apigateway.gateway.core.jpa;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 
 * @author xeldawe
 *
 * @param <T>
 */
@Repository
public interface ModelRepository<T>{
	public List<T> findAll();
	public T find(long id);
	public T create(T obj);
	public void delete(long id);
	public T update(long id,T obj);
	public List<T> findWithFilter(Map<String, String> map);
	public List<T> findWithFilter(T obj, Map<String, String> map);
	public T merge(T obj);
	public int getTotalCount();
}

