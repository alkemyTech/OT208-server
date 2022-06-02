package com.alkemy.ong.services;

import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.models.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends BasicService<CategoryEntity,String> {

    List<CategoryBasicDto> getCategoriesDto();
    Optional<CategoryDetailDto> getCategoryByIdDto(String id);
}
