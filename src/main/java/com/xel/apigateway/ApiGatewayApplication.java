package com.xel.apigateway;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import com.xel.apigateway.ApiGatewayApplication;
import com.xel.apigateway.gateway.config.loader.Loader;
import com.xel.apigateway.gateway.core.QueueExecturor;
import com.xel.apigateway.local.logger.CustomLogger;


@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = { "com" })
public class ApiGatewayApplication extends SpringBootServletInitializer {

	private static QueueExecturor qe = new QueueExecturor();
	public static CustomLogger cl = new CustomLogger();
	public static com.xel.apigateway.gateway.config.loader.Bean bean = null;

	public static void main(String[] args) {
		
		Loader l = new Loader();
		bean = l.start();
		
		SpringApplication.run(ApiGatewayApplication.class, args);

		ExecutorService es = Executors.newFixedThreadPool(2);
		Runnable task1 = () -> {
			String threadName = Thread.currentThread().getName();
			qe.priorityQrun();
		};
		Runnable task2 = () -> {
			String threadName = Thread.currentThread().getName();
			qe.normalQrun();
		};
//		Runnable task3 = () -> {
//			ServerManager sm = new ServerManager();
//			sm.addServer();
//		};
		es.execute(task1);
		es.execute(task2);
//		es.execute(task3);

		
		cl = new CustomLogger();
		boolean isRunning = false;
		// bruteforce start
		while (!isRunning) {
			try {
				cl.launch();
				isRunning = true;
			} catch (Exception e) {
			}
		}

	}

}
