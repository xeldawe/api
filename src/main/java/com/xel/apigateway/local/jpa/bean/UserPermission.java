package com.xel.apigateway.local.jpa.bean;

import java.io.Serializable;
import java.util.List;

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

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="user_permission", schema = "public")
@NamedQuery(name="UserPermission.findAll", query="SELECT up FROM UserPermission up")
public class UserPermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 370847645826725567L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_user_permission")
	private Long userPermissionId;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="userId",referencedColumnName="id_user", insertable=true, updatable=true)
	private User user;
	
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="permissionId",referencedColumnName="id_permission", insertable=true, updatable=true)
	private Permission permission;
	

	public Long getUserPermissionId() {
		return userPermissionId;
	}
	public void setUserPermissionId(Long userPermissionId) {
		this.userPermissionId = userPermissionId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}

		
}
