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

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="user_group", schema = "public")
@NamedQuery(name="UserGroup.findAll", query="SELECT ug FROM UserGroup ug")
public class UserGroup  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_user_group")
	private Long userGroupId;
	
	private String name;
	
	private int permissionLevel;
	
	@JsonBackReference
	@JsonInclude(Include.NON_NULL)
	@Nullable
	@OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<UserGroupLink> userGroupLink;

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPermissionLevel() {
		return permissionLevel;
	}

	public void setPermissionLevel(int permissionLevel) {
		this.permissionLevel = permissionLevel;
	}

	public List<UserGroupLink> getUserGroupLink() {
		return userGroupLink;
	}

	public void setUserGroupLink(List<UserGroupLink> userGroupLink) {
		this.userGroupLink = userGroupLink;
	}

	
}
