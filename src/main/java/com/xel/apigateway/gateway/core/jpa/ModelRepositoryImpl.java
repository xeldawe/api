package com.xel.apigateway.gateway.core.jpa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.internal.path.RootImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xel.apigateway.gateway.core.StaticBean;
import com.xel.apigateway.gateway.util.MicroFinder;
import com.xel.apigateway.local.datasource.CoreDataSource;
import com.xel.apigateway.local.datasource.CoreEntityManager;

/**
 * 
 * @author xeldawe
 *
 * @param <T>
 */
@Service
@Repository
@EnableTransactionManagement
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
public class ModelRepositoryImpl<T> implements ModelRepository<T> {

	private EntityManager entityManager;

	private Class modelCls;

	private SessionFactory hibernateFactory;

	private Map<String, Object> filters;

	private static List<Class[]> core = null;

	private int skip = 0;
	private int limit = 100;

	private int totalCount;

	@Autowired
	public void factory(EntityManagerFactory factory) {
		if (factory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("factory is not a hibernate factory");
		}
		this.hibernateFactory = factory.unwrap(SessionFactory.class);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, propagation = Propagation.REQUIRED)
	public List<T> findAll() {
		findEntityManager();
		skip = 0;
		limit = 100;
		filters = new LinkedHashMap<>();
		filters.put("limit", limit);
		filters.put("skip", skip);
		getCount(modelCls, filters);
		return (List<T>) localCriteriaExecutorList(modelCls, filters);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, propagation = Propagation.REQUIRED)
	@Override
	public T find(long id) {
		findEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(modelCls);
		Root<T> root = cq.from(modelCls);
		MicroFinder mf = new MicroFinder();
		cq.where(cb.equal(root.get(mf.entintyId(modelCls)), id));
		TypedQuery<T> q = entityManager.createQuery(cq);
		return q.getSingleResult();
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = false, propagation = Propagation.REQUIRED)
	public T create(T obj) {
		findEntityManager();
		entityManager.persist(obj);
		return obj;
	}

	@Override
	public T merge(T obj) {
		findEntityManager();
		entityManager.merge(obj);
		return obj;
	}

	@Override
	public void delete(long id) {
		findEntityManager();
		entityManager.remove(entityManager.find(modelCls, id));

	}

	@Override
	public T update(long id, T obj) {
		findEntityManager();
		T objTmp = (T) entityManager.find(modelCls, id);
		obj = objectUpdater(obj, objTmp);
		entityManager.persist(obj);
		return obj;
	}

