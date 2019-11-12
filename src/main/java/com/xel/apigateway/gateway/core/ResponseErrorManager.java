package com.xel.apigateway.gateway.core;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.xel.apigateway.gateway.bean.Response;

/**
 * 
 * @author xeldawe
 *
 */
@Service
public class ResponseErrorManager {

	/**
	 * Set response error status automatically
	 * @param response
	 * @param r
	 * @param httpMethod
	 * @return HttpServletResponse
	 */
	public HttpServletResponse responseErrorCorrector(HttpServletResponse response, Response r, HttpMethod httpMethod) {
		if (r.getError() != null) {
			response.setStatus(r.getError().getHttpStatusCode());
		} else {
			switch (httpMethod) {
			case GET:
				response.setStatus(HttpStatus.OK.value());
				break;
			case POST:
				response.setStatus(HttpStatus.CREATED.value());
				break;
			case PATCH:
				response.setStatus(HttpStatus.OK.value());
				break;
			case DELETE:
				response.setStatus(HttpStatus.OK.value());
				break;

			default:
				response.setStatus(HttpStatus.OK.value());
			}

		}
		return response;
	}

}
