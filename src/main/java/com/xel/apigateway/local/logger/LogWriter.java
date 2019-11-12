package com.xel.apigateway.local.logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogWriter {

	public static Queue<LoggerBean> queue;
	private static Map<Class, Path> paths;
	private static int frequencyInMillis = 5000; // default 5s
	private static int bufferSize = 1024; 
	private static boolean dynamicBufferSyze = true; 
	private static Map<Class, AsynchronousFileChannel> fileChannels;
	private static boolean isRunning;

	public LogWriter(Map<Class, Path> paths) {
		super();
		if (LogWriter.queue == null) {
			LogWriter.queue = new ConcurrentLinkedQueue();
			isRunning = false;
		}
		if (LogWriter.paths == null) {
			LogWriter.paths = paths;
		} else {
			LogWriter.paths.putAll(paths);
		}
		if (!isRunning) {
			isRunning = true;
			CustomLogger.executor.execute(writer);
		}
		if(fileChannels == null) {
			fileChannels = new HashMap<Class, AsynchronousFileChannel>();
		}
	}

	public void start() {
		LoggerBean loggerBean = null;
		try {
			while (true) {
				while (!queue.isEmpty()) {
					loggerBean = queue.poll();
					if (loggerBean != null) {
						writeOut(loggerBean.getMessage(), loggerBean.getC());
					}
					// for test:
//					queue.add(loggerBean);
					//
				}
				Thread.sleep(frequencyInMillis);
			}
		} catch (Exception e) {
			start();
		}
	}

	public void writeOut(String message, Class c) {
		Path path = paths.get(c);
		AsynchronousFileChannel fileChannel = null;
		try {
			fileChannel = fileChannels.get(c);
		} catch (Exception e) {
			//? :D
		}
		if (fileChannel == null) {
			try {
				fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
				fileChannels.put(c, fileChannel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {

			byte[] m = message.getBytes();
			byte[] l = System.lineSeparator().getBytes();
			
			ByteBuffer buffer = null;
			if(dynamicBufferSyze) {
			buffer = ByteBuffer.allocate(m.length+l.length);
			}else {
				buffer = ByteBuffer.allocate(bufferSize);
			}
			// long position = 0;
			buffer.put(m);
			buffer.put(l);
			buffer.flip();

			fileChannel.write(buffer, fileChannel.size(), buffer, new CompletionHandler<Integer, ByteBuffer>() {

				@Override
				public void completed(Integer result, ByteBuffer attachment) {
//		        System.out.println("bytes written: " + result);
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					// System.out.println("Write failed");
					exc.printStackTrace();
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static int getFrequencyInMillis() {
		return frequencyInMillis;
	}

	public static void setFrequencyInMillis(int frequencyInMillis) {
		LogWriter.frequencyInMillis = frequencyInMillis;
	}

	public void addLogMessage(LoggerBean loggerBean) {
		queue.add(loggerBean);
	}

	Runnable writer = () -> {
		this.start();
	};

}
