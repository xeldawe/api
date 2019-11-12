package com.xel.apigateway.gateway.config.loader;

/**
 * 
 * @author xeldawe
 *
 */
public class Bean {

	String dbHost;
	String dbUsername;
	String dbPassword;
	String sma;

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getSma() {
		return sma;
	}

	public void setSma(String trHost) {
		this.sma = trHost;
	}

	@Override
	public String toString() {
		return "Bean [dbHost=" + dbHost + ", dbUsername=" + dbUsername + ", dbPassword=" + dbPassword + ", sma=" + sma
				+ "]";
	}

}
