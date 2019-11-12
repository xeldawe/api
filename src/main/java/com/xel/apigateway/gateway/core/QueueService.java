package com.xel.apigateway.gateway.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author xeldawe
 *
 */
public class QueueService {

	//All avaiable queues
	final static List<List<ConcurrentLinkedQueue<Task>>> queues = new ArrayList<>();
	private final static List<ConcurrentLinkedQueue<Task>> priotityQueues = new ArrayList<>();
	private final static List<ConcurrentLinkedQueue<Task>> normalQueues = new ArrayList<>();

	//Max normal and max priority queues
	private final int LEVELS = 2;
	
	/**
	 * Generate queues if does not exist
	 */
	public QueueService() {
		super();
		//def: 0,1
		if (priotityQueues.isEmpty()) { 
			initQueue(priotityQueues);
		}
		//def: 0,1
		if (normalQueues.isEmpty()) {
			initQueue(normalQueues);
		}
	}

	/**
	 * Init queues by level
	 * @param queues
	 */
	private void initQueue(List<ConcurrentLinkedQueue<Task>> queues) {
		for (int i = 0; i < LEVELS; i++) {
			queues.add(new ConcurrentLinkedQueue<Task>());
		}
		this.queues.add(queues);
	}

	/**
	 * Add to queue
	 * 1 param: Task
	 * 2 param Priority (true is prior false is normal)
	 */
	public void addtoQueue(Task task, boolean isPriority) {
		addtoQueue(task, isPriority, 0);
	}

	/**
	 * Add to queue prio: normal
	 * 1 param: Task
	 * 2 param normal queue process level
	 * Default levels: 0, 1 - higher level will be slower
	 */
	public void addtoQueue(Task task, int level) {
		addtoQueue(task, false, level);
	}

	/**
	 * Add to queue prio: normal, level 0
	 * 1 param: Task
	 */
	public void addtoQueue(Task task) {
		addtoQueue(task, false, 0);
	}

	/**
	 * Add to queue prio: normal
	 * 1 param: Task
	 * 2 param normal q process level
	 * 3 param queue process level 
	 * Default levels: 0, 1 - higher level will be slower
	 */
	public void addtoQueue(Task task, boolean isPriority, int level) {
		ConcurrentLinkedQueue<Task> clq = null;
		if (isPriority) {

			clq = queues.get(0).get(level);
		} else {

			clq = queues.get(1).get(level);
		}
		clq.add(task);

	}

}
