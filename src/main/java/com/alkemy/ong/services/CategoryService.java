package com.alkemy.ong.services;

import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryBasicDto> getCategories();
    Optional<CategoryDetailDto> getCategoryById(String id);
}
