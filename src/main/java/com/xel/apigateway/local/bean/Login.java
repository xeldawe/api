package com.xel.apigateway.local.bean;

public class Login {
	
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Login(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}

	private String user;
	private String password;

	public String getUser() {
		return user;
	}

	public void setUsername(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Login [user=" + user + ", password=" + password + "]";
	}

}
