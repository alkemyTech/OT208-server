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

import com.alkemy.ong.dto.request.news.EntryNewsDto;
import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.mappers.NewsMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

	private final NewsService newsService;
	private final NewsMapper newsMapper;
	private final AWSS3Service aWSS3Service;

	@PutMapping("/{id}")
	public ResponseEntity<BasicNewsEntity> editNews(
			@PathVariable String id, 
			@Valid @RequestPart(name = "news", required = true) EntryNewsDto entryNewsDto,
			Errors errors,
			@RequestPart(name = "imageNews", required = false) MultipartFile image) {
		if (errors.hasErrors()) {
			throw new RuntimeException("Has error in: "  + errors.getFieldError().getField() + ". " 
										+ errors.getFieldError().getDefaultMessage());
		}
		Optional<NewsEntity> newsEntityOp = newsService.findById(id);
		
		if (!newsEntityOp.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		NewsEntity newsEntity = newsEntityOp.get();
		newsEntity = newsMapper.mapperEntryNewsDtoToNewsEntity(entryNewsDto, newsEntity);

		if (!image.isEmpty()) {
			String pathImage = aWSS3Service.uploadFile(image);
			newsEntity.setImage(pathImage);
		}
		newsService.edit(newsEntity);
		
		return ResponseEntity.ok(newsMapper.mapperNewsEntityToBasicNewsDto(newsEntity));
	}
}
