package com.alkemy.ong.controllers;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

import com.alkemy.ong.dto.request.testimonial.EntryTestimonialDto;
import com.alkemy.ong.dto.response.testimonial.BasicTestimonialDTo;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.TestimonialsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/testimonials")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 1024 * 1024 * 15)
public class TestimonialsController {

    private final TestimonialsService testimonialsService;

    @Operation(summary = "Endpoint to create a testimonials.",
            description = "It provides the necessary mechanism to be able to create a new testimonials.",
            requestBody = @RequestBody)

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ValidationException.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema()))})
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BasicTestimonialDTo> createTestimonial(
            @Valid @RequestPart(name = "file", required = true) EntryTestimonialDto entryTestimonialDto, Errors errors,
            @RequestPart(name = "img", required = true) MultipartFile file) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(testimonialsService.createTestimonial(entryTestimonialDto));

        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(testimonialsService.createTestimonial(entryTestimonialDto, file));
        }
    }

    @Operation(summary = "Endpoint to edit a testimonials.",
            description = "It provides the necessary mechanism to be able to edit a testimonials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = ValidationException.class))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema()))})
    
    @PutMapping("/{id}")
    public ResponseEntity<BasicTestimonialDTo> editTestimonial(
    		 @Parameter(description = "Id of the testimonials to be edited", example = "528f22c3-1f9c-493f-8334-c70b83b5b885")
    		@PathVariable String id,
    		@Valid @RequestPart(name = "file") EntryTestimonialDto entryTestimonialDto, Errors errors,
    		@RequestPart(name = "img", required = true) MultipartFile file) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        if (testimonialsService.existById(id)) {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(testimonialsService.updateTestimonial(id, entryTestimonialDto));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(testimonialsService.updateTestimonial(id, entryTestimonialDto, file));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
    
    @Operation(summary = "Endpoint to list all collated testimonials",
            description = "It provides the necessary mechanism to be able to list all the existing testimonials in a collated manner. "
                    + "Per page will list 10 testimonials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema()))})
    @GetMapping("/list")
    public ResponseEntity<Page<BasicTestimonialDTo>> getTestimonial(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(testimonialsService.getTestimonials(pageable));
    }

    @Operation(summary = "Endpoint to delete a testimonials.", 
    		description = "It provides the necessary mechanism to be able to eliminate a testimonials based on its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema()))})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTestimonial(
    		@Parameter(description = "Id of the testimonials to delete.", example = "528f22c3-1f9c-493f-8334-c70b83b5b885")
    		@PathVariable String id) {
        if (testimonialsService.deleteTestimonial(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Id not found", HttpStatus.NOT_FOUND);
        }
    }
}
