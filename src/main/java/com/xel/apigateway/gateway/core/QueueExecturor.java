package com.xel.apigateway.gateway.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.xel.apigateway.gateway.bean.Response;

/**
 * 
 * @author xeldawe
 *
 */
public class QueueExecturor {

	//Normal queues
	private List<ConcurrentLinkedQueue<Task>> normal = null;
	//Priority queues
	private List<ConcurrentLinkedQueue<Task>> priority = null;
	//All executed requests
	private static Map<String, Response<?>> executedTasks = new HashMap<String, Response<?>>();

	/**
	 * Init queues if does not exist
	 */
	public QueueExecturor() {
		super();
		if (normal == null || priority == null) {
			System.out.println("Queues init");
			init();
		}
	}

	/**
	 * Init normal and priority queues
	 */
	private void init() {
		System.out.println("Queues init2");
		QueueService qs = new QueueService();
		normal = QueueService.queues.get(1);
		priority = QueueService.queues.get(0);
	}

	/**
	 * Refres normal and priority queues
	 */
	private void refresh() {
		normal = QueueService.queues.get(1);
		priority = QueueService.queues.get(0);
	}

	//Priority queue thread
	Runnable priorityQ = () -> {
		while (true) {
			for (ConcurrentLinkedQueue<Task> p : priority) {
				while (!p.isEmpty()) {
					TaskExecutor te = new TaskExecutor();
					System.out.println(p.size());
					Task currentTask = p.remove();
					executedTasks.put(currentTask.getTaskId(), te.execute(currentTask)); //Execute
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refresh();
		}
	};

	//Normal queue thread
	Runnable normalQ = () -> {
		while (true) {

			for (ConcurrentLinkedQueue<Task> n : normal) {
				while (!n.isEmpty()) {
					TaskExecutor te = new TaskExecutor();
					Task currentTask = n.remove();
					executedTasks.put(currentTask.getTaskId(), te.execute(currentTask)); //Execute
				}
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refresh();
		}
	};

	/**
	 * Start priority queue process
	 */
	public void priorityQrun() {
		System.out.println("priorityQ start");
		priorityQ.run();
	}

	/**
	 * Start normal queue process
	 */
	public void normalQrun() {
		System.out.println("normalQ start");
		normalQ.run();
	}

	/**
	 * Find executed Task(Request) by unique id. If found will return Response. If Task(Request) is not executed currently return null.
	 * @param id
	 * @return Response
	 */
	public Response<?> getById(String id) {
		Map<String, Response<?>> tmp = new LinkedHashMap<>();
		if (executedTasks!=null && !executedTasks.isEmpty()) {
			tmp.putAll(executedTasks);
			for (Map.Entry<String, Response<?>> entry : tmp.entrySet()) {
				if (entry.getKey().equals(id)) {
					Response<?> resp = entry.getValue();
					executedTasks.remove(id);
					return resp;
				}
			}
		}
		return null;
	}
	

}
