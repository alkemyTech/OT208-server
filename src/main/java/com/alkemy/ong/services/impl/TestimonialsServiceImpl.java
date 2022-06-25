package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.testimonial.EntryTestimonialDto;
import com.alkemy.ong.dto.response.testimonial.BasicTestimonialDTo;
import com.alkemy.ong.models.TestimonialsEntity;
import com.alkemy.ong.repositories.ITestimonialsRepository;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.TestimonialsService;
import com.alkemy.ong.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class TestimonialsServiceImpl extends BasicServiceImpl<TestimonialsEntity, String, ITestimonialsRepository> implements TestimonialsService {

    private static final Logger LOG = LoggerFactory.getLogger(TestimonialsServiceImpl.class);

    private final AWSS3Service awss3Service;

    public TestimonialsServiceImpl(ITestimonialsRepository repository, AWSS3Service awss3Service) {
        super(repository);
        this.awss3Service = awss3Service;
    }

    @Override
    public BasicTestimonialDTo createTestimonial(EntryTestimonialDto entryTestimonialDto, MultipartFile file) {

        TestimonialsEntity testimonialsEntity = ObjectMapperUtils.map(entryTestimonialDto, TestimonialsEntity.class);

        testimonialsEntity.setImage(awss3Service.uploadFile(file));

        repository.save(testimonialsEntity);

        return ObjectMapperUtils.map(testimonialsEntity, BasicTestimonialDTo.class);
    }

    @Override
    public BasicTestimonialDTo createTestimonial(EntryTestimonialDto entryTestimonialDto) {

        TestimonialsEntity testimonialsEntity = new TestimonialsEntity();
        ObjectMapperUtils.map(entryTestimonialDto, testimonialsEntity);

        testimonialsEntity = repository.save(testimonialsEntity);

        return ObjectMapperUtils.map(testimonialsEntity, BasicTestimonialDTo.class);
    }

    @Override
    public BasicTestimonialDTo updateTestimonial(String id, EntryTestimonialDto entryTestimonialDto, MultipartFile img) {
        TestimonialsEntity testimonialsEntity = findById(id).get();
        testimonialsEntity = ObjectMapperUtils.map(entryTestimonialDto, testimonialsEntity);
        testimonialsEntity.setImage(awss3Service.uploadFile(img));
        testimonialsEntity = this.edit(testimonialsEntity);
        return ObjectMapperUtils.map(testimonialsEntity, BasicTestimonialDTo.class);
    }

    @Override
    public BasicTestimonialDTo updateTestimonial(String id, EntryTestimonialDto entryTestimonialDto) {
        TestimonialsEntity testimonialsEntity = findById(id).get();
        testimonialsEntity = ObjectMapperUtils.map(entryTestimonialDto, testimonialsEntity);
        testimonialsEntity = this.edit(testimonialsEntity);
        return ObjectMapperUtils.map(testimonialsEntity, BasicTestimonialDTo.class);
    }

    @Override
    public Page<BasicTestimonialDTo> getTestimonials(Pageable pageable) {
        List<TestimonialsEntity> testimonialsEntities = repository.findAll();
        List<BasicTestimonialDTo> response;

        if (!testimonialsEntities.isEmpty()) {
            response = ObjectMapperUtils.mapAll(testimonialsEntities, BasicTestimonialDTo.class);
            final int start = (int) pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), response.size());
            return new PageImpl<>(response.subList(start, end), pageable, response.size());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("There's no testimonial"));
        }
    }

    @Override
    public Boolean deleteTestimonial(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            LOG.info("Testimonial deleted");
            return true;
        } else {
            LOG.error("Id not Found");
            return false;
        }
    }

}
