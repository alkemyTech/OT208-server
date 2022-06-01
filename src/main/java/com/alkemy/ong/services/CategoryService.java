package com.alkemy.ong.services;

import com.alkemy.ong.dto.response.category.CategoryBasicDto;

import java.util.List;

public interface CategoryService {

    List<CategoryBasicDto> getCategories();
}
