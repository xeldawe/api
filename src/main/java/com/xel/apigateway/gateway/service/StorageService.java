package com.xel.apigateway.gateway.service;


import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@PropertySource(name = "default", value = "application.properties")
public class StorageService {

	private static SecureRandom random = new SecureRandom();
	
	private static String imagesDir;

	@Value("${images.dir}")
	public void setImagesDir(String imagesDir) {
		StorageService.imagesDir = imagesDir;
	}
	
	public String saveFile(MultipartFile multipartFile) {
		String name = multipartFile.getOriginalFilename();
		 String newName = "incorrect file";
		if(name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")) {
		
		 newName = uniqueIdGenerator()+ multipartFile.getOriginalFilename();
		 
		 String destination = imagesDir + newName; 
		 File file = new File(destination);
		 try {
			multipartFile.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			return "";
		}
		}
		 return newName;
	}
	
	public String uniqueIdGenerator() {
		StringBuilder sb = new StringBuilder();
		IntStream.range(0, 16).parallel().forEachOrdered(i -> {
		     sb.append(Long.toHexString(random.nextInt(16)));
		});
		return (sb.toString()+"_");
	}

}