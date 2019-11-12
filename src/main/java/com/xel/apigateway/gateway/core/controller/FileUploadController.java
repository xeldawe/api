package com.xel.apigateway.gateway.core.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xel.apigateway.gateway.service.StorageService;

/**
 * 
 * @author xeldawe
 * Direct upload
 *
 */
@Async("storage")
@RestController
@CrossOrigin(origins= { "http://localhost:4200",  "https://xeldawe.com"},  maxAge = 7000)
@RequestMapping("/api/storage/")
public class FileUploadController {

	@Autowired
	private StorageService storageService;

	@RequestMapping(value = { "/uploads", "/uploads/" }, method = RequestMethod.POST)
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) {
		String save = storageService.saveFile(multipartFile);
		JSONObject json = new JSONObject();
		try {
			json.put("uploadedFile", save);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(json.toString(), HttpStatus.OK);
	}

}