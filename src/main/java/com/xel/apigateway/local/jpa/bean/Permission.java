package com.xel.apigateway.local.jpa.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Nullable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="permission", schema = "public")
@NamedQuery(name="Permission.findAll", query="SELECT a FROM Permission a")
public class Permission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8716611038048289291L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_permission")
	private Long permissionId;

	@Column(name="proxy")
	private String proxy;
	
	@Column(name="http_method")
	private String httpMethod;
	
	@Column(name="create_time")
	private Timestamp createTime;
	
	@JsonBackReference
	@JsonInclude(Include.NON_NULL)
	@Nullable
	@OneToMany(mappedBy = "permission", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<UserPermission> userPermissions;

	@JsonIgnoreProperties({"user"})
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="userGroupId",referencedColumnName="id_user_group", insertable=true, updatable=true)
	private UserGroup userGroup;
	
	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<UserPermission> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(List<UserPermission> userPermissions) {
		this.userPermissions = userPermissions;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

}