package com.xel.apigateway.gateway.core.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xel.apigateway.gateway.core.jpa.Controller;
import com.xel.apigateway.gateway.core.jpa.ModelService;
import com.xel.apigateway.gateway.util.MicroFinder;

/**
 * 
 * @author xeldawe
 *
 * @param <T>
 */
@Service
public class ModelController<T> implements Controller<T> {

	private com.xel.apigateway.gateway.core.jpa.Service<T> service;

	private Class<T> t;

	public Class getT() {
		return t;
	}

	public void setT(Class t) {
		this.t = t;
	}

	// GET All
	public List<T> get() {
		return get(null);
	}

	public List<T> get(Map<String, String> map) {
		try {
			if (map == null) {
				return service.findAll();
			} else {
				return service.findWithFilter( map);
			}
		} catch (Exception e) {
			return new LinkedList<T>();
		}
	}

	public com.xel.apigateway.gateway.core.jpa.Service<T> getService() {
		return service;
	}

	public T get(long id) {
		return get(null, id);
	}

	public T get(Map<String, String> map, long id) {
		if (map == null) {
			return service.find(id);
		} else {
			MicroFinder mf = new MicroFinder();
			map.put(mf.entintyId(getT()), id + "");
			return  (T) service.findWithFilter(map);
		}
	}

	// POST
	@Override
	public T post(T obj) {
		return  service.create(obj);
	}

	// PATCH
	@Override
	public T patch(long id, T obj) {
		return  service.update(id,obj);
	}

	// DELETE
	public void delete(long id) {
		service.delete(id);
	}

	@Override
	public void set(com.xel.apigateway.gateway.core.jpa.Service modelService) {
		this.service = (ModelService<T>) modelService;
		this.service.setTCls(getT());
	}

	@Override
	public int getTotalCount() {
		return service.getTotalCount();
	}


}

