package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.models.CategoryEntity;
import com.alkemy.ong.repositories.ICategoryRepository;
import com.alkemy.ong.services.CategoryService;
import com.alkemy.ong.services.mappers.CategoryMapper;
import com.alkemy.ong.services.mappers.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public List<CategoryBasicDto> getCategoriesDto() {
        return categoryMapper.listCategories(findAll());
    }

    @Override
    public Optional<CategoryDetailDto> getCategoryByIdDto(String id) {
        if (existById(id)){
            return   Optional.of(categoryMapper.categoryDetail(findById(id).get()));

        }
        return Optional.empty();
    }

    @Override
    public Page<CategoryDetailDto> getCategories(Pageable pageable) {

        List<CategoryEntity> categoryEntities = repository.findAll();
        List<CategoryDetailDto> response;

        if(!categoryEntities.isEmpty()){
            response = ObjectMapperUtils.mapAll(categoryEntities,CategoryDetailDto.class);

            final int star = (int)pageable.getOffset();
            final int end = Math.min((star+pageable.getPageSize()),response.size());

            return new PageImpl<>(response.subList(star,end),pageable,response.size());
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"There's not Category");
        }

    }
}
