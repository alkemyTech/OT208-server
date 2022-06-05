package com.alkemy.ong.services;

import com.alkemy.ong.dto.request.slide.SlideRequestDto;
import com.alkemy.ong.dto.response.slide.SlideDto;
import com.alkemy.ong.models.SlideEntity;

import java.util.List;

public interface SlideService extends BasicService<SlideEntity, String> {

    List<SlideDto> getAll();

    SlideDto getSlide(String id);

    SlideDto createSlide(SlideRequestDto dto);

}
