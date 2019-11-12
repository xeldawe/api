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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="translation", schema = "public")
@NamedQuery(name="Translation.findAll", query="SELECT t FROM Translation t")
public class Translation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 235186042320694654L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_translation")
	private Long translationId;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="languageId",referencedColumnName="id_language", insertable=true, updatable=true)
	private Language language;
	
	private String value;
	
	private String name;
	
	@Column(name="default_value")
	private String defaultValue;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="userGroupId",referencedColumnName="id_user_group", insertable=true, updatable=true)
	private UserGroup userGroup;
	
	public Long getTranslationId() {
		return translationId;
	}
	public void setTranslationId(Long translationId) {
		this.translationId = translationId;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
