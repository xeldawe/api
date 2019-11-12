package com.xel.apigateway.local.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.xel.apigateway.local.server.nio.ServerConfigEnum.SETTINGS;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AsyncDefaultServer {

	private final EnumMap<SETTINGS, String> launchConfig;
	private int serverId;
	private String host;
	private int port;
	private InetSocketAddress inet;

	private AsynchronousServerSocketChannel server;

	private boolean isListen = false;
	private int byteBufferSize = 65535;

	public AsyncDefaultServer(EnumMap<SETTINGS, String> launchConfig) {
		super();
		System.out.println("Launch server...");
		this.launchConfig = launchConfig;
		init();
	}

	public boolean serverIsListening() {
		return isListen;
	}

	public boolean serverIsOpen() {
		return server.isOpen();
	}

	private void init() {
		try {
			System.out.println("Init server...");
			this.launchConfig.forEach((k, v) -> {
				String currentStr = k.toString().toLowerCase();
				if (currentStr.equals("host")) {
					host = launchConfig.get(SETTINGS.HOST);
				} else if (currentStr.equals("port")) {
					port = Integer.valueOf(launchConfig.get(SETTINGS.PORT));
				} else if (currentStr.equals("serverid")) {
					serverId = Integer.valueOf(launchConfig.get(SETTINGS.SERVERID));
				}
			});
			createListener();
			startListening();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				server.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void createListener() throws IOException {
		if (inet != null) {
			server = AsynchronousServerSocketChannel.open().bind(inet);
		} else {
			server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(host, port));
		}
	}

	public void startListening() {

		Runnable serverThread = () -> {
			System.out.println("Listening server on port: "+port);
			System.out.println("Listening server...");
			isListen = true;
			// while (isListen) {
			server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
				public void completed(AsynchronousSocketChannel ch, Void att) {

					if (ch.isOpen()) {
						server.accept(null, this);
					}
					ReadWriteHandler handler = new ReadWriteHandler();
					handler.handleChannel(ch);
				}

				@Override
				public void failed(Throwable exc, Void attachment) {
					// TODO Auto-generated method stub
				}
			});
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// }
		};
		ServerManager.executorService.execute(serverThread);
	}

	public void stopListening() {
		isListen = false;
	}

	public void close() {
		isListen = false;
	}

	private class ReadWriteHandler implements CompletionHandler<Integer, Map<String, Object>> {
		
		private AsynchronousSocketChannel client;

		private void handleChannel(AsynchronousSocketChannel server) {
			client = server;
			if ((server != null) && (server.isOpen())) {

				ByteBuffer buffer = ByteBuffer.allocate(byteBufferSize);
				buffer.clear();
				Map<String, Object> readInfo = new HashMap<>();
				readInfo.put("action", "read");
				readInfo.put("buffer", buffer);

				server.read(buffer, readInfo, this);
			}
		}

		@Override
		public void completed(Integer result, Map<String, Object> attachment) {
			Map<String, Object> actionInfo = attachment;
			String action = (String) actionInfo.get("action");
			if ("read".equals(action)) {

				byte[] message = new String("Gotcha :P | Server input: ").getBytes();
				ByteBuffer text = ByteBuffer.wrap(message);
				ByteBuffer input = (ByteBuffer) actionInfo.get("buffer");
				input.flip();
				ByteBuffer buffer = ByteBuffer.allocate(text.capacity() + input.capacity());
				buffer.put(text);
				buffer.put(input);
				buffer.flip();
				actionInfo.put("buffer", buffer);
				actionInfo.put("action", "write");
				client.write(buffer, actionInfo, this);

			} else if ("write".equals(action)) {
				ByteBuffer buffer = ByteBuffer.allocate(byteBufferSize);
				actionInfo.put("action", "read");
				actionInfo.put("buffer", buffer);
				client.read(buffer, actionInfo, this);
			}
		}

		@Override
		public void failed(Throwable exc, Map<String, Object> attachment) {

		}

	}

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

	public InetSocketAddress getInet() {
		return inet;
	}

	public void setInet(InetSocketAddress inet) {
		this.inet = inet;
	}

	public AsynchronousServerSocketChannel getServer() {
		return server;
	}

	public void setServer(AsynchronousServerSocketChannel server) {
		this.server = server;
	}

	public boolean isListen() {
		return isListen;
	}

	public void setListen(boolean isListen) {
		this.isListen = isListen;
	}

	public int getByteBufferSize() {
		return byteBufferSize;
	}

	public void setByteBufferSize(int byteBufferSize) {
		this.byteBufferSize = byteBufferSize;
	}

//	public static org.slf4j.Logger getLog() {
//		return log;
//	}

	public EnumMap<SETTINGS, String> getLaunchConfig() {
		return launchConfig;
	}

}
