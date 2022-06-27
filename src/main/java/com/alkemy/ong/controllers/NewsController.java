package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.news.EntryEditNewsDto;
import com.alkemy.ong.dto.request.news.EntryNewsDto;
import com.alkemy.ong.dto.response.news.BasicNewsDto;
import com.alkemy.ong.exeptions.CategoryNotExistException;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.NewsEntity;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.CategoryService;
import com.alkemy.ong.services.NewsService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.util.Optional;

@Tag(name = "Testimonials", description = "Enpoints to be able to inform the organization news.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@MultipartConfig(maxFileSize = 1024 * 1024 * 15)
public class NewsController {

    private final NewsService newsService;
    private final AWSS3Service awss3Service;
    private final CategoryService categoryService;

    @Operation(summary = "Get a news",
            description = "Get a news item if it already exists in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok Exists By Id"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    @GetMapping("/{id}")
    public ResponseEntity<BasicNewsDto> getNews(@PathVariable String id) {
        Optional<NewsEntity> newsEntity = newsService.findById(id);

        if (!newsEntity.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ObjectMapperUtils.map(newsEntity, BasicNewsDto.class));
    }

    @Operation(summary = "Create a news",
            description = "Endpoint to allow the administrator user to add a new " +
                    "feature to the system in order to report the organisation's news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BasicNewsDto> createNews(
            @Valid @RequestPart(name = "news", required = true) EntryNewsDto entryNewsDto,
            Errors errors,
            @RequestPart(name = "newsImage", required = true) MultipartFile image) {

        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        if (!categoryService.existById(entryNewsDto.getCategoryIdId())) {
            throw new CategoryNotExistException(entryNewsDto.getCategoryIdId());
        }
        if (image.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagen can not be empty");
        }

        NewsEntity newsEntity = ObjectMapperUtils.map(entryNewsDto, NewsEntity.class);
        newsEntity.setType("news");
        String pathImage = awss3Service.uploadFile(image);
        newsEntity.setImage(pathImage);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ObjectMapperUtils.map(newsService.save(newsEntity), BasicNewsDto.class));
    }

    @Operation(summary = "Update a news",
            description = "Endpoint for the administrator user to update an existing " +
                    "development in order to keep the information up to date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request")})
    @PutMapping("/{id}")
    public ResponseEntity<BasicNewsDto> editNews(
            @PathVariable String id,
            @Valid @RequestPart(name = "news", required = true) EntryEditNewsDto entryEditNewsDto,
            Errors errors,
            @RequestPart(name = "newsImage", required = true) MultipartFile image) {

        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        if (!categoryService.existById(entryEditNewsDto.getCategory())) {
            throw new CategoryNotExistException(entryEditNewsDto.getCategory());
        }
        Optional<NewsEntity> newsEntityOp = newsService.findById(id);

        if (!newsEntityOp.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        NewsEntity newsEntity = newsEntityOp.get();
        newsEntity = ObjectMapperUtils.map(entryEditNewsDto, newsEntity);
        newsEntity.setCategoryId(categoryService.findById(entryEditNewsDto.getCategory()).get());

        if (!image.isEmpty()) {
            String pathImage = awss3Service.uploadFile(image);
            newsEntity.setImage(pathImage);
        }

        return ResponseEntity.ok(ObjectMapperUtils.map(newsService.edit(newsEntity), BasicNewsDto.class));
    }

    @Operation(summary = "Delete a news",
            description = "Endpoint for the administrator user can delete an " +
                    "existing activity in order to keep the information up to date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request")})
    @DeleteMapping("/{id}")
    public ResponseEntity<BasicNewsDto> deleteNews(@PathVariable String id) {
        Optional<NewsEntity> newsEntity = newsService.findById(id);

        if (!newsEntity.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        newsService.delete(newsEntity.get());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get pegeable news",
            description = "Endpoint for the user to display paginated news " +
                    "results in order to increase the speed of response.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request")})
    @GetMapping("/list")
    public ResponseEntity<Page<BasicNewsDto>> getNews(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(newsService.getNews(pageable));
    }
}
