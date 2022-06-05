package com.alkemy.ong.services.impl;

import com.alkemy.ong.dto.request.slide.SlideRequestDto;
import com.alkemy.ong.dto.response.slide.SlideDto;
import com.alkemy.ong.models.OrganizationEntity;
import com.alkemy.ong.models.SlideEntity;
import com.alkemy.ong.repositories.IOrganizationRepository;
import com.alkemy.ong.repositories.ISlideRepository;
import com.alkemy.ong.services.AWSS3Service;
import com.alkemy.ong.services.SlideService;
import com.alkemy.ong.services.mappers.SlideMapper;
import com.alkemy.ong.utils.Base64Decode2Multipart;
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

    private final IOrganizationRepository organizationRepository;
    private final SlideMapper slideMapper;
    private final AWSS3Service awss3Service;

    public SlideServiceImpl(ISlideRepository repository, IOrganizationRepository organizationRepository, SlideMapper mapper, AWSS3Service awss3Service) {
        super(repository);
        this.slideMapper = mapper;
        this.organizationRepository = organizationRepository;
        this.awss3Service = awss3Service;
    }

    @Override
    public List<SlideDto> getAll() {
        List<SlideEntity> slides = repository.findAll();
        return slideMapper.entityList2DtoList(slides);
    }

    @Override
    public SlideDto getSlide(String id) {
        return slideMapper.entity2Dto(repository.getById(id));
    }

    @Transactional
    @Override
    public SlideDto createSlide(SlideRequestDto dto) {
        SlideEntity slideEntity = new SlideEntity();

        Optional<OrganizationEntity> op = organizationRepository.findById(dto.getOrganizationId());
            if (op.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ("Bad organization ID or null parameter" + slideEntity.getOrganizationEntityId().getId()));
            }
        slideEntity.setOrganizationEntityId(op.get());

        Integer slideListMax = repository.getMaxOrder();
            if (dto.getOrder() == null) {
                slideEntity.setOrder(1 + slideListMax);
            } else if (dto.getOrder() != slideListMax || dto.getOrder() != 0) {
                slideEntity.setOrder(dto.getOrder());
            } else if (dto.getOrder() == slideListMax) {
                slideEntity.setOrder(slideListMax + 1);
            }

        MultipartFile decodedImage = base64Image2MultipartFile(dto.getImageUrl());
        slideEntity.setImageUrl(awss3Service.uploadFile(decodedImage));

        slideEntity.setText(dto.getText());

        SlideEntity entityUpdated = repository.save(slideEntity);
        return slideMapper.entity2Dto(entityUpdated);
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
