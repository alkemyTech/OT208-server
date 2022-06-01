package com.alkemy.ong.services.mappers;

import com.alkemy.ong.dto.SlideDto;
import com.alkemy.ong.models.SlideEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SlideMapper {

    private final ModelMapper mapper;

    public SlideDto entity2Dto(SlideEntity slide) {
        return mapper.map(slide, SlideDto.class);
    }

    public List<SlideDto> entityList2DtoList(List<SlideEntity> slides) {

        List<SlideDto> dtos = new ArrayList<>();
        for (SlideEntity entity : slides) {
            dtos.add(this.entity2Dto(entity));
        }
        return dtos;
    }

}
