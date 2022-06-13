package com.alkemy.ong.services;

import com.alkemy.ong.dto.response.news.BasicNewsDto;
import com.alkemy.ong.models.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsService extends BasicService<NewsEntity, String> {

    Page<BasicNewsDto> getNews(Pageable pageable);
}
