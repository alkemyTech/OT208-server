package com.alkemy.ong.controllers;

import com.alkemy.ong.services.AWSS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 1024 * 1024 * 15)
public class UploadFileController {

    private final AWSS3Service awss3Service;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please choose an image file type", HttpStatus.BAD_REQUEST);
        }
        String response = awss3Service.uploadFile(file) + " File was successfully uploaded";
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<String>> listFiles() {
    	List<String> images = awss3Service.getAllObjectsFromS3();
    	if(images.isEmpty()) {
    		return ResponseEntity.notFound().build();
    	}
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<Resource> download(@RequestParam("key") String key) {
        try {
        	InputStreamResource resource = new InputStreamResource(awss3Service.downloadFile(key));
        	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"").body(resource);
        }catch (RuntimeException e) {
        	return ResponseEntity.notFound().build();
		}
    }
}