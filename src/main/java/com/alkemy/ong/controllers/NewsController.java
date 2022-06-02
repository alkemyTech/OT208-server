package com.alkemy.ong.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.dto.response.news.BasicNewsEntity;
import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.services.NewsService;
import com.alkemy.ong.services.mappers.NewsMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("news")
public class NewsController {

	private final NewsService newsService;
	private final NewsMapper newsMapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<BasicNewsEntity> getNews(@RequestParam String id){
		Optional<NewsEntity> newsEntity = newsService.findById(id);
		
		if(!newsEntity.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(newsMapper.mapperBasicNewsEntityToNewsEntity(newsEntity.get()));
	}
}