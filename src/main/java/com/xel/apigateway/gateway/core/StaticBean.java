package com.xel.apigateway.gateway.core;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 
 * @author xeldawe
 *
 */
public class StaticBean {

	public static final String ENTITY_PACKAGE = " com.xel.apigateway.local.jpa.bean";
	public static final String ORACLE_ENTITY_PACKAGE = " com.xel.apigateway.local.oracle.bean";

	@SuppressWarnings("rawtypes")
	public static final Class[] CORE_PRIMITIVE_CLASSES = { char.class, Byte.class, Short.class, Long.class,
			Integer.class, Double.class, Float.class, Boolean.class };

	@SuppressWarnings("rawtypes")
	public static final Class[] CORE_DATE_CLASSES = { Date.class, Timestamp.class };

	@SuppressWarnings("rawtypes")
	public static final Class[] CORE_REFERENCE_CLASSES = { String.class };

}
