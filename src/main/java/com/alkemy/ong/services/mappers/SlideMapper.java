package com.alkemy.ong.services.mappers;

import com.alkemy.ong.dto.request.slide.SlideRequestDto;
import com.alkemy.ong.dto.response.slide.SlideResponseDto;
import com.alkemy.ong.models.SlideEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SlideMapper {

    private final ModelMapper mapper;

    public SlideResponseDto entity2Dto(SlideEntity slide) {
        return mapper.map(slide, SlideResponseDto.class);
    }

    public SlideRequestDto entity2DtoRequest(SlideEntity slide) {
        return mapper.map(slide, SlideRequestDto.class);
    }

    public SlideEntity dtoRequest2Entity(SlideRequestDto requestDto) {
        return mapper.map(requestDto, SlideEntity.class);
    }

    public SlideEntity dto2Entity(SlideResponseDto dto) {
        return mapper.map(dto, SlideEntity.class);
    }

    public List<SlideResponseDto> entityList2DtoList(List<SlideEntity> slides) {
        return slides.stream().map(this::entity2Dto).collect(Collectors.toList());
    }

    public List<SlideEntity> dtoList2EntityList(List<SlideResponseDto> dtos) {
        List<SlideEntity> slides = new ArrayList<>();
        for (SlideResponseDto dto : dtos) slides.add(this.dto2Entity(dto));
        return slides;
    }


   /* SlideDtoFull slideEntityToSlideDtoFull(SlideEntity slideEntity);
    SlideResponseDto slideEntityToSlideDto(SlideDtoFull SlideDtoFull);
    SlideEntity slideDtoFullToSlideEntity(SlideDtoFull slideDtoFull);*/

}
