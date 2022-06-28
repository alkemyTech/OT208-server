package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.slide.EntrySlideDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.models.SlideEntity;
import com.alkemy.ong.repositories.IOrganizationRepository;
import com.alkemy.ong.repositories.ISlideRepository;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.SlideService;
import com.alkemy.ong.utils.Base64Decode2Multipart;
import com.alkemy.ong.utils.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Service
public class SlideServiceImpl extends BasicServiceImpl<SlideEntity, String, ISlideRepository> implements SlideService {

    private static final Logger LOG = LoggerFactory.getLogger(SlideServiceImpl.class);

    private final IOrganizationRepository organizationRepository;
    private final AWSS3Service awss3Service;

    public SlideServiceImpl(ISlideRepository repository, IOrganizationRepository organizationRepository, AWSS3Service awss3Service) {
        super(repository);
        this.organizationRepository = organizationRepository;
        this.awss3Service = awss3Service;
    }

    @Override
    public List<SlideResponseDto> getAll() {
        List<SlideEntity> slides = repository.findAll();
        if (slides.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("No Slides found"));
        }
        return ObjectMapperUtils.mapAll(slides, SlideResponseDto.class);
    }

    @Override
    public List<SlideResponseDto> getAllByOrganizationId(String id) {
        List<SlideEntity> listEntity = repository.findByOrganizationId(id);
        return ObjectMapperUtils.mapAll(listEntity, SlideResponseDto.class);
    }

    @Override
    public SlideResponseDto getSlide(String id) {
        Optional<SlideEntity> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ("Slide with id " + id + " not found"));
        }
        return ObjectMapperUtils.map(op.get(), SlideResponseDto.class);
    }

    @Override
    @Transactional
    public SlideResponseDto createSlide(EntrySlideDto dto) {
        OrganizationEntity ong = organizationRepository.findAll().get(0);
        if (ong == null) {
            LOG.error("Failure to create a slide, Organization not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
        }

        SlideEntity slideEntity = new SlideEntity();
        slideEntity.setOrganizationEntity(ong);
        Integer slideListMax = repository.getMaxOrder();
        if (slideListMax == null) {
            slideListMax = 0;
        }
        if (dto.getOrder() == null) {
            slideEntity.setOrder(1 + slideListMax);
        } else if (dto.getOrder() != slideListMax || dto.getOrder() != 0) {
            slideEntity.setOrder(dto.getOrder());
        } else if (slideListMax == dto.getOrder()) {
            slideEntity.setOrder(slideListMax + 1);
        }
        MultipartFile decodedImage = base64Image2MultipartFile(dto.getImageUrl());
        slideEntity.setImageUrl(awss3Service.uploadFile(decodedImage));
        slideEntity.setText(dto.getText());
        LOG.info("Create Slide: {}", slideEntity.getId());
        return ObjectMapperUtils.map(repository.save(slideEntity), SlideResponseDto.class);
    }

    @Override
    @Transactional
    public SlideResponseDto updateSlide(String id, MultipartFile file) {

        Optional<SlideEntity> op = repository.findById(id);
        if (op.isEmpty()) {
            LOG.error("Failure to update a slide, Slide with id {} not found", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ("Bad slide ID or null parameter" + id));
        }
        SlideEntity slideEntity = op.get();
        slideEntity.setImageUrl(awss3Service.uploadFile(file));
        LOG.info("Update Slide: {}", slideEntity.getId());
        return ObjectMapperUtils.map(repository.save(slideEntity), SlideResponseDto.class);
    }

    @Override
    @Transactional
    public Boolean deleteSlide(String id) {
        if (!repository.existsById(id)) {
            LOG.error("Failure to delete a slide, Slide with id {} not found", id);
            return false;
        } else {
            repository.deleteById(id);
            LOG.info("Delete Slide: {}", id);
            return true;
        }
    }

    //Base64 Decoded
    private MultipartFile base64Image2MultipartFile(String image64) {
        String[] baseString = image64.split(",");
        byte[] byteArray = Base64.getDecoder().decode(baseString[1]);

        for (int i = 0; i < byteArray.length; ++i) {
            if (byteArray[i] < 0) {
                byteArray[i] += 256;
            }
        }
        return new Base64Decode2Multipart(byteArray, baseString[0]);
    }

}
