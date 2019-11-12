package com.xel.apigateway.gateway.core.jpa.auth;

import java.util.List;

import com.xel.apigateway.local.jpa.bean.Permission;

/**
 * 
 * @author xeldawe
 *
 */
public interface PermissionRepository {
	public List<Permission> findNoPermission();
	public Permission findPermission(String proxy, String httpMethod);
}
