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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="account", schema = "public")
@NamedQuery(name="Account.findAll", query="SELECT e FROM Account e")
public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3130725650199195124L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_account")
	private Long accountId;

	@Column(name="account")
	private String account;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, optional=true, fetch = FetchType.LAZY)
	@JoinColumn(name="userId",referencedColumnName="id_user", insertable=true, updatable=true)
	private User user;

	@Column(name="balance")
	private double balance;
	
	@Column(name="default_currency")
	private String defaultCurrency;
	
	@Column(name="ticket")
	private long ticket;
	
	@Column(name="status")
	private boolean status;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public long getTicket() {
		return ticket;
	}

	public void setTicket(long ticket) {
		this.ticket = ticket;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
