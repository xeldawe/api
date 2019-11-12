package com.xel.apigateway.local.jpa.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="language", schema = "public")
@NamedQuery(name="Language.findAll", query="SELECT l FROM Language l")
public class Language implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8005807547597785725L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_language")
	private Long languageId;
	
	private String name;
	
	private String code;

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
