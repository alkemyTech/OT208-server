package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.response.news.BasicNewsDto;
import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.repositories.INewsRepository;
import com.alkemy.ong.services.NewsService;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NewsServiceImpl extends BasicServiceImpl<NewsEntity, String, INewsRepository> implements NewsService {

	public NewsServiceImpl(INewsRepository repository) {
		super(repository);
	}

	@Override
	public Page<BasicNewsDto> getNews(Pageable pageable) {
		List<NewsEntity> newsEntities = repository.findAll();
		List<BasicNewsDto> response;

		if (!newsEntities.isEmpty()) {
			response = ObjectMapperUtils.mapAll(newsEntities, BasicNewsDto.class);

			final int start = (int) pageable.getOffset();
			final int end = Math.min((start + pageable.getPageSize()), response.size());

			return new PageImpl<>(response.subList(start, end), pageable, response.size());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("There's no news"));
		}
	}
}
