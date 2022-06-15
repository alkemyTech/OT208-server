package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.testimonial.EntryTestimonialDto;
import com.alkemy.ong.dto.response.testimonial.BasicTestimonialDTo;
import com.alkemy.ong.models.TestimonialsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface TestimonialsService extends BasicService<TestimonialsEntity, String> {

    BasicTestimonialDTo createTestimonial(EntryTestimonialDto entryTestimonialDto, MultipartFile file);

    BasicTestimonialDTo createTestimonial(EntryTestimonialDto entryTestimonialDto);

    BasicTestimonialDTo updateTestimonial(String id, EntryTestimonialDto entryTestimonialDto,String image);

    Page<BasicTestimonialDTo> getTestimonials(Pageable pageable);

}
