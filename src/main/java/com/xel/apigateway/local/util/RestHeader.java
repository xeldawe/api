package com.xel.apigateway.local.util;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class RestHeader {

	HttpHeaders headers;

	public RestHeader() {
		super();
		this.headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Headers", "X-Access-Token");
		headers.add("Access-Control-Expose-Headers", "X-Total-Count");
		headers.add("Content-Type", "application/json; charset=utf-8");
	}

	public HttpHeaders getHeaders() {
		return this.headers;
	}

	public void addHeader(String str1, String str2) {
		this.headers.add(str1, str2);
	}

}
