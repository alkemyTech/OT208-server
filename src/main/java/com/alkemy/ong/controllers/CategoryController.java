package com.alkemy.ong.controllers;


import com.alkemy.ong.dto.request.category.EntryCategoryDto;
import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.CategoryEntity;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.CategoryService;
import com.alkemy.ong.services.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 1024*1024*15)
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final AWSS3Service awss3Service;

    @PostMapping
    public ResponseEntity<CategoryDetailDto>createCategory(
            @Valid @RequestPart(name = "name",required = true) EntryCategoryDto entryCategoryDto,
            Errors errors, @RequestPart(name = "img",required = true) MultipartFile image){
        if (errors.hasErrors()){
            throw new ValidationException(errors.getFieldErrors());
        }
        CategoryEntity categoryEntity = categoryMapper.entryCategoryDtoToEntity(entryCategoryDto);
        if(!image.isEmpty()){
            String pathImage = awss3Service.uploadFile(image);
            categoryEntity.setImage(pathImage);
        }
        categoryService.save(categoryEntity);

        return ResponseEntity.ok(categoryMapper.categoryDetail(categoryEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDetailDto>editCategory(
            @PathVariable String id,
            @Valid @RequestPart(value = "name",required = true) EntryCategoryDto entryCategoryDto,
            Errors errors, @RequestPart(value = "img",required = false) MultipartFile image){
        if (errors.hasErrors()){
            throw new ValidationException(errors.getFieldErrors());
        }
        Optional<CategoryEntity> categoryEntityoOp = categoryService.findById(id);
        if(!categoryEntityoOp.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        CategoryEntity categoryEntity = categoryEntityoOp.get();
        categoryEntity = categoryMapper.entryCategoryDtoToEntityEdit(entryCategoryDto,categoryEntity);

        if(!image.isEmpty()){
            String pathImage = awss3Service.uploadFile(image);
            categoryEntity.setImage(pathImage);
        }
        categoryService.edit(categoryEntity);

        return ResponseEntity.ok(categoryMapper.categoryDetail(categoryEntity));
    }


    @GetMapping
    public ResponseEntity<List<CategoryBasicDto>> getCategories(){
        List<CategoryBasicDto> categoryBasicDtos = categoryService.getCategoriesDto();
        return ResponseEntity.ok(categoryBasicDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailDto>getCategory(@PathVariable final String id){

        Optional<CategoryDetailDto>categoryDetailDto= categoryService.getCategoryByIdDto(id);
        if(categoryDetailDto.isPresent()){
            return ResponseEntity.ok(categoryDetailDto.get());
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryEntity> deleteCategory(@PathVariable String id){
        Optional<CategoryEntity> categoryEntity = categoryService.findById(id);
        if(categoryEntity.isPresent()){
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}
