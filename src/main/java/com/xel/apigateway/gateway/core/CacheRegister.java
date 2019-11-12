package com.xel.apigateway.gateway.core;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.xel.apigateway.gateway.bean.Response;
import com.xel.apigateway.local.util.CurrentDate;


/**
 * 
 * @author xeldawe
 *
 */
@Service
public class CacheRegister {

	// Cache time
	private final static long TTL = 3000;
	// Locked endpoints
	private static Map<String, Boolean> lockedEdpoints = new HashMap();
	// Time support
	private static final CurrentDate cd = new CurrentDate();
	// Stored cache objects
	private static Map<String, Cache> cacheMap = new LinkedHashMap<String, Cache>();

	/**
	 * Endpoint check. If available cache for this endpoint this will check and
	 * return it. Else return null.
	 * 
	 * @param endpoint
	 * @param hm
	 * @return Response
	 */
	public Response<?> check(String endpoint, HttpMethod hm) {
		if (cacheMap != null && !cacheMap.isEmpty()) {

				Cache currentCache = cacheMap.get(endpoint);
				if (currentCache != null) {
					if (currentCache.getHm() == hm) {
						LocalDateTime start = currentCache.getStartTime();
						Timestamp currentTime = cd.toTimestamp(cd.getCurrentTime());

						if (cd.toTimestamp(start.plusSeconds(TTL / 1000)).before(currentTime)) {
							cacheMap.remove(endpoint);
							return null;
						} else {
							return (Response<?>) currentCache.getLastObject();
						}
					}
				}
			}

		return null;
	}

	/**
	 * Activate cache on endpoint.
	 * 
	 * @param endpoint
	 * @param hm
	 * @param lastObject
	 * @param startTime
	 */
	public void addToCache(String endpoint, HttpMethod hm, Object lastObject, LocalDateTime startTime) {
		Cache c = new Cache(hm, lastObject, startTime);
		cacheMap.put(endpoint, c);
		try {
			lockedEdpoints.remove(endpoint);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Add endpoint lock
	 * 
	 * @param endpoint
	 */
	public void addEndpointToLock(String endpoint) {
		lockedEdpoints.put(endpoint, false);
	}

	/**
	 * Remove endpoint lock.
	 * 
	 * @param endpoint
	 */
	public void removeEndpointLock(String endpoint) {
		lockedEdpoints.remove(endpoint);
	}

	/**
	 * This will check the endpoint status. Return true if endpoint is not locked.
	 * 
	 * @param endpoint
	 * @return boolean
	 */
	public boolean checkEndpointStatus(String endpoint) {
		Boolean res = lockedEdpoints.get(endpoint);
		if (res == null)
			return true;
		else {
			if (res) {
				lockedEdpoints.remove(endpoint);
				return res;
			} else {
				return res;
			}
		}
	}

}
