package com.xel.apigateway.local.logger;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.xel.apigateway.local.util.CurrentDate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@PropertySource(name = "CustomLogger", value = "logger.properties") // Custom config
public class CustomLogger {
	
	private static String loggerControllerLogsDir;
	static ExecutorService executor; 
	private static LogWriter writer;

	@Value("${logger.controller.logs.dir}")
	public void setLoggerControllerLogsDir(String loggerControllerLogsDir) {
		this.loggerControllerLogsDir = loggerControllerLogsDir;
	}

	public void launch() {
		if (executor == null) {
			executor = Executors.newCachedThreadPool();
		}
		if (writer == null) {
			writer = new LogWriter(detectPaths());
		}
	}
	
	private Map<Class, Path> detectPaths() {
		
		Map<Class, Path> paths = new HashMap<Class, Path>();
		List<Class> controllers = detectControllers();
		for (Class controller : controllers) {
			if (CustomLogger.loggerControllerLogsDir.contains(",")) {
				String[] pathParts = CustomLogger.loggerControllerLogsDir.split(",");
				IntStream.range(0, pathParts.length).parallel().forEach(i -> {
					StringBuilder pathBuilder = new StringBuilder();
							pathBuilder.append(pathParts[i].trim())
									.append(controller.getSimpleName())
									.append(".txt");
					paths.put(controller, Paths.get(pathBuilder.toString()));
					System.out.println("Path: "+pathBuilder.toString());
				});
			} else {
				StringBuilder pathBuilder = new StringBuilder();
				pathBuilder.append(CustomLogger.loggerControllerLogsDir.trim())
						.append(controller.getSimpleName())
						.append(".txt");
				paths.put(controller, Paths.get(pathBuilder.toString()));
				System.out.println("Path: "+pathBuilder.toString());
			}
		}
		return paths;
	}

	private List<Class> detectControllers() {

		List<Class> controllers = new LinkedList<>();
		try {
			String packageName = "com.xel.apigateway.controllers";
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			assert classLoader != null;
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			ArrayList<Class> classes = new ArrayList<Class>();
			for (File directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}

			Class[] c = classes.toArray(new Class[classes.size()]);
			System.out.println("C size: "+c.length);
			IntStream.range(0, c.length).parallel().forEach(i -> {
				controllers.add(c[i]);
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		return controllers;

	}

	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public void log(Class c, String message) {
		builder(c, message);

	}

	private void builder(Class c, String message) {
		CurrentDate time = new CurrentDate();
		StringBuilder builder = new StringBuilder();

		builder.append("TIME: ").append(time.getCurrentTime()).append(" | Epoch: ").append(time.getCurrentTime().toEpochSecond(ZoneOffset.UTC)).append(" | Class: ").append(c).append(" | Message: ")
				.append(message);
		writer.addLogMessage(new LoggerBean(c, builder.toString()));
	}


}
