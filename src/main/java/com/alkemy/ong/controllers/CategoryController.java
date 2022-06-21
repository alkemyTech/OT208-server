package com.alkemy.ong.controllers;


import com.alkemy.ong.dto.request.category.EntryCategoryDto;
import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.CategoryEntity;
import com.alkemy.ong.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 1024*1024*15)
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<CategoryDetailDto>createCategory(
            @Valid @RequestPart(name = "category",required = true) EntryCategoryDto entryCategoryDto,
            Errors errors, @RequestPart(name = "img",required = false) MultipartFile image){
        if (errors.hasErrors()){
            throw new ValidationException(errors.getFieldErrors());
        }
        if(!image.isEmpty()){

            return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(entryCategoryDto,image));
        }else{
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(entryCategoryDto));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDetailDto>editCategory(
            @PathVariable String id,
            @Valid @RequestPart(value = "category",required = true) EntryCategoryDto entryCategoryDto,
            Errors errors, @RequestPart(value = "img",required = false) MultipartFile image){
        if (errors.hasErrors()){
            throw new ValidationException(errors.getFieldErrors());
        }
            return ResponseEntity.ok().body(categoryService.editCategory(id,entryCategoryDto,image));


    }


    @GetMapping
    public ResponseEntity<List<CategoryBasicDto>> getCategories(){
        try {
            return new ResponseEntity<>(this.categoryService.getCategoriesDto(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailDto>getCategory(@PathVariable final String id){

        return ResponseEntity.ok(categoryService.getCategoryByIdDto(id));

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

    @GetMapping("/list")
    public ResponseEntity<Page<CategoryDetailDto>>getCategory(@PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(categoryService.getCategories(pageable));
    }


}
