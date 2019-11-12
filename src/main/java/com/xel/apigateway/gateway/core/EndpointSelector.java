package com.xel.apigateway.gateway.core;

/**
 * 
 * @author xeldawe
 *
 */
public class EndpointSelector {

	private static final QueueService qs = new QueueService();

	/**
	 * Add task to queue and return added task unique id
	 * @param task
	 * @return long
	 */
	public String addEndpointRequest(Task task ) {
		qs.addtoQueue(task);
		return task.getTaskId();
	}

}
