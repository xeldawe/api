package com.xel.apigateway.gateway.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xel.apigateway.gateway.core.jpa.Controller;
import com.xel.apigateway.gateway.core.jpa.Service;
import com.xel.apigateway.gateway.service.LoginService;
import com.xel.apigateway.local.bean.Login;
import com.xel.apigateway.local.bean.LoginWithToken;

/**
 * Custom Controller
 * @author xeldawe
 *
 */
public class LoginController implements Controller{
	
	private LoginService ls = new LoginService();
	
	private com.xel.apigateway.gateway.core.jpa.Service service;
	
	private Class t;
	
	@Override
	public List get() {
		return null;
	}

	@Override
	public List get(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(Map map, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object post(Object obj) {
	
		ObjectMapper mapper = new ObjectMapper();
		Login login = null;
		try {
			login = mapper.readValue(((String) obj), Login.class); //Parse json to login object
		} catch (IOException e) {
//			e.printStackTrace();
		}
		
		LoginWithToken lwt = ls.login(login, this.service);
		if (lwt == null) {
			return new LoginWithToken(null, null);
		}
		
		lwt.getUser().setPassword(null);
		return lwt;
	}

	@Override
	public Object patch(long id, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class getT() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setT(Class t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Service getService() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void set(com.xel.apigateway.gateway.core.jpa.Service modelService) {
		this.service = modelService;
		this.service.setTCls(getT());
	}

	@Override
	public int getTotalCount() {
		// TODO Auto-generated method stub
		return 0;
	}


}

