package com.xel.apigateway.gateway.util;

import java.lang.reflect.Field;

import javax.persistence.EntityManager;
import javax.persistence.Id;

import com.xel.apigateway.local.annotation.Database;

/**
 * 
 * @author xeldawe
 *
 */
public class MicroFinder {

	public String entintyId(Class c) {
		String result = null;
		try {
			Field[] fields = c.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getAnnotation(Id.class) != null)
					result = fields[i].getName();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public String findDatabase(Class c) {
		String result = null;
		try {
			Database db =  (Database) c.getAnnotation(Database.class);
			result = db.value();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
}
