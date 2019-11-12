package com.xel.apigateway.gateway.core;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.xel.apigateway.gateway.bean.Auth;
import com.xel.apigateway.gateway.core.jpa.auth.AuthRepository;
import com.xel.apigateway.gateway.core.jpa.auth.AuthStatus;
import com.xel.apigateway.gateway.core.jpa.auth.PermissionRepository;
import com.xel.apigateway.local.jpa.bean.AccessToken;
import com.xel.apigateway.local.jpa.bean.Permission;
import com.xel.apigateway.local.jpa.bean.User;
import com.xel.apigateway.local.util.CurrentDate;

/**
 * 
 * @author xeldawe
 *
 */
@Service
public class AuthChecker {

	@Autowired
	AuthRepository authRepository;

	@Autowired
	PermissionRepository permissionRepository;

	private static final CurrentDate CD = new CurrentDate();

	private static Map<String, LocalDateTime> blacklistedTokens = new LinkedHashMap<>();
	private static ExecutorService es;

	private static boolean blackListCleanerStatus = true;

	private String token;
	private String proxy;
	private HttpMethod hm;
	private User user;

	private void initBlackListCleaner() {
		es = Executors.newFixedThreadPool(1);
		es.execute(blackListCleaner);
	}

	// Guest endpoints
	private static Map<String, List<HttpMethod>> openProxy;

	/**
	 * Authentication and authorization. Check everything what you need.
	 * 
	 * @param token
	 * @param proxy
	 * @param hm
	 * @return Auth
	 */
	public Auth check(String token, String proxy, HttpMethod hm) {
		
		if (es == null) {
			initBlackListCleaner();
		}
		
		if (openProxy == null) {
			initOpenProxy();
		}
//		System.out.println("TOKEN: "+token);
		this.token = token;
		this.proxy = proxy;
		this.hm = hm;

		Auth respAuth = new Auth();
		boolean permissionStatus = false;

		// Guest endpoints
		List<HttpMethod> enabled = openProxy.get(proxy);

		if (enabled != null && !enabled.isEmpty()) {
			for (HttpMethod item : enabled) {
				if (item == hm) {
					permissionStatus = true;
				}
			}
		}

		// Anti spam
		LocalDateTime bt = blacklistedTokens.get(token);
		if (bt != null) {
			token = null;
		}

		// For guest endpoints
		if (token == null && !permissionStatus) {
			return new Auth(token, AuthStatus.UNAUTHORIZED, AuthStatus.PERMISSION_DENIED);
		} else if (permissionStatus) {
			blacklistedTokens.put(token, CD.getCurrentTime());
			return new Auth(token, AuthStatus.UNAUTHORIZED, AuthStatus.PERMISSION_GRANTED);
		}

		// For normal endpoints
		if (authentication()) {
			if (authorization()) {
				return new Auth(token, AuthStatus.AUTHORIZED, AuthStatus.PERMISSION_GRANTED);
			}
			return new Auth(token, AuthStatus.AUTHORIZED, AuthStatus.PERMISSION_DENIED);
		} else {
			blacklistedTokens.put(token, CD.getCurrentTime());
			return new Auth(token, AuthStatus.UNAUTHORIZED, AuthStatus.PERMISSION_DENIED);
		}

	}

	/**
	 * Authenticate from token. If token is valid return true else return false.
	 * 
	 * @return boolean
	 */
	private boolean authentication() {
		AccessToken at = null;
		try {
			at = authRepository.findATByToken(this.token);
			LocalDateTime created = at.getCreateTime().toLocalDateTime();
			created = created.plusSeconds(at.getTtl());
			LocalDateTime currentTime = CD.getCurrentTime();
			if (currentTime.isBefore(created) || currentTime.isEqual(created)) {
				this.user = at.getUser();
			}
			if (this.user != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check user permissions. If he have permission for action he will return true
	 * else return false.
	 * 
	 * @return boolean
	 */
	private boolean authorization() {
		long id = this.user.getUserId(); //TODO - FIX ME
		Permission permission = permissionRepository.findPermission(this.proxy, this.hm.toString());
		if (permission != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Find opened endpoints for guest users and store it.
	 */
	private void initOpenProxy() {
		openProxy = new LinkedHashMap<String, List<HttpMethod>>();
		List<Permission> permissions = permissionRepository.findNoPermission();
		for (Permission permission : permissions) {
			HttpMethod hm = null;
			String key = permission.getProxy();
			switch (permission.getHttpMethod().toUpperCase()) {
			case "GET":
				hm = HttpMethod.GET;
				break;
			case "POST":
				hm = HttpMethod.POST;
				break;
			case "PATCH":
				hm = HttpMethod.PATCH;
				break;
			case "DELETE":
				hm = HttpMethod.DELETE;
				break;

			default:
				break;
			}
			List<HttpMethod> tmp = openProxy.get(key);
			if (tmp == null) {
				tmp = new LinkedList<HttpMethod>();
				tmp.add(hm);
			} else {
				tmp.add(hm);

			}
			openProxy.put(key, tmp);
		}

	}

	Runnable blackListCleaner = () -> {
		Map<String, LocalDateTime> tmp;
		while (blackListCleanerStatus) {
			tmp = new LinkedHashMap<String, LocalDateTime>();
			tmp.putAll(blacklistedTokens);
			for (Entry<String, LocalDateTime> item : tmp.entrySet()) {
				String key = item.getKey();
				LocalDateTime startTime = item.getValue().plusMinutes(1);
				if (CD.getCurrentTime().isBefore(startTime)) {
					System.out.println("Removed token: " + key);
					blacklistedTokens.remove(key);
				}
			}
			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
	};

}
