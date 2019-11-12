package com.xel.apigateway.gateway.core;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.xel.apigateway.gateway.bean.Response;
import com.xel.apigateway.gateway.core.jpa.auth.AuthStatus;
import com.xel.apigateway.local.util.CurrentDate;

/**
 * Select processing mode
 * 
 * @author xeldawe
 *
 */
public class ProcessModeSelector {
	

	private static final IpBlocker IP_BLOCKER = new IpBlocker(10,1);

	private enum SecureMode {
		SECURE, // For live
		SANDBOX, // For test
	}

	public ProcessModeSelector(String secure, String proxy, HttpServletRequest request, HttpServletResponse response,
			HttpMethod httpMethod, CoreProcess xp) {
		super();
		this.secure = secure;
		this.proxy = proxy;
		this.request = request;
		this.response = response;
		this.httpMethod = httpMethod;
		this.xp = xp;
	}

	public ProcessModeSelector(String secure, String proxy, HttpServletRequest request, HttpServletResponse response,
			HttpMethod httpMethod, CoreProcess xp, CacheRegister cr) {
		super();
		this.secure = secure;
		this.proxy = proxy;
		this.request = request;
		this.response = response;
		this.httpMethod = httpMethod;
		this.xp = xp;
		this.cr = cr;
	}

	private String secure;
	private String proxy;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpMethod httpMethod;
	private CoreProcess xp;
	private CacheRegister cr;

	private SecureMode secureMode;

	public Response getResponse() {
		modeSelect();
		return processSelector();
	}

	private void modeSelect() {

		switch (this.secure) {
		case "secure":
			this.secureMode = SecureMode.SECURE;
			break;
		case "sandbox":
			this.secureMode = SecureMode.SANDBOX;
			break;

		default:
			break;
		}

	}

	/**
	 * Select live or test mode, then add request call to queue and wait for
	 * results.
	 * 
	 * @return Response
	 */
	private Response processSelector() {
		Response r = null;
		
		if(!IP_BLOCKER.checkIp(request.getRequestURI()+"?"+request.getQueryString(),request.getRemoteAddr())) {
			r = new Response<>();
			r.setError(new Error(HttpStatus.TOO_MANY_REQUESTS, 429, "SPAM NOT ALLOWED"));
			return r;
		}

		switch (this.httpMethod) {

		case GET:
			String endpoint = request.getRequestURI()+"?"+request.getQueryString();
			while (!cr.checkEndpointStatus(endpoint)) { // Check endpoint lock
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
			r = cr.check(endpoint, HttpMethod.GET); // Check endpoint. If have cached object.. he will set.

			if (r == null) {
				r = lockedRequest(r, endpoint);
			} else {
				CurrentDate cd = new CurrentDate();
				r.setRequestTime(cd.toTimestamp(LocalDateTime.now()));
				r.setResponseTime(cd.toTimestamp(LocalDateTime.now()));
				r.setServerProcessTime(0 + " ms");
				r.setQueueWaitingTime(0 + " ms");
			}
			return r;
		case POST:
			r = xp.addCall(new EndpointSelectorBean(HttpMethod.POST, proxy, request, response)); // Post request
			return r;
		case PATCH:
			r = xp.addCall(new EndpointSelectorBean(HttpMethod.PATCH, proxy, request, response)); // Patch request
			return r;
		case DELETE:
			r = xp.addCall(new EndpointSelectorBean(HttpMethod.DELETE, proxy, request, response)); // Delete request
			return r;

		default:
			r = new Response<>();
			r.setError(new Error(HttpStatus.METHOD_NOT_ALLOWED, 405, null));
			return null;
		}
	}

	private Response lockedRequest(Response r, String endpoint) {
		r = cr.check(endpoint, HttpMethod.GET);
		if (r == null) {
			cr.addEndpointToLock(endpoint);
			r = xp.addCall(new EndpointSelectorBean(HttpMethod.GET, proxy, request, response)); // Get request
			if (r.getMessage() != null || r.getAuth().getAuthStatus().equals(AuthStatus.PERMISSION_GRANTED)
					|| (r.getAuth().getAuthStatus().equals(AuthStatus.PERMISSION_GRANTED)
							&& r.getAuth().getAuthStatus().equals(AuthStatus.UNAUTHORIZED))) {
				cr.addToCache(endpoint, HttpMethod.GET, r, LocalDateTime.now()); // Add response to cache
			} else {
				cr.removeEndpointLock(endpoint);
			}
		}else {
			CurrentDate cd = new CurrentDate();
			r.setRequestTime(cd.toTimestamp(LocalDateTime.now()));
			r.setResponseTime(cd.toTimestamp(LocalDateTime.now()));
			r.setServerProcessTime(0 + " ms");
			r.setQueueWaitingTime(0 + " ms");
		}
		return r;
	};

}
