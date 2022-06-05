package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.slide.SlideRequestDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.services.SlideService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        if (slideService.findById(id).isPresent()) {
            return new ResponseEntity<SlideResponseDto>(slideService.getSlide(id), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<SlideResponseDto> createSlide(@RequestBody SlideRequestDto slideDto) {
        return new ResponseEntity<SlideResponseDto>(slideService.createSlide(slideDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SlideResponseDto> updateSlide(@PathVariable String id,
                                                        @RequestPart(value = "file") MultipartFile file) {
        if (slideService.findById(id).isPresent()) {
            return new ResponseEntity<SlideResponseDto>(slideService.updateSlide(id, file), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSlide(@PathVariable String id) {
        if (slideService.findById(id).isPresent()) {
            slideService.deleteSlide(id);
            return new ResponseEntity<>("It was deleted on: " + id, HttpStatus.OK);
        } else return new ResponseEntity<>("ID not found or null: " + id, HttpStatus.NOT_FOUND);
    }

}
