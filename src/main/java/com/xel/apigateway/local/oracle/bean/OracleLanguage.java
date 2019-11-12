package com.xel.apigateway.local.oracle.bean;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xel.apigateway.local.annotation.Database;

@Database("Oracle")
@Transactional
@Entity
@Table(name = "LANGUAGE", schema = "PUBLIC")
public class OracleLanguage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5131641829346623578L;

	@Id
	@Column(name = "ID")
	private Long id;


	@Column(name = "LANG_NAME")
	private String langName;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}

	
}
