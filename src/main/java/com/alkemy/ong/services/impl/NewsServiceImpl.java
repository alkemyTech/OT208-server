package com.alkemy.ong.services.impl;

import org.springframework.stereotype.Service;

import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.repositories.INewsRepository;
import com.alkemy.ong.services.NewsService;

@Service
public class NewsServiceImpl extends BasicServiceImpl<NewsEntity, String, INewsRepository> implements NewsService{

	public NewsServiceImpl(INewsRepository repository) {
		super(repository);
	}

}
