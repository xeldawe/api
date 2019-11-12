package com.xel.apigateway.local.jpa.bean;

import java.io.Serializable;
import java.sql.Timestamp;

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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name = "ip", schema = "public")
@NamedQuery(name = "Ip.findAll", query = "SELECT i FROM Ip i")
public class Ip implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6650657317430186991L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ip")
	private Long ipId;

	@Column(name = "ip")
	private String ip;

	@Column(name = "create_time")
	private Timestamp createTime;

	@ManyToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "id_user", insertable = false, updatable = true)
	private User user;

	public Long getIpId() {
		return ipId;
	}

	public void setIpId(Long ipId) {
		this.ipId = ipId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}