package com.alkemy.ong.services;

import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.models.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends BasicService<CategoryEntity,String> {

    List<CategoryBasicDto> getCategoriesDto();
    Optional<CategoryDetailDto> getCategoryByIdDto(String id);
    Page<CategoryDetailDto> getCategories(Pageable pageable);
}
