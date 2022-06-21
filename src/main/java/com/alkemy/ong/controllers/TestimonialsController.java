package com.alkemy.ong.controllers;

import com.alkemy.ong.dto.request.testimonial.EntryTestimonialDto;
import com.alkemy.ong.dto.response.testimonial.BasicTestimonialDTo;
import com.alkemy.ong.exeptions.ValidationException;
import com.alkemy.ong.models.TestimonialsEntity;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.TestimonialsService;
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

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/testimonials")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 1024*1024*15)
public class TestimonialsController {

    private final TestimonialsService testimonialsService;
    private final AWSS3Service awss3Service;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BasicTestimonialDTo> createTestiomonial(@Valid @RequestPart(name = "name",required = true) EntryTestimonialDto entryTestimonialDto,
                                                                  @RequestPart(name = "file",required = false) MultipartFile file){

        if(entryTestimonialDto.getName()==null){
           return ResponseEntity.badRequest().build();
        }
        if(entryTestimonialDto.getContent()==null){
            return ResponseEntity.badRequest().build();
        }

        if (file.isEmpty()) {
            testimonialsService.createTestimonial(entryTestimonialDto);
            return ResponseEntity.ok().build();
        } else {
            testimonialsService.createTestimonial(entryTestimonialDto, file);
            return ResponseEntity.ok().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicTestimonialDTo>editTestimonial(
                                                             @Valid @RequestPart(name = "name") EntryTestimonialDto entryTestimonialDto,
                                                             @PathVariable String id, Errors errors,
                                                             @RequestPart(name = "file",required = false) MultipartFile file){

        if(errors.hasErrors()){
            throw new ValidationException(errors.getFieldErrors());
        }
        if(file.isEmpty()){
            return ResponseEntity.ok().body(testimonialsService.updateTestimonial(id,entryTestimonialDto,""));
        }else
            return ResponseEntity.ok().body(testimonialsService.updateTestimonial(id,entryTestimonialDto,awss3Service.uploadFile(file)));
    }



    @GetMapping("/list")
    public ResponseEntity<Page<BasicTestimonialDTo>> getMembers(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(testimonialsService.getTestimonials(pageable));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTestimonial(@PathVariable String id){
        if(testimonialsService.deleteTestimonial(id)){
            return new ResponseEntity<>("It Was Delete", HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>("Id not found",HttpStatus.NOT_FOUND);
        }
    }
}
