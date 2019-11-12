package com.xel.apigateway.local.bean;

import com.xel.apigateway.local.jpa.bean.User;

public class LoginWithToken {

	public LoginWithToken(User user, String token) {
		super();
		this.user = user;
		this.token = token;
	}

	User user;
	String token;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
