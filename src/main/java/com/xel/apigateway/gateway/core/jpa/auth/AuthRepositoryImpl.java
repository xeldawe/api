package com.xel.apigateway.gateway.core.jpa.auth;

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

import com.xel.apigateway.gateway.core.StaticBean;
import com.xel.apigateway.gateway.util.MicroFinder;
import com.xel.apigateway.local.datasource.CoreEntityManager;
import com.xel.apigateway.local.jpa.bean.AccessToken;
import com.xel.apigateway.local.jpa.bean.User;
import com.xel.apigateway.local.util.CurrentDate;

/**
 * 
 * @author xeldawe
 *
 */
@Service
@Repository
@EnableTransactionManagement
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
public class AuthRepositoryImpl implements AuthRepository {

	private EntityManager entityManager;

	private static final CurrentDate CD = new CurrentDate();

	private Map<String, Object> filters;

	private static List<Class[]> core = null;

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true, propagation = Propagation.REQUIRES_NEW)
	@Override
	public AccessToken findATByToken(String token) {
		filters = new LinkedHashMap<String, Object>();
		filters.put("accessToken", token);
		return (AccessToken) localCriteriaExecutor(AccessToken.class, filters);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateToken(long tokenId) {
		filters = new LinkedHashMap<String, Object>();
		filters.put("accessTokenId", tokenId);
		AccessToken at = (AccessToken) localCriteriaExecutor(AccessToken.class, filters);
		if (at != null) {
			at.setCreateTime(CD.toTimestamp(CD.getCurrentTime()));
			create(at);
		}
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public AccessToken create(AccessToken obj) {
		entityManager.persist(obj);
		return obj;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(long id) {
		entityManager.remove(entityManager.find(AccessToken.class, id));
	}

	@Override
	public void removeAccessTokenByToken(String token) {
		filters = new LinkedHashMap<String, Object>();
		filters.put("accessToken", token);
		AccessToken at = (AccessToken) localCriteriaExecutor(AccessToken.class, filters);
		delete(at.getAccessTokenId());
	}

	@Override
	public void removeAccessTokenById(long id) {
		filters = new LinkedHashMap<String, Object>();
		filters.put("accessTokenId", id);
		AccessToken at = (AccessToken) localCriteriaExecutor(AccessToken.class, filters);
		delete(at.getAccessTokenId());
	}

	@Override
	public void removeAccessTokenByUser(User user) {
		filters = new LinkedHashMap<String, Object>();
		filters.put("User.UserId", user.getUserId());
		AccessToken at = (AccessToken) localCriteriaExecutor(AccessToken.class, filters);
		delete(at.getAccessTokenId());
	}

	@Override
	public void removeAccessTokenByUserId(long id) {
		filters = new LinkedHashMap<String, Object>();
		filters.put("User.UserId", id);
		AccessToken at = (AccessToken) localCriteriaExecutor(AccessToken.class, filters);
		delete(at.getAccessTokenId());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object localCriteriaExecutor(@SuppressWarnings("rawtypes") Class cls, Map<String, Object> filters) {
		findEntityManager(cls);
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		CriteriaQuery cq = cb.createQuery(cls);
		@SuppressWarnings("unchecked")
		Root<AccessToken> root = cq.from(cls);
		for (Map.Entry<String, Object> currentFilter : filters.entrySet()) {
			String key = currentFilter.getKey();
			if (key.contains(".")) {
				String[] a = key.split(".");
				for (int i = 0; i < a.length; i++) {
					Class c = findClassEntity(a[i]);
					if (instanceCheck(c)) {
						Root r = cq.from(c);
						cq.where(cb.equal(r.get(a[i]+1), currentFilter.getValue()));
						break;
					}
				}
			}else {
			cq.where(cb.equal(root.get(key), currentFilter.getValue()));
			}
		}
		TypedQuery<AccessToken> q = entityManager.createQuery(cq);
		return q.getSingleResult();
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

	@SuppressWarnings("rawtypes")
	private void coreInit() {
		core = new LinkedList<Class[]>();
		core.add(StaticBean.CORE_PRIMITIVE_CLASSES);
		core.add(StaticBean.CORE_DATE_CLASSES);
		core.add(StaticBean.CORE_REFERENCE_CLASSES);
	}

	private void findEntityManager(Class c) {
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
	}
	
}
