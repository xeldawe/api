package com.xel.apigateway.local.util;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JsonParser {
	
	private ObjectMapper mapper =new ObjectMapper();            

	public JsonParser() {
		super();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public String parse(Object o) {
		String jsonInString = null;
		try {
			jsonInString = mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			
		}
		return jsonInString;
	}
	
	public String parse(List<Object> o) {
		String jsonInString = null;
		try {
			jsonInString = mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			
		}
		return jsonInString;
	}
	
	public Object toObject(String json, Class c) {
		Object obj = null;
		try {
			obj = mapper.readValue(json, c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
