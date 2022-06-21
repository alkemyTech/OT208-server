package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.category.EntryCategoryDto;
import com.alkemy.ong.dto.response.activity.BasicActivityDto;
import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.models.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CategoryService extends BasicService<CategoryEntity,String> {

    CategoryDetailDto saveCategory(EntryCategoryDto entryCategoryDto, MultipartFile image);
    CategoryDetailDto saveCategory(EntryCategoryDto entryCategoryDto);
    List<CategoryBasicDto> getCategoriesDto();
    CategoryDetailDto getCategoryByIdDto(String id);
    Page<CategoryDetailDto> getCategories(Pageable pageable);
    CategoryDetailDto editCategory(String id,EntryCategoryDto entryCategoryDto,MultipartFile image);
}
