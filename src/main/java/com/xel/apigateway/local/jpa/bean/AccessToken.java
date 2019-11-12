package com.xel.apigateway.local.jpa.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="access_token", schema = "public")
@NamedQuery(name="AccessToken.findAll", query="SELECT a FROM AccessToken a")
public class AccessToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7503368038145537418L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_access_token")
	private Long accessTokenId;

	@Column(name="access_token")
	private String accessToken;
	
	@Column(name="ttl")
	private int ttl;
	
	@Column(name="create_time")
	private Timestamp createTime;
	
	@JsonIgnoreProperties({"password", "emails", "accessTokens", "accessTokens", "ips", "orders", "ips"})
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;

	public Long getAccessTokenId() {
		return accessTokenId;
	}

	public void setAccessTokenId(Long accessTokenId) {
		this.accessTokenId = accessTokenId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}