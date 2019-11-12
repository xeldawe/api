package com.xel.apigateway.local.server.nio;

public class Server {

	private int serverId;
	private String host;
	private int port;
	private boolean isAsync;
	private boolean isOpen;
	private boolean isListening;
	@SuppressWarnings("rawtypes")
	private Class serverType;
	private int complatedTasks;
	private int waitingTasks;
	private Object server;
	private Thread serverThread;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isAsync() {
		return isAsync;
	}

	public void setAsync(boolean isAsync) {
		this.isAsync = isAsync;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public boolean isListening() {
		return isListening;
	}

	public void setListening(boolean isListening) {
		this.isListening = isListening;
	}

	public Class getServerType() {
		return serverType;
	}

	public void setServerType(Class serverType) {
		this.serverType = serverType;
	}

	public int getComplatedTasks() {
		return complatedTasks;
	}

	public void setComplatedTasks(int complatedTasks) {
		this.complatedTasks = complatedTasks;
	}

	public int getWaitingTasks() {
		return waitingTasks;
	}

	public void setWaitingTasks(int waitingTasks) {
		this.waitingTasks = waitingTasks;
	}

	public Object getServer() {
		return server;
	}

	public void setServer(Object server) {
		this.server = server;
	}

	public Thread getServerThread() {
		return serverThread;
	}

	public void setServerThread(Thread serverThread) {
		this.serverThread = serverThread;
	}

}
