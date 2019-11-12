package com.xel.apigateway.local.server.nio;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xel.apigateway.local.server.nio.ServerConfigEnum.SETTINGS;

public class ServerManager {

	public final static ExecutorService executorService = Executors.newCachedThreadPool();
	private static List<Server> servers = new ArrayList<>();

	// DEFAULT CONFIG
	private static int lastDefaultServerPort = -1;
	private static int lastServerId = -1;
	private static int defaultServerStartPort = 40000;
	private static String defaultServerHost = "127.0.0.1";
	@SuppressWarnings("rawtypes")
	private static Class defaultServerType = AsyncDefaultServer.class;
	private static boolean defaultServerIsAsync = true;

	// Add default server
	public synchronized Server addServer() {
		Server server = new Server();
		if (lastServerId == -1) {
			lastServerId = 1;
			server.setServerId(lastServerId);
		} else {
			lastServerId++;
			server.setServerId(lastServerId);
		}
		server.setServerType(defaultServerType);
		server.setAsync(defaultServerIsAsync);
		server.setHost(defaultServerHost);
		if (lastDefaultServerPort == -1) {
			lastDefaultServerPort = defaultServerStartPort;
			server.setPort(lastDefaultServerPort);
		} else {
			lastDefaultServerPort++;
			server.setPort(lastDefaultServerPort);
		}

		try {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Constructor defaultConstructor = defaultServerType.getDeclaredConstructor(EnumMap.class);
			EnumMap<SETTINGS, String> launchConfig = new EnumMap<>(SETTINGS.class);
			launchConfig.put(SETTINGS.HOST, defaultServerHost);
			launchConfig.put(SETTINGS.PORT, defaultServerStartPort+"");
			launchConfig.put(SETTINGS.SERVERID, lastServerId+"");

			try {
				server.setServer(defaultConstructor.newInstance(launchConfig));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			return server;
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	};

	public synchronized Server addServer(String host, int port) {
		String tmpHost = defaultServerHost;
		defaultServerHost = host;
		int tmpPort = defaultServerStartPort;
		defaultServerStartPort = port;

		Server server = addServer();

		// rollback to default
		defaultServerHost = tmpHost;
		defaultServerStartPort = tmpPort;

		return server;
	}

}
