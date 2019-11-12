package com.xel.apigateway.local.logger;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;

public class LoggerBean {

	public LoggerBean(Class c, String message) {
		super();
		this.c = c;
		this.message = message;
	}

	public LoggerBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	Class c;
	String message;

	public Class getC() {
		return c;
	}

	public void setC(Class c) {
		this.c = c;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
