package com.xel.apigateway.gateway.core;

import java.lang.reflect.InvocationTargetException;

import com.xel.apigateway.gateway.core.controller.ModelController;
import com.xel.apigateway.gateway.core.jpa.Controller;

/**
 * 
 * @author xeldawe
 *
 */
public class TaskGenerator {

	//Default Model package for auto detect
	private static final String MODEL_PACKAGE = "com.xel.apigateway.local.jpa.bean.";
	private static final String ORACLE_MODEL_PACKAGE = "com.xel.apigateway.local.oracle.bean.";
	//Default Controller package for auto detect
	private static final String CONTROLLER_PACKAGE = "com.xel.apigateway.gateway.controller.";

	/**
	 * Generate unique task for TaskExecutor
	 * @param esb
	 * @return Task
	 */
	public Task generate(EndpointSelectorBean esb, String uniqueId) {
		String proxy = esb.getProxy();
		String endpoint = findEnpoint(proxy);
		Class model = findModel(endpoint);
		Controller c = null;
		if (model == null) {
			c = controllerInjector(endpoint);
		} else {
			c = getController(model);
		}
		if (c == null) {
			return null;
		} else {
			return new Task(uniqueId, endpoint, c, findModel(endpoint), esb.getH(), false, 0, esb);
		}
	}

	/**
	 * This fill automatically detect the endpoint by name
	 * @param name
	 * @return String
	 */
	private String findEnpoint(String name) {
		if (name.contains("/"))
			name = name.substring(1);
		if (name.contains("/"))
			name = name.substring(0, name.indexOf("/"));
		if (name.charAt(name.length() - 1) == 's' && !name.contains("twitch")) {
			name = name.substring(0, name.length() - 1);
		}

		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return name;
	}

	/**
	 * Find model by name
	 * @param name
	 * @return Class
	 */
	private Class findModel(String name) {
		Class<?> cls = null;
		try {
			cls = Class.forName(MODEL_PACKAGE + name);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			try {
				cls = Class.forName(ORACLE_MODEL_PACKAGE + name);
			} catch (ClassNotFoundException ee) {
				System.err.println(ee.getMessage());
			}
		}
		return cls;
	}

	/**
	 * Return new ModelController with correct model class
	 * @param cls
	 * @return Controller
	 */
	private Controller getController(Class cls) {
		Controller c = null;
		ModelController mc = new ModelController(); // TODO FIX ME
		mc.setT(cls);
		c = (Controller) mc;
		return c;
	}

	/**
	 * Find the correct Controller by name
	 * 
	 * @param name
	 * @return Controller
	 */
	private Controller controllerInjector(String name) {
		Controller c = null;
		try {
			c = (Controller) Class.forName(CONTROLLER_PACKAGE + name + "Controller").getDeclaredConstructor()
					.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			//
		}
		return c;
	}

}
