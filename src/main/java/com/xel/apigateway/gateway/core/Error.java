package com.xel.apigateway.gateway.core;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author xeldawe
 *
 */
public class Error {

	public Error(HttpStatus httpStatus, int httpStatusCode, String message) {
		super();
		this.httpStatus = httpStatus;
		this.httpStatusCode = httpStatusCode;
		this.message = message;
	}

	public Error() {
		super();
		// TODO Auto-generated constructor stub
	}

	private HttpStatus httpStatus;
	private int httpStatusCode;
	
	@JsonInclude(Include.NON_NULL)
	private String message;

	
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
