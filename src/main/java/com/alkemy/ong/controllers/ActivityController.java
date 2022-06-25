package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.activity.EntryActivityDto;
import com.alkemy.ong.dto.response.activity.BasicActivityDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 1024 * 1024 * 15)
public class ActivityController {

    private final ActivityService service;
    private final AWSS3Service awss3Service;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BasicActivityDto> create(@Valid @RequestPart(name = "activity") EntryActivityDto entryActivityDto, Errors errors, @RequestPart MultipartFile file) {

        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        if (file.isEmpty()) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The image cannot be empty");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveActivity(entryActivityDto, awss3Service.uploadFile(file)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicActivityDto> update(
    	@PathVariable String id,
    	@Valid @RequestPart(name = "activity") EntryActivityDto dto,
    	Errors errors, @RequestPart(required = true) MultipartFile file) {
    	
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.updateActivity(dto, file, id));    
    }
}
