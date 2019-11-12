package com.xel.apigateway.local.util;

import java.security.SecureRandom;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {

	protected static SecureRandom random = new SecureRandom();
	
	public String generateToken(int maxRange) {
		StringBuilder sb = new StringBuilder();
		IntStream.range(0, maxRange).parallel().forEachOrdered(i -> {
		     sb.append(Long.toHexString(random.nextInt(16)));
		});
		return (sb.toString());
	}
}
