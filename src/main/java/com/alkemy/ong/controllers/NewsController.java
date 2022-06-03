package com.alkemy.ong.controllers;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alkemy.ong.dto.response.news.EntryNewsDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.mappers.NewsMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@MultipartConfig(maxFileSize = 1024*1024*10)
public class NewsController {

	private final NewsService newsService;
	private final NewsMapper newsMapper;
	private final AWSS3Service awss3Service;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BasicNewsDto> createNews(
			@Valid @RequestPart("news") EntryNewsDto entryNewsDto,
			Errors errors,
			@RequestPart("newsImage") MultipartFile image){
		
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getFieldErrors());
		}
		NewsEntity newsEntity  = newsMapper.mapperEntryNewsDtoToNewsEntity(entryNewsDto);
		newsEntity.setType("news");
		
		if (!image.isEmpty()) {
			String pathImage = aWSS3Service.uploadFile(image);
			newsEntity.setImage(pathImage);
		}
		newsService.save(newsEntity);
		
		return ResponseEntity.ok(newsMapper.mapperNewsEntityToBasicNewsDto(newsEntity));
	}
}
	
