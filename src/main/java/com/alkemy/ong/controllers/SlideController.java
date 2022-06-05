package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.slide.SlideRequestDto;
import com.alkemy.ong.dto.response.slide.SlideDto;
import com.alkemy.ong.services.SlideService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slides")
@AllArgsConstructor
public class SlideController {

    private SlideService slideService;

    @GetMapping
    public ResponseEntity<List<SlideDto>> getAll() {
        return ResponseEntity.ok().body(slideService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SlideDto> getSlide(@PathVariable String id) {
        if(slideService.findById(id).isPresent()) {
            return new ResponseEntity<SlideDto>(slideService.getSlide(id), HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<SlideDto> createSlide(@RequestBody SlideRequestDto slideDto) {
        return new ResponseEntity<SlideDto>(slideService.createSlide(slideDto), HttpStatus.CREATED);
    }

}
