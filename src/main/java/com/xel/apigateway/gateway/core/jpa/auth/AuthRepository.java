package com.xel.apigateway.gateway.core.jpa.auth;

import org.springframework.stereotype.Repository;

import com.xel.apigateway.local.jpa.bean.AccessToken;
import com.xel.apigateway.local.jpa.bean.User;

/**
 * 
 * @author xeldawe
 *
 */
@Repository
public interface AuthRepository {
	public AccessToken findATByToken(String token);
	public void updateToken(long tokenId);
	public void removeAccessTokenByToken(String token);
	public void removeAccessTokenById(long id);
	public void removeAccessTokenByUser(User user);
	public void removeAccessTokenByUserId(long id);
	
}
