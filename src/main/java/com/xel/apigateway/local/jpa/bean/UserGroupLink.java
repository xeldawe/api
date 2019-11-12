package com.xel.apigateway.local.jpa.bean;

import java.io.Serializable;

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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="user_group_link", schema = "public")
@NamedQuery(name="UserGroupLink.findAll", query="SELECT ugl FROM UserGroupLink ugl")
public class UserGroupLink implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_user_group_link")
	private Long userGroupLinkId;
	
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="userGroupId",referencedColumnName="id_user_group", insertable=true, updatable=true)
	private UserGroup userGroup;
	
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="userId",referencedColumnName="id_user", insertable=true, updatable=true)
	private User user;

	public Long getUserGroupLinkId() {
		return userGroupLinkId;
	}

	public void setUserGroupLinkId(Long userGroupLinkId) {
		this.userGroupLinkId = userGroupLinkId;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
