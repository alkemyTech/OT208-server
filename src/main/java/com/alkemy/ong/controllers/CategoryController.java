package com.alkemy.ong.controllers;


import java.util.Optional;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alkemy.ong.dto.request.category.EntryCategoryDto;
import com.alkemy.ong.dto.response.category.CategoryDetailDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.CategoryEntity;
import com.alkemy.ong.services.CategoryService;
import com.alkemy.ong.services.mappers.CategoryMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 1024*1024*15)
public class CategoryController {

    private final CategoryService categoryService;


    @Operation(summary = "Endpoint to create a category.",
    		description = "It provides the necessary mechanism to be able to create a new category.",
    		requestBody = @RequestBody)
    
    @ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "Created"),
			@ApiResponse(responseCode = "400", description = "Bad Request", 
					content = @Content(schema = @Schema(implementation = ValidationException.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",
					content = @Content(schema = @Schema()))})
    @PostMapping
    public ResponseEntity<CategoryDetailDto>createCategory(
    		@Valid @RequestPart(name = "name",required = true)@Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = EntryCategoryDto.class))) EntryCategoryDto entryCategoryDto,
            Errors errors, 
            @RequestPart(name = "img",required = true)@Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = MultipartFile.class))) MultipartFile image){
        if (errors.hasErrors()){
            throw new ValidationException(errors.getFieldErrors());
        }
        if(!image.isEmpty()){

            return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(entryCategoryDto,image));
        }else{
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(entryCategoryDto));
        }

    }
    
    @Operation(summary = "Endpoint to edit a category.",
    		description = "It provides the necessary mechanism to be able to edit a category.")
    @ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ok"),
			@ApiResponse(responseCode = "400", description = "Bad Request", 
					content = @Content(schema = @Schema(implementation = ValidationException.class))),
			@ApiResponse(responseCode = "404", description = "Not Found", 
					content = @Content(schema = @Schema())),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",
					content = @Content(schema = @Schema()))})
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDetailDto>editCategory(
    		@Parameter(description = "Id of the category to be edited", example = "528f22c3-1f9c-493f-8334-c70b83b5b885") 
    		@PathVariable String id,
            @Valid @RequestPart(name = "name",required = true) EntryCategoryDto entryCategoryDto,
            Errors errors,
            @RequestPart(name = "img",required = false) MultipartFile image){
        if (errors.hasErrors()){
            throw new ValidationException(errors.getFieldErrors());
        }
            return ResponseEntity.ok().body(categoryService.editCategory(id,entryCategoryDto,image));


    }

        return ResponseEntity.ok(categoryService.getCategoryByIdDto(id));

    }
    
    @Operation(summary = "Endpoint to delete a category.",
    		description = "It provides the necessary mechanism to be able to eliminate a category based on its id.")
    @ApiResponses(value = { 
			@ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema())),
					content = @Content(schema = @Schema())),
			@ApiResponse(responseCode = "500", description = "Internal Server Error",
					content = @Content(schema = @Schema()))})

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryEntity> deleteCategory(
    		@Parameter(description = "Id of the category to delete.", example = "528f22c3-1f9c-493f-8334-c70b83b5b885") 
    		@PathVariable String id){
        Optional<CategoryEntity> categoryEntity = categoryService.findById(id);
        if(categoryEntity.isPresent()){
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Endpoint to list all collated categories",
	description = "It provides the necessary mechanism to be able to list all the existing categories in a collated manner. "
				+ "Per page will list 10 categories.")
    @ApiResponses(value = { 
    		@ApiResponse(responseCode = "200", description = "Ok"),
    		@ApiResponse(responseCode = "404", description = "Not Found", 
    			content = @Content(schema = @Schema())),
    		@ApiResponse(responseCode = "500", description = "Internal Server Error",
				content = @Content(schema = @Schema()))})
    
    @GetMapping()
    public ResponseEntity<Page<CategoryDetailDto>> getCategory(
    		 @Parameter(hidden = true) @PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(categoryService.getCategories(pageable));
    }


}
