package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.testimonial.EntryTestimonialDto;
import com.alkemy.ong.dto.response.testimonial.BasicTestimonialDTo;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.services.TestimonialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

@RestController
@RequestMapping("/testimonials")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 1024*1024*15)
public class TestimonialsController {

    private final TestimonialsService testimonialsService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BasicTestimonialDTo> createTestiomonial(@Valid @RequestPart(name = "name",required = true) EntryTestimonialDto entryTestimonialDto,
                                                                  @RequestPart(name = "file",required = false) MultipartFile file){

        if(entryTestimonialDto.getName()==null){
           return ResponseEntity.badRequest().build();
        }
        if(entryTestimonialDto.getContent()==null){
            return ResponseEntity.badRequest().build();
        }

        if(file.isEmpty()){
            testimonialsService.createTestimonial(entryTestimonialDto);
            return ResponseEntity.ok().build();
        }else{
            testimonialsService.createTestimonial(entryTestimonialDto,file);
            return ResponseEntity.ok().build();
        }

    }
}
