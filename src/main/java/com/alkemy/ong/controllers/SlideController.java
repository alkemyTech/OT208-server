package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.slide.EntrySlideDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.SlideService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/slides")
@AllArgsConstructor
public class SlideController {

    private SlideService slideService;

    @GetMapping
    public ResponseEntity<List<SlideResponseDto>> getAll() {
        return ResponseEntity.ok().body(slideService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SlideResponseDto> getSlide(@PathVariable String id) {
        if (!slideService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(slideService.getSlide(id));
    }

    @PostMapping
    public ResponseEntity<SlideResponseDto> createSlide(@Valid @RequestBody EntrySlideDto slideDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
        return new ResponseEntity<SlideResponseDto>(slideService.createSlide(slideDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SlideResponseDto> updateSlide(@PathVariable String id,
                                                        @RequestPart(value = "file") MultipartFile file) {
        if (!slideService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().body(slideService.updateSlide(id, file));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSlide(@PathVariable String id) {
        if (!slideService.deleteSlide(id)) {
            return new ResponseEntity<>("ID not Found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
