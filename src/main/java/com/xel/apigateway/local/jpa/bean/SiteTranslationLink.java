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
@Table(name="site_translation_link", schema = "public")
@NamedQuery(name="SiteTranslationLink.findAll", query="SELECT stl FROM SiteTranslationLink stl")
public class SiteTranslationLink  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7358506385045691944L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_site_translation_link")
	private Long siteTranslationLinkId;
	
	@JsonIgnoreProperties({"siteTranslationLink","siteLanguages", "hibernateLazyInitializer"})
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="siteId",referencedColumnName="id_site", insertable=true, updatable=true)
	private Site site;
	
	@JsonIgnoreProperties({"language", "hibernateLazyInitializer"})
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="translationId",referencedColumnName="id_translation", insertable=true, updatable=true)
	private Translation Translation;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="languageId",referencedColumnName="id_language", insertable=true, updatable=true)
	private Language language;

	public Long getSiteTranslationLinkId() {
		return siteTranslationLinkId;
	}

	public void setSiteTranslationLinkId(Long siteTranslationLinkId) {
		this.siteTranslationLinkId = siteTranslationLinkId;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Translation getTranslation() {
		return Translation;
	}

	public void setTranslation(Translation translation) {
		Translation = translation;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
	
	
}
