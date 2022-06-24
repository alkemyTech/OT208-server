package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.category.EntryCategoryDto;
import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.models.CategoryEntity;
import com.alkemy.ong.repositories.ICategoryRepository;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.CategoryService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl extends BasicServiceImpl<CategoryEntity, String, ICategoryRepository> implements CategoryService {

    private final AWSS3Service awss3Service;

    public CategoryServiceImpl(ICategoryRepository iCategoryRepository, AWSS3Service awss3Service) {
        super(iCategoryRepository);
        this.awss3Service = awss3Service;
    }

    @Override
    public CategoryDetailDto saveCategory(EntryCategoryDto entryCategoryDto, MultipartFile image) {

        CategoryEntity categoryEntity = ObjectMapperUtils.map(entryCategoryDto, CategoryEntity.class);

        categoryEntity.setImage(awss3Service.uploadFile(image));

        repository.save(categoryEntity);

        return ObjectMapperUtils.map(categoryEntity, CategoryDetailDto.class);
    }

    @Override
    public CategoryDetailDto saveCategory(EntryCategoryDto entryCategoryDto) {

        CategoryEntity categoryEntity = ObjectMapperUtils.map(entryCategoryDto, CategoryEntity.class);

        repository.save(categoryEntity);

        return ObjectMapperUtils.map(categoryEntity, CategoryDetailDto.class);
    }

    @Override
    public CategoryDetailDto getDetailDto(String id) {

        Optional<CategoryEntity> categoryEntityop = repository.findById(id);

        if (categoryEntityop.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not Found");
        } else {
            return ObjectMapperUtils.map(categoryEntityop.get(), CategoryDetailDto.class);
        }

    }

    @Override
    public Page<CategoryBasicDto> getCategories(Pageable pageable) {

        List<CategoryEntity> categoryEntities = repository.findAll();
        List<CategoryBasicDto> response;

        if (!categoryEntities.isEmpty()) {
            response = ObjectMapperUtils.mapAll(categoryEntities, CategoryBasicDto.class);

            final int star = (int) pageable.getOffset();
            final int end = Math.min((star + pageable.getPageSize()), response.size());

            return new PageImpl<>(response.subList(star, end), pageable, response.size());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's not Category");
        }

    }

    @Override
    public CategoryDetailDto editCategory(String id, EntryCategoryDto entryCategoryDto, MultipartFile image) {
        Optional<CategoryEntity> op = repository.findById(id);
        if (op.isPresent()) {
            CategoryEntity categoryEntity = ObjectMapperUtils.map(entryCategoryDto, op.get());
            if (!image.isEmpty()) {
                categoryEntity.setImage(awss3Service.uploadFile(image));
            }
            categoryEntity = this.edit(categoryEntity);
            return ObjectMapperUtils.map(categoryEntity, CategoryDetailDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not Found");
        }

    }
}
