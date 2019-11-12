package com.xel.apigateway.gateway.core.jpa.auth;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xel.apigateway.gateway.util.MicroFinder;
import com.xel.apigateway.local.datasource.CoreEntityManager;
import com.xel.apigateway.local.jpa.bean.AccessToken;
import com.xel.apigateway.local.jpa.bean.Permission;

/**
 * 
 * @author xeldawe
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
@Repository
@EnableTransactionManagement
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
public class PermissionRepositoryImpl implements PermissionRepository {

	private EntityManager entityManager;

	// private static final CurrentDate CD = new CurrentDate();

	private Map<String, Object> filters;

	private static final String ENTITY_PACKAGE = "com.xel.apigateway.local.jpa.bean.";

	private static final Class[] CORE_PRIMITIVE_CLASSES = { char.class, Byte.class, Short.class, Long.class,
			Integer.class, Double.class, Float.class, Boolean.class };

	private static final Class[] CORE_DATE_CLASSES = { Date.class, Timestamp.class };

	private static final Class[] CORE_REFERENCE_CLASSES = { String.class };

	private static List<Class[]> core = null;

	@Override
	public List<Permission> findNoPermission() {
		filters = new LinkedHashMap<String, Object>();
		return (List<Permission>) localCriteriaExecutor(Permission.class, filters);
	}

	@Override
	public Permission findPermission(String proxy, String httpMethod) {
		filters = new LinkedHashMap<String, Object>();
		filters.put("proxy", proxy);
		filters.put("httpMethod", httpMethod);
		return ((List<Permission>) localCriteriaExecutor(Permission.class, filters)).get(0);
	}

	private Object localCriteriaExecutor(Class cls, Map<String, Object> filters) {
		findEntityManager(cls);
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery(cls);
		Root root = cq.from(cls);
		for (Map.Entry<String, Object> currentFilter : filters.entrySet()) {
			String key = currentFilter.getKey();
			if (key.contains(".")) {
				String[] a = key.split(".");
				for (int i = 0; i < a.length; i++) {
					Class c = findClassEntity(a[i]);
					if (instanceCheck(c)) {
						Root r = cq.from(c);
						Object value = currentFilter.getValue();
						if (value == null) {
							cq.where(cb.isNull(r.get(a[i] + 1)));
						} else {
							cq.where(cb.equal(r.get(a[i] + 1), value));
						}
						break;
					}
				}
			} else {
				Object value = currentFilter.getValue();
				if (value == null) {
					cq.where(cb.isNull(root.get(key)));
				} else {
					cq.where(cb.equal(root.get(key), value));
				}
			}
		}
		TypedQuery<AccessToken> q = entityManager.createQuery(cq);
		return q.getResultList();
	}

	private Class findClassEntity(String className) {
		if (className != null && className.length() > 2)
			className = className.substring(0, 1).toUpperCase() + className.substring(1);
		try {
			return Class.forName(ENTITY_PACKAGE + className);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	private boolean instanceCheck(Class c) {
		if (core == null) {
			coreInit();
		}
		for (Class[] array : core) {
			for (int i = 0; i < array.length; i++) {
				if (c.isInstance(array[i])) {
					return false;
				}
			}

		}
		return true;
	}

	private void coreInit() {
		core = new LinkedList<Class[]>();
		core.add(CORE_PRIMITIVE_CLASSES);
		core.add(CORE_DATE_CLASSES);
		core.add(CORE_REFERENCE_CLASSES);
	}

	private void findEntityManager(Class c) {
		try {
			MicroFinder mc = new MicroFinder();
			String name = mc.findDatabase(c);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
