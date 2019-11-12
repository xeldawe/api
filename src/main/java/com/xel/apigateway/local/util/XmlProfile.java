package com.xel.apigateway.local.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class XmlProfile {

	static public boolean test;

	static public String dir;
	static public String testdir;

	@SuppressWarnings("static-access")
	@Value("${xmlprofile.test}")
	public void setTest(boolean test) {
		this.test = test;
	}

	@SuppressWarnings("static-access")
	@Value("${xmlprofile.test.dir}")
	public void setTestdir(String testdir) {
		this.testdir = testdir;
	}

	@SuppressWarnings("static-access")
	@Value("${xmlprofile.dir}")
	public void setDir(String dir) {
		this.dir = dir;
	}

	public List<String> checkDirectory() {
		@SuppressWarnings("rawtypes")
		List<String> fileNames = new LinkedList();
		if (test) {
			try {
				Files.list(Paths.get(testdir)).filter(Files::isRegularFile).forEach(path -> {
					String pstr = path.toString();
					fileNames.add(pstr.substring(pstr.lastIndexOf("/")+1));
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Files.list(Paths.get(dir)).filter(Files::isRegularFile).forEach(path -> {
					String pstr = path.toString();
					fileNames.add(pstr.substring(pstr.lastIndexOf("/")+1));
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileNames;
	}

}
