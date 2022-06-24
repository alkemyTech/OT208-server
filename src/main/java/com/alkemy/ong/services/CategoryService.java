package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.category.EntryCategoryDto;
import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.models.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService extends BasicService<CategoryEntity, String> {

    CategoryDetailDto saveCategory(EntryCategoryDto entryCategoryDto, MultipartFile image);

    CategoryDetailDto saveCategory(EntryCategoryDto entryCategoryDto);

    CategoryDetailDto getDetailDto(String id);

    Page<CategoryBasicDto> getCategories(Pageable pageable);

    CategoryDetailDto editCategory(String id, EntryCategoryDto entryCategoryDto, MultipartFile image);
}
