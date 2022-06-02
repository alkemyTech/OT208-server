package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.models.CategoryEntity;
import com.alkemy.ong.repositories.ICategoryRepository;
import com.alkemy.ong.services.CategoryService;
import com.alkemy.ong.services.mappers.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl extends BasicServiceImpl<CategoryEntity,String, ICategoryRepository> implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(ICategoryRepository iCategoryRepository,CategoryMapper categoryMapper){
        super(iCategoryRepository);
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryBasicDto> getCategories() {
        List<CategoryEntity> categoryEntityList = findAll();
        return categoryMapper.listCategories(categoryEntityList);
    }

    @Override
    public Optional<CategoryDetailDto> getCategoryById(String id) {

        Optional<CategoryEntity>categoryEntity = findById(id);
        if (categoryEntity.isPresent()){
            Optional<CategoryDetailDto>categoryDetailDto = Optional.of(categoryMapper.categoryDetail(categoryEntity.get()));
            return categoryDetailDto;
        }
        return Optional.empty();
    }
}
