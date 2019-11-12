package com.xel.apigateway.gateway.core.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author xeldawe
 *
 * @param <T>
 */
@Service
public class ModelService<T> implements com.xel.apigateway.gateway.core.jpa.Service<T> {

	@Autowired
	private ModelRepositoryImpl<T> modelRepository;
	
	private SessionFactory hibernateFactory;
	
	private Class TCls;

	@Autowired
	public void factory(EntityManagerFactory factory) {
		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}
		this.hibernateFactory = factory.unwrap(SessionFactory.class);
	}
	
	public List<T> findAll() {
		return (List<T>) modelRepository.findAll();
	}

	public SessionFactory getHibernateFactory() {
		return hibernateFactory;
	}

	public void setHibernateFactory(SessionFactory hibernateFactory) {
		this.hibernateFactory = hibernateFactory;
	}

	@Override
	public List<T> findWithFilter(Map<String, String> map) {
		return (List<T>) modelRepository.findWithFilter(map);
	}

	@Override
	public List<T> findWithFilter(T obj, Map<String, String> map) {
		return (List<T>) modelRepository.findWithFilter(obj, map);
	}

	@Override
	public T find(long id) {
		return modelRepository.find(id);
	}

	@Override
	public T create(T obj) {
		return modelRepository.create(obj);
	}
	
	@Override
	public T merge(T obj) {
		return modelRepository.merge(obj);
	}

	@Override
	public void delete(long id) {
		modelRepository.delete(id);
	}

	@Override
	public T update(long id, T obj) {
		return modelRepository.update(id, obj);
	}

	public Class getTCls() {
		return TCls;
	}

	public void setTCls(Class cls) {
		TCls = cls;
		modelRepository.setModelCls(cls);
	}

	@Override
	public int getTotalCount() {
		return modelRepository.getTotalCount();
	}

}
