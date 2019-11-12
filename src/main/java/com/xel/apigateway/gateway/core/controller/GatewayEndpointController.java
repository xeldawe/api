package com.xel.apigateway.gateway.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.xel.apigateway.gateway.bean.Response;
import com.xel.apigateway.gateway.core.CacheRegister;
import com.xel.apigateway.gateway.core.CoreProcess;
import com.xel.apigateway.gateway.core.ProcessModeSelector;
import com.xel.apigateway.gateway.core.ResponseErrorManager;

/**
 * 
 * @author xeldawe
 *
 */
@RestController
@RequestMapping
@Async("rest")
@Scope(value = WebApplicationContext.SCOPE_REQUEST,  proxyMode = ScopedProxyMode.NO)
@CrossOrigin(origins = { "http://localhost:4200", "https://xeldawe.com/api/", "https://xeldawe.com" })
public class GatewayEndpointController {

	@Autowired
	private CoreProcess cp;

	@Autowired
	private CacheRegister cr;

	@Autowired
	private ResponseErrorManager rem;

	private ProcessModeSelector pms;

	@SuppressWarnings("rawtypes")
	private Response r = null;

	private HttpMethod httpMethod;

	@GetMapping({ "/secure/{proxy}*", "/secure/{proxy}/**" })
	private Response<?> getGet(@PathVariable(value = "proxy") String proxy,
			 HttpServletRequest request, HttpServletResponse response) {
		String secure = "secure";
		httpMethod = HttpMethod.GET;

		try {
			pms = new ProcessModeSelector(secure, proxy, request, response, httpMethod, cp, cr);
			r = pms.getResponse();
			response = rem.responseErrorCorrector(response, r, httpMethod);
			return r;
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return r;
		}

	}

	@PostMapping({ "/secure/{proxy}*" })
	private Response<?> getPost(@PathVariable(value = "proxy") String proxy,
			 HttpServletRequest request, HttpServletResponse response) {
		String secure = "secure";
		httpMethod = HttpMethod.POST;

		try {
			pms = new ProcessModeSelector(secure, proxy, request, response, httpMethod, cp);
			r = pms.getResponse();
			response = rem.responseErrorCorrector(response, r, httpMethod);
			return r;
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return r;
		}

	}

	@PatchMapping({ "/secure/{proxy}/**" })
	private Response<?> getPatch(@PathVariable(value = "proxy") String proxy,
			 HttpServletRequest request, HttpServletResponse response) {
		String secure = "secure";
		httpMethod = HttpMethod.PATCH;

		try {
			pms = new ProcessModeSelector(secure, proxy, request, response, httpMethod, cp);
			r = pms.getResponse();
			response = rem.responseErrorCorrector(response, r, httpMethod);
			return r;
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return r;
		}

	}

	@DeleteMapping({ "/secure/{proxy}/**" })
	private Response<?> getDelete(@PathVariable(value = "proxy") String proxy,
			 HttpServletRequest request, HttpServletResponse response) {
		String secure = "secure";
		httpMethod = HttpMethod.DELETE;

		try {
			pms = new ProcessModeSelector(secure, proxy, request, response, httpMethod, cp);
			r = pms.getResponse();
			response = rem.responseErrorCorrector(response, r, httpMethod);
			return r;
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return r;
		}

	}

}
