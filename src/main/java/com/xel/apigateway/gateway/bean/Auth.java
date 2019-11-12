package com.xel.apigateway.gateway.bean;

import com.xel.apigateway.gateway.core.jpa.auth.AuthStatus;

/**
 * 
 * @author xeldawe
 *
 */
public class Auth {

	public Auth() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Auth(AuthStatus authStatus, AuthStatus permissionStatus) {
		super();
		this.authStatus = authStatus;
		this.permissionStatus = permissionStatus;
	}

	public Auth(String token, AuthStatus authStatus, AuthStatus permissionStatus) {
		super();
		this.token = token;
		this.authStatus = authStatus;
		this.permissionStatus = permissionStatus;
	}

	private String token;
	private AuthStatus authStatus;
	private AuthStatus permissionStatus;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}



	public AuthStatus getAuthStatus() {
		return authStatus;
	}



	public void setAuthStatus(AuthStatus authStatus) {
		this.authStatus = authStatus;
	}



	public AuthStatus getPermissionStatus() {
		return permissionStatus;
	}



	public void setPermissionStatus(AuthStatus permissionStatus) {
		this.permissionStatus = permissionStatus;
	}



}
