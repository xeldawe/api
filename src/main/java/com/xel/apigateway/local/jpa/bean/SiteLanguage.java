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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name="site_language", schema = "public")
@NamedQuery(name="SiteLanguage.findAll", query="SELECT sl FROM SiteLanguage sl")
public class SiteLanguage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2451639176744764274L;
	
	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_site_language")
	private Long siteLanguageId;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="siteId",referencedColumnName="id_site", insertable=true, updatable=true)
	private Site site;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="languageId",referencedColumnName="id_language", insertable=true, updatable=true)
	private Language language;

	public Long getSiteLanguageId() {
		return siteLanguageId;
	}

	public void setSiteLanguageId(Long siteLanguageId) {
		this.siteLanguageId = siteLanguageId;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

}
