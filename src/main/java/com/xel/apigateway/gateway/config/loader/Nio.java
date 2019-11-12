package com.xel.apigateway.gateway.config.loader;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 
 * @author xeldawe
 *
 */
public class Nio {

	private List<Bean> lines = new LinkedList<>();

	void read() {
		Path path = Paths.get(new File("").getAbsoluteFile().toString()+"/config");
		AsynchronousFileChannel fileChannel = null;
		try {
			fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteBuffer buffer = null;
		try {
			buffer = ByteBuffer.allocate((int) fileChannel.size());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Future<?> result = fileChannel.read(buffer, 0);
		while (!result.isDone()) {
			// ??? :)
		}

		buffer.flip();

		Bean bean = null;
		StringBuilder sb = new StringBuilder();
		int localIndex = 0;
		String tmp = null;
		while (buffer.hasRemaining()) {
			byte c = buffer.get();

			if (c != 10)
				sb.append((char) c);
			else {
				switch (localIndex) {
				case 0:
					bean = new Bean();
					tmp = sb.toString();
					tmp = tmp.substring(tmp.indexOf(":")+1);
					bean.setDbHost(tmp);
					break;
				case 1:
					tmp = sb.toString();
					tmp = tmp.substring(tmp.indexOf(":") + 1);
					bean.setDbUsername(tmp);
					break;
				case 2:
					tmp = sb.toString();
					tmp = tmp.substring(tmp.indexOf(":") + 1);
					bean.setDbPassword(tmp);
					break;
				case 3:
					tmp = sb.toString();
					tmp = tmp.substring(tmp.indexOf(":") + 1);
					bean.setSma(tmp);
					lines.add(bean);
					System.out.println(bean);
					localIndex = -1;
					break;

				default:
					break;
				}
				sb = new StringBuilder();
				localIndex++;
			}

		}
		buffer.clear();
		try {
			fileChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public List<Bean> getLines() {
		return lines;
	}

}
