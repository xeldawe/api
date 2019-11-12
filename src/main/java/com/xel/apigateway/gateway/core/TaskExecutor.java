package com.xel.apigateway.gateway.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xel.apigateway.gateway.bean.Filter;
import com.xel.apigateway.gateway.bean.Response;
import com.xel.apigateway.gateway.core.controller.ModelController;
import com.xel.apigateway.gateway.core.jpa.Controller;

/**
 * 
 * @author xeldawe
 *
 */
public class TaskExecutor {

	public Response<?> execute(Task task) {
		try {
		String query = task.getEsb().getRequest().getQueryString();
		Map<String, String> m = null;
		if (query != null && query.length() > 0) {
			String[] pairs = query.split("&");
			m = new LinkedHashMap<String, String>();
			for (String pair : pairs) {
				int idx = pair.indexOf("=");
				try {
					m.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
							URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
				} catch (UnsupportedEncodingException e) {

				}
			}
		}

		String endpoint = task.getEsb().getRequest().getRequestURI();
		String tmpEndpoint = null;
		Long endpointId = null;

		Error currentError = null;
		boolean isError = false;
		if (endpoint == null) {
			isError = true;
			currentError = new Error(HttpStatus.SERVICE_UNAVAILABLE, 504, "Unknown endpoint");
		}

		if (!isError) {
			try {
				endpoint = java.net.URLDecoder.decode(endpoint, StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				currentError = new Error(HttpStatus.BAD_REQUEST, 400, e.getMessage());
			}
			endpoint = endpoint.substring(1);
			endpoint = endpoint.substring(endpoint.indexOf("/")+1);
			if(endpoint.endsWith("/")) {
				endpoint = endpoint.substring(0, endpoint.lastIndexOf("/"));
			}
			tmpEndpoint = endpoint.substring(1);
			if (tmpEndpoint.contains("/")) {
				tmpEndpoint = tmpEndpoint.substring(tmpEndpoint.indexOf("/") + 1);
			} else {
				tmpEndpoint = null;
			}

			if (tmpEndpoint != null) {
				try {
					endpointId = Long.valueOf(tmpEndpoint);
				} catch (Exception e) {
					tmpEndpoint = null;
				}
			}
		}

		Response resp = task.getEsb().getResp();

		Long rT = resp.getRequestTime().getTime();
		Long now = Timestamp.valueOf(LocalDateTime.now()).getTime();
		resp.setQueueWaitingTime(now - rT + " ms");
		Long time = System.currentTimeMillis();

		ObjectMapper mapper = new ObjectMapper();

		if (!isError) {
			try {
				Controller c = task.getController();
				String json = null;
				switch (task.getEsb().getH()) {
				case GET:
					
					if (m != null && m.size() > 0) {

						if (endpointId != null) {
							resp.setMessage(c.get(m, endpointId));
							resp.setTotalCount(c.getTotalCount());
						} else {
							resp.setMessage(c.get(m));
							resp.setTotalCount(c.getTotalCount());
						}

					} else {

						if (endpointId != null) {
							resp.setMessage(c.get(endpointId));
							resp.setTotalCount(c.getTotalCount());
						} else {
							resp.setMessage(c.get());
							resp.setTotalCount(c.getTotalCount());
						}
					}
					break;
				case POST:

					json = getBody(task.getEsb().getRequest().getInputStream());
					if (c.getClass() == ModelController.class) {
						mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
						resp.setMessage(c.post(mapper.readValue(json, (Class<?>) task.getModel())));
					} else {
						resp.setMessage(c.post(json));
					}

					break;
				case PATCH:

					json = getBody(task.getEsb().getRequest().getInputStream());
					resp.setMessage(c.patch(endpointId, mapper.readValue(json, (Class<?>) task.getModel())));

					break;
				case DELETE:
					c.delete(endpointId);
					break;

				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				currentError = new Error(HttpStatus.BAD_REQUEST, 400, e.getMessage());
			}
		}

		if (currentError != null) {

			String msg = currentError.getMessage();
			if (msg != null) {
				if (msg.startsWith("Unrecognized field")) {
					msg = msg.substring(0, msg.indexOf("(") - 1).replace("\"", "");
				}
			}
			resp.setMessage(msg);
			currentError.setMessage(null);
		}

		time = System.currentTimeMillis() - time;
		resp.setServerProcessTime(time + " ms");
		resp.setEndpoint(endpoint);
		Filter f = new Filter();
		f.setLimit(100);
		f.setSkip(0);
		f.setFilter(m);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		resp.setHeaders(headers);
		resp.setError(currentError);
		resp.setFilters(f);
		resp.setPriorityQueue(false);
		resp.setAuth(null);
		return resp;
		}catch (Exception e) {
			Error e1 = new Error(HttpStatus.BAD_REQUEST, 400, "Invalid request");
			Response r1 = new Response<>();
			r1.setError(e1);
			return r1;
		}
	}

	private String getBody(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			ReadableByteChannel inputChannel = Channels.newChannel(is);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (inputChannel.read(buffer) > 0) {
				buffer.flip();
				sb.append(new String(buffer.array()));
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
