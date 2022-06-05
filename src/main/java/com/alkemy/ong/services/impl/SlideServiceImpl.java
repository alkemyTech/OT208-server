package com.alkemy.ong.services.Impl;

import com.alkemy.ong.dto.response.slide.SlideDto;
import com.alkemy.ong.models.SlideEntity;
import com.alkemy.ong.repositories.ISlideRepository;
import com.alkemy.ong.services.SlideService;
import com.alkemy.ong.services.impl.BasicServiceImpl;
import com.alkemy.ong.services.mappers.SlideMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SlideServiceImpl extends BasicServiceImpl<SlideEntity, String, ISlideRepository> implements SlideService {

    private final ISlideRepository slideRepository;
    private final SlideMapper slideMapper;

    public SlideServiceImpl(ISlideRepository repository, SlideMapper mapper) {
        super(repository);
        this.slideMapper = mapper;
        this.slideRepository = repository;
    }

    @Override
    public List<SlideDto> getAll() {
        List<SlideEntity> slides = slideRepository.findAll();
        List<SlideDto> result = slideMapper.entityList2DtoList(slides);
        return result;
    }

    @Override
    public SlideDto getSlide(String id) {
        return slideMapper.entity2Dto(repository.getById(id));
    }

}
