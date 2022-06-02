package com.alkemy.ong.controllers;


import com.alkemy.ong.dto.response.category.CategoryBasicDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryBasicDto>> getCategories(){
        List<CategoryBasicDto> categoryBasicDtos = categoryService.getCategories();
        return ResponseEntity.ok(categoryBasicDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailDto>getCategory(@PathVariable final String id){

        Optional<CategoryDetailDto>categoryDetailDto= categoryService.getCategoryById(id);
        if(categoryDetailDto.isPresent()){
            return ResponseEntity.ok(categoryDetailDto.get());
        }else {
            return ResponseEntity.notFound().build();
        }

    }
}
