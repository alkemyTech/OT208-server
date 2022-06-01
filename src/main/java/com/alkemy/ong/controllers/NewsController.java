package com.alkemy.ong.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.services.AWSS3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

	private final NewsService newsService;
	private final NewsMapper newsMapper;
	private final AWSS3Service aWSS3Service;

	@PutMapping("/{id}")
	public ResponseEntity<NewsEntity> editNews(
			@PathVariable String id, 
			@Valid @RequestPart(name = "news", required = true) EditNewsDto editNewsDto,
			Errors errors,
			@RequestPart(name = "image", required = false) MultipartFile image) {
		if (errors.hasErrors()) {
			throw new RuntimeException("");
		}
		Optional<NewsEntity> newsEntityOp = newsService.findById(id);
		
		if (!newsEntityOp.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		NewsEntity newsEntity = newsEntityOp.get();
		newsEntity = newsMapper.mapperEditNewsDtoToNewsEntity(editNewsDto, newsEntity);

		if (!image.isEmpty()) {
			String pathImage = aWSS3Service.uploadFile(image);
			newsEntity.setImage(pathImage);
		}

		return ResponseEntity.ok(newsService.edit(newsEntity));
	}
}
