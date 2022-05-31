package com.alkemy.ong.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.services.NewsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("news")
public class NewsController {

	private NewsService newsService;
	
	@GetMapping("/{id}")
	public ResponseEntity<NewsEntity> getNews(@RequestParam String id){
		Optional<NewsEntity> newsEntity = newsService.findById(id);
		
		if(!newsEntity.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(newsEntity.get());
	}
}
