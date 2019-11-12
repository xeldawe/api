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

import com.xel.apigateway.local.annotation.Database;

@Database
@Transactional
@Entity
@Table(name="address", schema = "public")
@NamedQuery(name="Address.findAll", query="SELECT a FROM Address a")
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7740616325002782551L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_address")
	private Long addressId;

	@Column(name="county")
	private String county;
	
	@Column(name="zip_code")
	private String zipCode;
	
	@Column(name="city")
	private String city;
	
	@Column(name="street")
	private String street;
	
	@Column(name="floor")
	private String floor;
	
	@Column(name="number")
	private String number;
	
	@Column(name="prefix_number_text")
	private String prefixNumberText;
	
	@Column(name="postfix_number_text")
	private String postfixNumberText;
	

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPrefixNumberText() {
		return prefixNumberText;
	}

	public void setPrefixNumberText(String prefixNumberText) {
		this.prefixNumberText = prefixNumberText;
	}

	public String getPostfixNumberText() {
		return postfixNumberText;
	}

	public void setPostfixNumberText(String postfixNumberText) {
		this.postfixNumberText = postfixNumberText;
	}


}