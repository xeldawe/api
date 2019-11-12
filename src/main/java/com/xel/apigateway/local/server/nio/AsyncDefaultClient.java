package com.xel.apigateway.local.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncDefaultClient {

	private  AsynchronousSocketChannel client = null;
	private  InetSocketAddress hostAddress = null;
	private  Future<Void> future = null;

	public void startClient() {

		try {
			client = AsynchronousSocketChannel.open();
			hostAddress = new InetSocketAddress("127.0.0.1", 40000);
			future = client.connect(hostAddress);

			this.start();
			String a = "hi";;
			for(int i = 0; i < 10; i++) {
				// System.out.println(sendMessage(" "+i));
				sendMessage(a);
			}

//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//			System.out.println("Message to server:");
//			System.out.println("Message to server:");
//				br.lines().forEachOrdered(line -> {
//					long start = System.currentTimeMillis();
//					String response = null;
//					if (line.length() > 0)
//						response = client.sendMessage(line);
//					else 
//						response = client.sendMessage(" ");
//					
//					System.out.println("Response time: " + (System.currentTimeMillis() - start) + " ms");
//					System.out.println("Response from server: " + response);
//					System.out.println();
//					System.out.println("Response from server: " + response);
//					System.out.println(System.currentTimeMillis() - start + "");
//					System.out.println("Message to server:");
//					System.out.println("Message to server:");
//				});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String sendMessage(String message) {
		byte[] byteMsg = message.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(byteMsg);
		Future<Integer> writeResult = client.write(buffer);

		try {
			writeResult.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		buffer.flip();
		buffer = ByteBuffer.allocate(65535);
		Future<Integer> readResult = client.read(buffer);
		try {
			readResult.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String echo = new String(buffer.array()).trim();
		buffer.clear();
		return echo;
	}

	private void start() {
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public AsynchronousSocketChannel getClient() {
		return client;
	}

	public void setClient(AsynchronousSocketChannel client) {
		this.client = client;
	}

	public InetSocketAddress getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(InetSocketAddress hostAddress) {
		this.hostAddress = hostAddress;
	}

	public Future<Void> getFuture() {
		return future;
	}

	public void setFuture(Future<Void> future) {
		this.future = future;
	}

	@Override
	public String toString() {
		return "AsyncDefaultClient [client=" + client + ", hostAddress=" + hostAddress + ", future=" + future + "]";
	}

}
