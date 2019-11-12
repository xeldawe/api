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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="site", schema = "public")
@NamedQuery(name="Site.findAll", query="SELECT s FROM Site s")
public class Site implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1868482735802614028L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_site")
	private Long siteId;
	
	private String name;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@JsonInclude(Include.NON_NULL)
	@Nullable
	@OneToMany(mappedBy = "site", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SiteTranslationLink> siteTranslationLink;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer"})
	@JsonInclude(Include.NON_NULL)
	@Nullable
	@OneToMany(mappedBy = "site", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<SiteLanguage> siteLanguages;

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SiteTranslationLink> getSiteTranslationLink() {
		return siteTranslationLink;
	}

	public void setSiteTranslationLink(List<SiteTranslationLink> siteTranslationLink) {
		this.siteTranslationLink = siteTranslationLink;
	}

	public List<SiteLanguage> getSiteLanguages() {
		return siteLanguages;
	}

	public void setSiteLanguages(List<SiteLanguage> siteLanguages) {
		this.siteLanguages = siteLanguages;
	}
		
}
