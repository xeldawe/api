package com.xel.apigateway.gateway.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.xel.apigateway.local.util.CurrentDate;

public class IpBlocker {

	private static Map<String, Integer> lockedIpPerEndpoint = new LinkedHashMap();
	private static final CurrentDate CD = new CurrentDate();

	// 10 request/sec/endpoint/ip
	private static int maxRequest = 10;
	private static int resetTimer = 1;

	private static ExecutorService es;
	private static boolean blackListCleanerStatus = true;

	
	public IpBlocker() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IpBlocker(int maxRequest, int resetTimer) {
		super();
		this.maxRequest = maxRequest;
		this.resetTimer = resetTimer;
	}

	private void initBlackListCleaner() {
		es = Executors.newFixedThreadPool(1);
		es.execute(blackListCleaner);
	}

	public boolean checkIp(String endpoint, String ip) {
		if(ip.contentEquals("127.0.0.1")){
			return true;
		}
		System.out.println("endpoint: "+endpoint);
		System.out.println("ip: "+ip);
		if (es == null) {
			initBlackListCleaner();
		}
		System.out.println("Try to check ip");
		String currentIp = endpoint+ip;

		Integer requestCount = lockedIpPerEndpoint.get(currentIp);
		System.out.println("requestCount: "+requestCount);
		if (requestCount != null) {
			if (requestCount > maxRequest-1) {
				return false;
			} else {
				lockedIpPerEndpoint.put(currentIp, requestCount + 1);
				return true;
			}
		} else {
			lockedIpPerEndpoint.put(currentIp, 1);
			return true;
		}

	}

	Runnable blackListCleaner = () -> {
		Map<String, Integer> tmp;
		while (blackListCleanerStatus) {
			tmp = new LinkedHashMap<String, Integer>();
			tmp.putAll(lockedIpPerEndpoint);
			for (Entry<String, Integer> item : tmp.entrySet()) {
				String key = item.getKey();
				System.out.println("Removed IP: " + key.toString());
				lockedIpPerEndpoint.remove(key);

			}
			try {
				TimeUnit.SECONDS.sleep(resetTimer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
	};


	public static int getMaxRequest() {
		return maxRequest;
	}

	public static void setMaxRequest(int mAX_REQUEST) {
		maxRequest = mAX_REQUEST;
	}

	public static int getResetTimer() {
		return resetTimer;
	}

	public static void setResetTimer(int rESET_TIMER) {
		resetTimer = rESET_TIMER;
	}

	
	
}
