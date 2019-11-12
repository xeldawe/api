package com.xel.apigateway.gateway.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.xel.apigateway.gateway.core.jpa.Service;
import com.xel.apigateway.local.bean.Login;
import com.xel.apigateway.local.bean.LoginWithToken;
import com.xel.apigateway.local.config.TokenConfig;
import com.xel.apigateway.local.jpa.bean.AccessToken;
import com.xel.apigateway.local.jpa.bean.User;
import com.xel.apigateway.local.util.CurrentDate;
import com.xel.apigateway.local.util.PasswordEncryptor;
import com.xel.apigateway.local.util.TokenGenerator;

/**
 * 
 * @author xeldawe
 *
 */
public class LoginService {

	private TokenGenerator tokenGenerator;
	private PasswordEncryptor pe;
	private CurrentDate cd = new CurrentDate();

	private Service service = null;

	private enum UserMode {
		USERNAME, EMAIL;
	}

	public LoginWithToken login(Login login, Service service) {
		this.service = service;
		String user = login.getUser();
		UserMode userMode = null;

		if (user.contains("@")) {
			userMode = UserMode.EMAIL;
		} else {
			userMode = UserMode.USERNAME;
		}

		login.setPassword(encryptPass(login.getPassword()));

		User u = null;
		LoginWithToken resp = null;

		switch (userMode) {
		case USERNAME:
			this.service.setTCls(User.class);
			u = checkWithUsername(login);
	//		System.out.println("USER: "+u.toString());
			if (u != null) {
				resp = new LoginWithToken(u, generateToken());
			}
			break;
		case EMAIL:
//			this.service.setTCls(User.class);
//			u = checkWithEmail(login);
//			if(u!= null) {
//				resp = new LoginWithToken(u, generateToken());
//			}
			break;

		default:
			break;
		}
		if (resp == null) {
			return null;
		}
		this.service.setTCls(AccessToken.class);
		try {
			System.out.println("Resp: "+resp);
			String token = resp.getToken();
							System.out.println("token: "+token);
			if (token != null) {
				AccessToken at = new AccessToken();
				at.setTtl(TokenConfig.TOKEN_TTL);
				at.setAccessToken(token);
				at.setCreateTime(cd.toTimestamp(cd.getCurrentTime()));
				this.service.create(at);
				at.setUser(u);
				this.service.merge(at);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	private String generateToken() {
		tokenGenerator = new TokenGenerator();
		return tokenGenerator.generateToken(16);
	}

	private String encryptPass(String nativePw) {
		String encryptedPass = null;

		pe = new PasswordEncryptor();
		encryptedPass = pe.encrypt(nativePw);

		return encryptedPass;
	}

	// NOT SUPPORTED NOW!
//	private User checkWithEmail(Login login) {
//		Map<String, String> filter = new LinkedHashMap<>();
//		filter.put("email", login.getUser());
//		filter.put("password", login.getPassword());
//		User user = (User) this.service.findWithFilter(filter).get(0);
//		return user;
//
//	}

	private User checkWithUsername(Login login) {
		Map<String, String> filter = new LinkedHashMap<>();
		filter.put("username", login.getUser());
		filter.put("password", login.getPassword());
		System.out.println("Try to get use");
		try {
			User user = (User) this.service.findWithFilter(filter).get(0);
			System.out.println("User: "+ user);
			return user;
		} catch (Exception e) {
			return null;
		}

	}

}