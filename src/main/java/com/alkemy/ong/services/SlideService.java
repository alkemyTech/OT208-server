package com.alkemy.ong.services;

import com.alkemy.ong.dto.response.slide.SlideDto;
import com.alkemy.ong.models.SlideEntity;

import java.util.List;

public interface SlideService extends BasicService<SlideEntity, String> {

    public List<SlideDto> getAll();

    public SlideDto getSlide(String id);

}
