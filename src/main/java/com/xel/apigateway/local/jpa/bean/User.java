package com.xel.apigateway.local.jpa.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xel.apigateway.local.annotation.Database;
import com.xel.apigateway.local.util.PasswordEncryptor;

/**
 * The persistent class for the frame database table.
 * 
 */

@Database
@Transactional
@Entity
@Table(name = "user", schema = "public")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 938205715588059493L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_user")
	private Long userId;

	@Column(name = "username")
	private String username;

	@Column(name = "display_name")
	private String displayName;

	@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "password")
	private String password;

	@Column(name = "verified")
	private boolean verified;

	@Column(name = "email")
	private String email;

	@JsonInclude(Include.NON_NULL)
	@Nullable
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AccessToken> accessTokens;

	@JsonInclude(Include.NON_NULL)
	@Nullable
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserPermission> userPermissions;

	@JsonInclude(Include.NON_NULL)
	@Nullable
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Ip> ips;
	
	@JsonIgnoreProperties({"user","hibernateLazyInitializer"})
	@JsonInclude(Include.NON_NULL)
	@Nullable
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<UserGroupLink> userGroupLink;
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		String encryptedPass = null;

		PasswordEncryptor pe = new PasswordEncryptor();
		encryptedPass = pe.encrypt(password);

		this.password = encryptedPass;
	}

	@JsonIgnore
	public List<AccessToken> getAccessTokens() {
		return accessTokens;
	}

	public void setAccessTokens(List<AccessToken> accessTokens) {
		this.accessTokens = accessTokens;
	}

	@JsonIgnore
	public List<Ip> getIps() {
		return ips;
	}

	public void setIps(List<Ip> ips) {
		this.ips = ips;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<UserPermission> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(List<UserPermission> userPermissions) {
		this.userPermissions = userPermissions;
	}

	public List<UserGroupLink> getUserGroupLink() {
		return userGroupLink;
	}

	public void setUserGroupLink(List<UserGroupLink> userGroupLink) {
		this.userGroupLink = userGroupLink;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}