	@Override

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, propagation = Propagation.REQUIRED)
	public List<T> findWithFilter(Map<String, String> map) {
		findEntityManager();
		// TODO Auto-generated method stub
		return findWithFilter(null, map);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, propagation = Propagation.REQUIRED)
	public List<T> findWithFilter(T obj, Map<String, String> map) {
		findEntityManager();
		if (obj != null) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(modelCls);
			Root<T> root = cq.from(modelCls);
			List<Predicate> predicates = new ArrayList<Predicate>();

			Method[] m = null;
			m = obj.getClass().getDeclaredMethods();
			for (Method item : m) {
				String mName = item.getName();
				if (mName.contains("get")) {
					Object o = null;
					try {
						o = item.invoke(obj);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}
					if (o != null) {
						mName = mName.substring(3);
						mName = mName.substring(0, 1).toUpperCase() + mName.substring(1);
						try {
							predicates.add(cb.equal(root.get(mName), item.invoke(obj)));
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}
					}
				}
			}
			cq.select(root).where(predicates.toArray(new Predicate[] {}));
			getCount(modelCls, null);
			if(limit > 100) {
				limit = 100;
			}
			List<T> res = entityManager.createQuery(cq).setFirstResult(skip).setMaxResults(limit).getResultList();
			return res;
		} else {
			filters = new LinkedHashMap<String, Object>();

			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = entry.getKey();
				if (key.contains("<like>")) {
					key = key.substring(6);
					filters.put(key, "%" + entry.getValue() + "%");
					// predicates.add(cb.like(root.get(key), "%" + entry.getValue() + "%"));
				} else {
					// predicates.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
					filters.put(key, entry.getValue());
				}
			}

			return (List<T>) localCriteriaExecutorList(modelCls, filters);
		}
	}

	public Class getModelCls() {
		return modelCls;
	}

	public void setModelCls(Class modelCls) {
		this.modelCls = modelCls;
	}

	public T objectUpdater(T newObject, T dbObject) {
		Class cls = newObject.getClass();
		Method[] methods = cls.getDeclaredMethods();

		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			String setterName = null;
			if (methodName.startsWith("get")) {// if not boolean
				setterName = methods[i].getName().replace("get", "set");
			} else if (methodName.startsWith("is")) { // if boolean
				setterName = methods[i].getName().replace("is", "set");
			}
			if (setterName != null) {
				try {
					Class returnType = methods[i].getReturnType();
					Object resValue = methods[i].invoke(newObject);
					if (resValue != null) {
						Method m = cls.getDeclaredMethod(setterName, returnType);
						m.invoke(dbObject, resValue);
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		}

		return dbObject;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, propagation = Propagation.REQUIRED)
	public void getCount(Class cls, Map<String, Object> filters) {
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
//		Root<T> root = cq.from(modelCls);
//		List<Predicate> predicates = new LinkedList<>();
//		Path<Object> currentRoot = null;
//		if (filters != null && !filters.isEmpty()) {
//			for (Map.Entry<String, Object> currentFilter : filters.entrySet()) {
//				String key = currentFilter.getKey();
//				if (key.equals("limit")) {
//					limit = Integer.valueOf((String) (currentFilter.getValue() + ""));
//				} else if (key.equals("skip")) {
//					skip = Integer.valueOf((String) (currentFilter.getValue() + ""));
//				} else if (key.contains(".")) {
//					String[] a = key.split("\\.");
//					Predicate[] pArrayCore = new Predicate[1];
//					Predicate jsonQ = null;
//					if(a.length < 3) {
//					for (int i = 0; i < a.length; i++) {
//						Class c = findClassEntity(a[i]);
//						if(currentRoot == null) {
//							currentRoot = root.get("site");
//							}else {
//								currentRoot = currentRoot.get(a[i]);
//							}
//						if (instanceCheck(c)) {
//							String curr = currentFilter.getValue().toString();
//							if ((curr.contains("[") && curr.contains("]"))
//									|| (curr.contains("{") && curr.contains("}"))) {
//								curr = curr.substring(curr.indexOf("[") + 1, curr.lastIndexOf("]"));
//								String[] parts = curr.replace("\"", "").split(",");
//								Predicate[] pArray = new Predicate[parts.length];
//								for (int ii = 0; ii < parts.length; ii++) {
//									if (parts[ii].startsWith("%")) {
//										pArray[ii] = cb.like(currentRoot.get(key), parts[ii]);
//									} else {
//										pArray[ii] = cb.equal(currentRoot.get(key), parts[ii]);
//									}
//								}
//								predicates.add(cb.or(pArray));
//							} else {
//								if (curr.startsWith("%")) {
//									predicates.add(cb.equal(root.get(a[i]).get(a[i + 1]), curr));
//								} else {
//									predicates.add(cb.equal(root.get(a[i]).get(a[i + 1]), curr));
//								}
//								break;
//							}
//						}
//					}
//					} else if (a.length > 2) {
//						for (int i = 0; i < a.length; i++) {
//							Class c = findClassEntity(a[i]);
//							if(currentRoot == null) {
//							currentRoot = root.get(a[i]);
//							}else {
//								currentRoot = currentRoot.get(a[i]);
//							}
//							if (i == (a.length - 2)) {
//								if (instanceCheck(c)) {
//									String curr = currentFilter.getValue().toString();
//									if (curr.contains("[") && curr.contains("]")) {
//										curr = curr.substring(curr.indexOf("[") + 1, curr.lastIndexOf("]"));
//										String[] parts = curr.replace("\"", "").split(",");
//										Predicate[] pArray = new Predicate[parts.length];
//										for (int ii = 0; ii < parts.length; ii++) {
//											if (parts[i].startsWith("%")) {
//												pArray[ii] = cb.like(root.get(key), parts[ii]);
//											} else {
//												pArray[ii] = cb.equal(root.get(key), parts[ii]);
//											}
//										}
//										predicates.add(cb.or(pArray));
//									} else {							
//										predicates.add(cb.equal(currentRoot.get(a[i+1]), curr));
//									}
//								}
//								
//							}
//						}
//					}
//				} else { 
//					String curr = currentFilter.getValue().toString();
//					if ((curr.contains("[") && curr.contains("]")) || (curr.contains("{") && curr.contains("}"))) {
//						curr = curr.substring(curr.indexOf("[") + 1, curr.lastIndexOf("]"));
//						String[] parts = curr.replace("\"", "").split(",");
//						Predicate[] pArray = new Predicate[parts.length];
//						for (int i = 0; i < parts.length; i++) {
//
//							if (parts[i].startsWith("%")) {
//								pArray[i] = cb.like(root.get(key), parts[i]);
//							} else {
//								pArray[i] = cb.equal(root.get(key), parts[i]);
//							}
//						}
//						predicates.add(cb.or(pArray));
//					} else {
//						if (curr.startsWith("%")) {
//							predicates.add(cb.like(root.get(key), curr));
//						} else {
//							predicates.add(cb.equal(root.get(key), curr));
//						}
//					}
//				}
//			}
//		}
//		if (!predicates.isEmpty()) {
//			Predicate[] tmp = new Predicate[predicates.size()];
//			int counter = 0;
//			for (Predicate item : predicates) {
//				tmp[counter] = item;
//				counter++;
//			}
//			cq.where(tmp);
//		}
//		cq.select(cb.countDistinct(root));
//		TypedQuery<Long> queryCount = entityManager.createQuery(cq);
//		Long count = queryCount.getSingleResult();
//		totalCount = count.intValue();
		// System.out.println(count); //TODO NEED TO FIX
		totalCount = -1;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object localCriteriaExecutorList(@SuppressWarnings("rawtypes") Class cls, Map<String, Object> filters) {
		try {
			skip = 0;
			limit = 100;
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery cq = cb.createQuery(cls);
			Root<T> root = cq.from(cls);
			CriteriaQuery<Long> counterCq = cb.createQuery(Long.class);
			List<Predicate> predicates = new LinkedList<>();
			Path<Object> currentRoot = null;
			for (Map.Entry<String, Object> currentFilter : filters.entrySet()) {
				String key = currentFilter.getKey();
				if (key.equals("limit")) {
					limit = Integer.valueOf((String) (currentFilter.getValue() + ""));
				} else if (key.equals("skip")) {
					skip = Integer.valueOf((String) (currentFilter.getValue() + ""));
				} else if (key.contains(".")) {
					// System.out.println("key: " + key);
					String[] a = key.split("\\.");
					Predicate jsonQ = null;
					if (a.length < 3) {
						
						for (int i = 0; i < a.length; i++) {
							Class c = findClassEntity(a[i]);
							if(currentRoot == null) {
								currentRoot = root.get(a[i]);
							
							}else {
								currentRoot = currentRoot.get(a[i]);
							}
							if (instanceCheck(c)) {
								String curr = currentFilter.getValue().toString();
								
								if (curr.contains("[") && curr.contains("]")) {
									if(i == a.length-1) {
									curr = curr.substring(curr.indexOf("[") + 1, curr.lastIndexOf("]"));
									String[] parts = curr.replace("\"", "").split(",");
									Predicate[] pArray = new Predicate[parts.length];
									if(key.contains(".")) {
										key = key.substring(key.lastIndexOf(".")+1);
									}
									for (int ii = 0; ii < parts.length; ii++) {
										if (parts[i].startsWith("%")) {
											//pArray[ii] = cb.like(currentRoot, parts[ii]);
										} else {
											pArray[ii] = cb.equal(currentRoot, parts[ii]);
										}
									}
									predicates.add(cb.or(pArray));
									}
								} else {
									if (curr.startsWith("%")) {
										predicates.add(cb.like(currentRoot.get(a[i]).get(a[i + 1]), curr));
									} else {
										predicates.add(cb.equal(currentRoot.get(a[i]).get(a[i + 1]), curr));
									}
									break;
								}
							}
						}
					} else if (a.length > 2) {
					
						for (int i = 0; i < a.length; i++) {
							Class c = findClassEntity(a[i]);
							if(currentRoot == null) {
								currentRoot = root.get(a[i]);
							
							}else {
								currentRoot = currentRoot.get(a[i]);
							}
							if (i == (a.length - 2)) {
								if (instanceCheck(c)) {
									String curr = currentFilter.getValue().toString();
									if (curr.contains("[") && curr.contains("]")) {
										curr = curr.substring(curr.indexOf("[") + 1, curr.lastIndexOf("]"));
										String[] parts = curr.replace("\"", "").split(",");
										Predicate[] pArray = new Predicate[parts.length];
										if(key.contains(".")) {
											key = key.substring(key.lastIndexOf(".")+1);
										}
										for (int ii = 0; ii < parts.length; ii++) {
											if (parts[i].startsWith("%")) {
												pArray[ii] = cb.like(currentRoot.get(key), parts[ii]);
											} else {
												pArray[ii] = cb.equal(currentRoot.get(key), parts[ii]);
											}
										}
										predicates.add(cb.or(pArray));
									} else {							
										predicates.add(cb.equal(currentRoot.get(a[i+1]), curr));
									}
								}
								
							}
						}
					}
				} else { //TODO FIX ME need to refactor and reduce the code duplication
					String curr = currentFilter.getValue().toString();
					if (curr.contains("[") && curr.contains("]")) {
						curr = curr.substring(curr.indexOf("[") + 1, curr.lastIndexOf("]"));
						String[] parts = curr.replace("\"", "").split(",");
						Predicate[] pArray = new Predicate[parts.length];
						for (int i = 0; i < parts.length; i++) {
							if (parts[i].startsWith("%")) {
								pArray[i] = cb.like(root.get(key), parts[i]);
							} else {
								pArray[i] = cb.equal(root.get(key), parts[i]);
							}
						}
						predicates.add(cb.or(pArray));
					} else {
						if (curr.startsWith("%")) {
							predicates.add(cb.like(root.get(key), curr));
						} else {
							predicates.add(cb.equal(root.get(key), curr));
						}
					}
				}
			}
			if (!predicates.isEmpty()) {
				Predicate[] tmp = new Predicate[predicates.size()];
				int counter = 0;
				for (Predicate item : predicates) {
					tmp[counter] = item;
					counter++;
				}
				cq.where(tmp);
			}
			if(limit > 100) {
				limit = 100;
			}
			TypedQuery<T> q = entityManager.createQuery(cq).setFirstResult(skip).setMaxResults(limit);
			List<T> a = q.getResultStream().collect(Collectors.toList());
			//getCount(cls, filters);  //TODO FIX ME 
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private Class findClassEntity(String className) {
		if (className != null && className.length() > 2)
			className = className.substring(0, 1).toUpperCase() + className.substring(1);
		Class resp = null;
		try {
			resp = Class.forName(StaticBean.ENTITY_PACKAGE + className);
		} catch (ClassNotFoundException e) {
		}
		if (resp == null) {
			try {
				resp = Class.forName(StaticBean.ORACLE_ENTITY_PACKAGE + className);
			} catch (ClassNotFoundException e) {
			}
		}
		return resp;
	}

	@SuppressWarnings("rawtypes")
	private boolean instanceCheck(Class c) {
		// System.out.println("c: " + c);
		if (core == null) {
			coreInit();
		}
		// System.out.println("check");
		if (c != null) {
			for (Class[] array : core) {
				for (int i = 0; i < array.length; i++) {
					if (c.isInstance(array[i])) {
						return false;
					}
				}

			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	private void coreInit() {
		core = new LinkedList<Class[]>();
		core.add(StaticBean.CORE_PRIMITIVE_CLASSES);
		core.add(StaticBean.CORE_DATE_CLASSES);
		core.add(StaticBean.CORE_REFERENCE_CLASSES);
	}

	@Override
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int count) {
		this.totalCount = count;
	}

	private void findEntityManager() {
		MicroFinder mc = new MicroFinder();
		String name = mc.findDatabase(this.modelCls);
		try {
			name = name.toLowerCase();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (name == null) {
			this.entityManager = CoreEntityManager.getPostgresEntityManager();
		} else {
			switch (name) {
			case "postgres":
				this.entityManager = CoreEntityManager.getPostgresEntityManager();
				break;
			case "oracle":
				this.entityManager = CoreEntityManager.getOracleEntityManager();
				break;

			default:
				this.entityManager = CoreEntityManager.getPostgresEntityManager();
			}
		}
	}

//	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, propagation = Propagation.REQUIRED)
//	public void test(CriteriaQuery cq) {
//		   CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		   CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
//		   Predicate restriction = critQ.getRestriction();
//		   countCriteria.where(restriction);
//		   Root<T> root = cq.from(modelCls);
//		   countCriteria.select(cb.countDistinct(root));
//		   
//		   TypedQuery<Long> queryCount = entityManager.createQuery(countCriteria);
//		   Long count = queryCount.getSingleResult();
//		   System.out.println("COUNT: "+count);
//	}

}
