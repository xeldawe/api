package com.xel.apigateway.gateway.core;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.xel.apigateway.gateway.bean.Auth;
import com.xel.apigateway.gateway.bean.Response;
import com.xel.apigateway.gateway.core.jpa.ModelService;
import com.xel.apigateway.gateway.core.jpa.auth.AuthStatus;
import com.xel.apigateway.local.util.TokenGenerator;

/**
 * 
 * @author xeldawe
 *
 */
@Service
public class CoreProcess {

	@Autowired
	ModelService modelService;

	@Autowired
	AuthChecker authChecker;

	private static final EndpointSelector es = new EndpointSelector();
	private static final TaskGenerator tg = new TaskGenerator();

	private static QueueExecturor qe = new QueueExecturor();
	private final TokenGenerator tgr = new TokenGenerator();
	
	
	public synchronized Response addCall(EndpointSelectorBean esb) {
		Response r = new Response<>();
		String token = esb.getRequest().getHeader("accessToken");
		Auth auth = authChecker.check(token, esb.getProxy(), esb.getH());
		AuthStatus as = auth.getPermissionStatus();
		Error error = null;
		switch (as) {
		case PERMISSION_GRANTED:

			break;
		case PERMISSION_DENIED:
			error = new Error(HttpStatus.FORBIDDEN, 403, "PERMISSION_DENIED");
			r.setError(error);
			r.setAuth(auth);
			return r;

		default:
			error = new Error(HttpStatus.FORBIDDEN, 403, "PERMISSION_DENIED");
			r.setError(error);
			r.setAuth(auth);
			return r;
		}

		if (!as.equals(AuthStatus.PERMISSION_GRANTED)) {
			switch (auth.getAuthStatus()) {
			case AUTHORIZED:

				break;
			case UNAUTHORIZED:
				error = new Error(HttpStatus.UNAUTHORIZED, 401, "UNAUTHORIZED");
				r.setError(error);
				r.setAuth(auth);
				return r;

			default:
				error = new Error(HttpStatus.UNAUTHORIZED, 401, "UNAUTHORIZED");
				r.setError(error);
				r.setAuth(auth);
				return r;
			}
		}
		
		Long time = System.currentTimeMillis();

		r.setRequestTime(Timestamp.valueOf(LocalDateTime.now()));
		StringBuilder sb = new StringBuilder();
		sb.append(esb.getRequest());
		sb.append(tgr.generateToken(8));
		String uniqueId = sb.toString();
		Task task = tg.generate(esb, uniqueId);
		task.getController().set(modelService);
		esb.setResp(r);
		String returnedId = es.addEndpointRequest(task);
		Response<?> resp = null;
		if (returnedId == null) {
			resp.setError(new Error(HttpStatus.NOT_FOUND, 404, null));
			return resp;
		}
		LocalDateTime timeout = LocalDateTime.now();
		while (resp == null) {
			resp = qe.getById(returnedId);
			if (resp == null) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
			}
			if (LocalDateTime.now().isAfter(timeout.plusSeconds(10))) {
				resp = new Response<>();
				break;
			}
		}
		time = System.currentTimeMillis() - time;
		resp.setResponseTime(Timestamp.valueOf(LocalDateTime.now()));
		resp.setAuth(auth);
		return resp;

	}

}
