package com.alkemy.ong.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.models.NewsEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

	private NewsService newsService;
	
	@DeleteMapping("/{id}")
	public ResponseEntity<BasicNewsEntity> deleteNews(@PathVariable String id){
		Optional<NewsEntity> newsEntity = newsService.findById(id);
		
		if(!newsEntity.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		newsService.save(newsEntity.get());
		
		return ResponseEntity.noContent().build();
	}
}
