package com.xel.apigateway.gateway.bean;

import java.util.Map;

/**
 * 
 * @author xeldawe
 *
 */
public class Filter {

	private int limit;
	private int skip;
	private String orderBy;
	private String orderType;
	private Map<String, String> filter;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Map<String, String> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, String> filter) {
		this.filter = filter;
	}

}
