package com.alkemy.ong.controllers;

import java.util.Optional;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alkemy.ong.dto.request.news.EntryNewsDto;
import com.alkemy.ong.dto.response.news.BasicNewsDto;
import com.alkemy.ong.exeptions.CategoryNotExistException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.CategoryService;
import com.alkemy.ong.services.NewsService;
import com.alkemy.ong.services.mappers.NewsMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@MultipartConfig(maxFileSize = 1024*1024*15)
public class NewsController {

	private final NewsService newsService;
	private final NewsMapper newsMapper;
	private final AWSS3Service awss3Service;
	private final CategoryService categoryService;
	
	@GetMapping("/{id}")
	public ResponseEntity<BasicNewsDto> getNews(@PathVariable String id){
		Optional<NewsEntity> newsEntity = newsService.findById(id);
		
		if(!newsEntity.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(newsMapper.entityToBasicNewsDto(newsEntity.get()));
	}
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<BasicNewsDto> createNews(
			@Valid @RequestPart(name = "news", required = true) EntryNewsDto entryNewsDto,
			Errors errors,
			@RequestPart(name = "newsImage", required = true) MultipartFile image){
		
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getFieldErrors());
		}
		if (!categoryService.existById(entryNewsDto.getCategoryIdId())) {
			throw new CategoryNotExistException(entryNewsDto.getCategoryIdId());
		}
		
		NewsEntity newsEntity  = newsMapper.entryNewsDtoToEntity(entryNewsDto);
		newsEntity.setType("news");
		
		if (!image.isEmpty()) {
			String pathImage = awss3Service.uploadFile(image);
			newsEntity.setImage(pathImage);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(newsMapper.entityToBasicNewsDto(newsService.save(newsEntity)));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<BasicNewsDto> editNews(
			@PathVariable String id, 
			@Valid @RequestPart(name = "news", required = true) EntryNewsDto entryNewsDto,
			Errors errors,
			@RequestPart(name = "newsImage", required = false) MultipartFile image) {
		
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getFieldErrors());
		}
		if (!categoryService.existById(entryNewsDto.getCategoryIdId())) {
			throw new CategoryNotExistException(entryNewsDto.getCategoryIdId());
		}
		Optional<NewsEntity> newsEntityOp = newsService.findById(id);
		
		if (!newsEntityOp.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		NewsEntity newsEntity = newsEntityOp.get();
		newsEntity = newsMapper.entryNewsDtoToEntity(entryNewsDto, newsEntity);
		newsEntity.setCategoryId(categoryService.findById(newsEntity.getCategoryId().getId()).get());

		if (!image.isEmpty()) {
			String pathImage = awss3Service.uploadFile(image);
			newsEntity.setImage(pathImage);
		}
		
		return ResponseEntity.ok(newsMapper.entityToBasicNewsDto(newsService.edit(newsEntity)));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<BasicNewsDto> deleteNews(@PathVariable String id){
		Optional<NewsEntity> newsEntity = newsService.findById(id);
	
		if(!newsEntity.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		newsService.save(newsEntity.get());
	
		return ResponseEntity.noContent().build();
	}
}
